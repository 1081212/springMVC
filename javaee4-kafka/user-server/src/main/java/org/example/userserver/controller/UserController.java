package org.example.userserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.example.orderserver.entity.Order;
import org.example.shipserver.entity.Ship;
import org.example.userserver.entity.Result;
import org.example.userserver.entity.User;
import org.example.userserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/user-server")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @GetMapping("/kafkaTest")
    public String kafkaTest(){
        kafkaTemplate.send("user-server-kafkaTest-request","hello kafka");
        return "user-server-kafkaTest-request send";
    }

    @KafkaListener(topics = "user-server-kafkaTest-response",groupId = "user-1")
    public void uListen(String response){
        System.out.println("listen response suc!");
        System.out.println("res:"+response);
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    boolean getAllShipsRes = false;
    String getAllShipsStr = "";

    private ConcurrentHashMap<String, CompletableFuture<String>> futures = new ConcurrentHashMap<>();



    /**
     * 获取用户
     * @return
     */

    @KafkaListener(topics = "user-server-getAllUser-request",groupId = "user-server-1")
    public void getAllUser(String request) {
        String topic = "user-server-getAllUser-response";
        try {
            JSONObject requestObj = JSON.parseObject(request);
            String requestId = requestObj.getString("requestId"); // 获取请求ID
            int pageNum = requestObj.getIntValue("pageNum");
            int pageSize = 12; // 每页显示12条数据
            int total = userService.countAllUsers();
            int totalPages = (int) Math.ceil((double) total / pageSize); // 计算总页数
            if (pageNum < 1) {
                pageNum = 1;
            } else if (pageNum > totalPages) {
                pageNum = totalPages;
            }
            int start = (pageNum - 1) * pageSize; // 计算起始记录的下标
            List<User> userList = userService.getUsersByPage(start,pageSize);

            PageInfo<User> pageInfo = new PageInfo<>(userList);
            pageInfo.setPageNum(pageNum);
            pageInfo.setTotal(total);
            pageInfo.setPages(totalPages);
            if (pageNum!=totalPages){
                pageInfo.setHasNextPage(true);
                pageInfo.setNextPage(pageNum+1);
            }
            if (pageNum!=1){
                pageInfo.setHasPreviousPage(true);
                pageInfo.setPrePage(pageNum-1);
            }

            JSONObject responseObj = new JSONObject();
            responseObj.put("requestId", requestId); // 将请求ID放入响应中

            System.out.println("pageinfo:"+pageInfo);
            if (total==0){
                logger.info("getAllUser() use but Don't have any ship!");
                responseObj.put("result", new Result(true,"Don't have any ship!",404)); // 将结果放入响应中
                kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
            }else {
                logger.info("getAllUser() use successful!");
                responseObj.put("result", new Result(true,"Search ship successful!",200,pageInfo)); // 将结果放入响应中
                kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
            }
        }catch (Exception e){
            logger.error("Error while executing getAllUser()!",e);
            JSONObject responseObj = new JSONObject();
            JSONObject requestObj = JSON.parseObject(request);
            String requestId = requestObj.getString("requestId"); // 获取请求ID

            responseObj.put("requestId", requestId); // 将请求ID放入响应中
            responseObj.put("result", new Result(false,"Search error:"+e.getMessage(),422)); // 将结果放入响应中
            kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
        }
    }


    @GetMapping("/getAllShips/{pageNum}")
    @ApiOperation(value = "外部接口-根据页号获取所有货船")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class)
    })
    public CompletableFuture<Result> getAllShips(@ApiParam(value = "页号",required = true) @PathVariable("pageNum") int pageNum){
        try {
            String requestId = UUID.randomUUID().toString(); // Create a unique ID for this request
            JSONObject requestObj = new JSONObject();
            requestObj.put("requestId", requestId);
            requestObj.put("pageNum", pageNum);
            kafkaTemplate.send("ship-server-getAllShips-request", JSON.toJSONString(requestObj));

            CompletableFuture<String> future = new CompletableFuture<>();
            futures.put(requestId, future); // Store the future so that we can complete it later
//            return future.thenApply(this::parseResponseList);
            return future.thenApply(response -> parseResponseList(response, Ship.class));
        } catch (Exception e) {
            logger.error("Error while executing getAllShips()", e);
            CompletableFuture<Result> errorFuture = new CompletableFuture<>();
            errorFuture.completeExceptionally(e);
            return errorFuture;
        }
    }



    @KafkaListener(topics = {"ship-server-getAllShips-response","order-server-payOrder-response",
            "order-server-over-response","order-server-saveOrder-response"}, groupId = "user-server-1")
    public void getResponse(String response) {
        JSONObject responseObj = JSON.parseObject(response);
        String requestId = responseObj.getString("requestId");
        if (requestId != null){
            CompletableFuture<String> future = futures.remove(requestId);
            if (future != null) {
                future.complete(response);
            } else {
                logger.warn("No future found for request ID: " + requestId);
            }
        }
    }


    @PostMapping("/saveOrder")
    @ApiOperation(value = "外部接口-用户下单")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class)
    })
    public CompletableFuture<Result> saveOrder(@ApiParam(value = "订单实体",type = "body",required = true)@RequestBody Order order){
        try {
            String requestId = UUID.randomUUID().toString(); // Create a unique ID for this request
            JSONObject requestObj = new JSONObject();
            requestObj.put("requestId", requestId);
            requestObj.put("order", order);
            kafkaTemplate.send("order-server-saveOrder-request", JSON.toJSONString(requestObj));

            CompletableFuture<String> future = new CompletableFuture<>();
            futures.put(requestId, future); // Store the future so that we can complete it later
            return future.thenApply(this::parseResponse);
        }catch (Exception e){
            logger.error("Error while executing saveOrder()",e);
            CompletableFuture<Result> errorFuture = new CompletableFuture<>();
            errorFuture.completeExceptionally(e);
            return errorFuture;
        }
    }

    @PostMapping("/payOrder/{orderId}")
    @ApiOperation("外部接口-用户支付")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class)
    })
    public CompletableFuture<Result> payOrder(@ApiParam(value = "订单id",required = true)@PathVariable("orderId") Integer orderId){
        try {
            String requestId = UUID.randomUUID().toString(); // Create a unique ID for this request
            JSONObject requestObj = new JSONObject();
            requestObj.put("requestId", requestId);
            requestObj.put("orderId", orderId);
            kafkaTemplate.send("order-server-payOrder-request", JSON.toJSONString(requestObj));

            CompletableFuture<String> future = new CompletableFuture<>();
            futures.put(requestId, future); // Store the future so that we can complete it later
            return future.thenApply(this::parseResponse);
        }catch (Exception e){
            logger.error("Error while executing saveOrder()",e);
            CompletableFuture<Result> errorFuture = new CompletableFuture<>();
            errorFuture.completeExceptionally(e);
            return errorFuture;
        }
    }


    @PostMapping("/over/{orderId}")
    @ApiOperation("外部接口-用户确认订单接收")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class)
    })
    public CompletableFuture<Result> over(@ApiParam(value = "订单id",required = true)@PathVariable("orderId") Integer orderId){
        try {
            String requestId = UUID.randomUUID().toString(); // Create a unique ID for this request
            JSONObject requestObj = new JSONObject();
            requestObj.put("requestId", requestId);
            requestObj.put("orderId", orderId);
            kafkaTemplate.send("order-server-over-request", JSON.toJSONString(requestObj));

            CompletableFuture<String> future = new CompletableFuture<>();
            futures.put(requestId, future); // Store the future so that we can complete it later
            return future.thenApply(this::parseResponse);
        }catch (Exception e){
            logger.error("Error while executing saveOrder()",e);
            CompletableFuture<Result> errorFuture = new CompletableFuture<>();
            errorFuture.completeExceptionally(e);
            return errorFuture;
        }
    }

    private <T> Result parseResponseList(String response, Class<T> clazz) {
        JSONObject object = JSON.parseObject(response);
        object = object.getJSONObject("result");
        if (object.getBoolean("flag").equals(true)) {
            JSONObject data = object.getJSONObject("data");
            JSONArray jsonArray = data.getJSONArray("list");
            List<T> items = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                T item = jsonObject.toJavaObject(clazz);
                items.add(item);
            }
            logger.info("parseResponseList() use successful!");
            return new Result(true, "suc", 200, items);
        } else {
            logger.info("parseResponseList() client error");
            return new Result(false, object.getString("msg"), object.getInteger("statusCode"));
        }
    }

    private <T>  Result parseResponseObj(String response, Class<T> clazz) {
        JSONObject object = JSON.parseObject(response);
        object = object.getJSONObject("result");
        if (object.getBoolean("flag").equals(true)) {
            JSONObject data = object.getJSONObject("data");
            T item = data.toJavaObject(clazz);
            logger.info("parseResponseObj() use successful!");
            return new Result(true, "suc", 200, item);
        } else {
            logger.info("getAllShips() client error");
            return new Result(false, object.getString("msg"), object.getInteger("statusCode"));
        }
    }
    private Result parseResponse(String response) {
        JSONObject object = JSON.parseObject(response);
        object = object.getJSONObject("result");
        if (object.getBoolean("flag").equals(true)) {
            logger.info("parseResponse() use successful!");
            return new Result(true, "suc", 200);
        } else {
            logger.info("parseResponse() client error");
            return new Result(false, object.getString("msg"), object.getInteger("statusCode"));
        }
    }

}
