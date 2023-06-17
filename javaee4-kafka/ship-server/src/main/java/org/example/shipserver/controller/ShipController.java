package org.example.shipserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.checkerframework.checker.units.qual.A;
import org.example.shipserver.entity.Result;
import org.example.shipserver.entity.Ship;
import org.example.shipserver.service.ShipService;
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
@RequestMapping("/ship-server")
public class ShipController {

    @Autowired
    private ShipService shipService;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    private ConcurrentHashMap<String, CompletableFuture<String>> futures = new ConcurrentHashMap<>();


    private static final Logger logger = LoggerFactory.getLogger(ShipController.class);

    @KafkaListener(topics = "user-server-kafkaTest-request",groupId = "ship-consumer-1")
    public void kafkaListenTest(String request){
        System.out.println("listen suc!");
        System.out.println("the msg is: "+request);
        kafkaTemplate.send("user-server-kafkaTest-response","listen suc!");
    }


    @KafkaListener(topics = "ship-server-getAllShips-request",groupId = "ship-server-1")
    public void getAllShips(String request) {
        try {
            JSONObject requestObj = JSON.parseObject(request);
            String requestId = requestObj.getString("requestId"); // 获取请求ID
            int pageNum = requestObj.getIntValue("pageNum");
            int pageSize = 12; // 每页显示12条数据
            int total = shipService.countAllShip(); // 获取总记录数
            int totalPages = (int) Math.ceil((double) total / pageSize); // 计算总页数
            if (pageNum < 1) {
                pageNum = 1;
            } else if (pageNum > totalPages) {
                pageNum = totalPages;
            }
            int start = (pageNum - 1) * pageSize; // 计算起始记录的下标
            List<Ship> shipList = shipService.getShipsByPage(start, pageSize);

            PageInfo<Ship> pageInfo = new PageInfo<>(shipList);
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
                logger.info("getAllShips() use but Don't have any ship!");
                responseObj.put("result", new Result(true,"Don't have any ship!",404)); // 将结果放入响应中
                kafkaTemplate.send("ship-server-getAllShips-response",JSON.toJSONString(responseObj));
            }else {
                logger.info("getAllShips() use successful!");
                responseObj.put("result", new Result(true,"Search ship successful!",200,pageInfo)); // 将结果放入响应中
                kafkaTemplate.send("ship-server-getAllShips-response",JSON.toJSONString(responseObj));
            }
        }catch (Exception e){
            logger.error("Error while executing getAllShips()!",e);
            JSONObject responseObj = new JSONObject();
            JSONObject requestObj = JSON.parseObject(request);
            String requestId = requestObj.getString("requestId"); // 获取请求ID

            responseObj.put("requestId", requestId); // 将请求ID放入响应中
            responseObj.put("result", new Result(false,"Search error:"+e.getMessage(),422)); // 将结果放入响应中
            kafkaTemplate.send("ship-server-getAllShips-response",JSON.toJSONString(responseObj));
        }
    }


    @GetMapping("/getDetail/{shipId}")
    @ApiOperation(value = "微服务间接口-获取船只详情,需传递订单id")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString")
    })
    public String getDetail(@ApiParam(value = "船只id",required = true) @PathVariable("shipId") Integer shipId) {
        try {
            Ship ship = shipService.getShipById(shipId);
            System.out.println(ship);
            if (ship!=null){
                logger.info("getDetail() use successful!");
                return JSON.toJSONString(new Result(true,"Search successful!",200,ship));
            }else {
                logger.info("getDetail() use but Not find any ship!");
                return JSON.toJSONString(new Result(true,"Not find any ship!",404));
            }
        }catch (Exception e){
            logger.error("Error while executing getDetail()",e);
            return JSON.toJSONString(new Result(false,"Search error:"+e.getMessage(),422));
        }

    }

    @GetMapping("/getShipsByLevel/{level}")
    @ApiOperation(value = "微服务间接口-获取对应等级的船只")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString")
    })
    public String getShipsByLevel(@ApiParam(value = "船只等级",required = true)@PathVariable("level") String level){
        try {
            List<Ship> ships = shipService.getShipsByLevel(level);
            if (ships!=null){
                logger.info("getShipsByLevel() use successful!");
                return JSON.toJSONString(new Result(true,"successful!",200,ships));
            }else {
                logger.info("getShipsByLevel() use but Not find any ship!");
                return JSON.toJSONString(new Result(true,"Not find any ship!",404));
            }
        }catch (Exception e){
            logger.error("Error while executing getShipsByLevel()",e);
            return JSON.toJSONString(new Result(false,"Search error:"+e.getMessage(),422));
        }
    }

    @PostMapping("/accept/{orderId}/{shipId}")
    @ApiOperation(value = "外部接口-货船接受调派")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString")
    })
    public CompletableFuture<Result> accept(@ApiParam(value = "订单id",required = true)@PathVariable("orderId") Integer orderId,@ApiParam(value = "货船id",required = true)@PathVariable("shipId") Integer shipId){
        try {
            String requestId = UUID.randomUUID().toString(); // Create a unique ID for this request
            JSONObject requestObj = new JSONObject();
            requestObj.put("requestId", requestId);
            requestObj.put("orderId", orderId);
            requestObj.put("shipId",shipId);
            kafkaTemplate.send("order-server-accept-request", JSON.toJSONString(requestObj));

            CompletableFuture<String> future = new CompletableFuture<>();
            futures.put(requestId, future); // Store the future so that we can complete it later
            return future.thenApply(this::parseResponse);
        }catch (Exception e){
            logger.error("Error while executing accept()",e);
            CompletableFuture<Result> errorFuture = new CompletableFuture<>();
            errorFuture.completeExceptionally(e);
            return errorFuture;
        }
    }

    @PostMapping("/arrival/{orderId}")
    @ApiOperation(value = "外部接口-货船到达目的地")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString")
    })
    public CompletableFuture<Result> arrival(@ApiParam(value = "订单id",required = true)@PathVariable("orderId") Integer orderId){
        try {
            String requestId = UUID.randomUUID().toString(); // Create a unique ID for this request
            JSONObject requestObj = new JSONObject();
            requestObj.put("requestId", requestId);
            requestObj.put("orderId", orderId);
            kafkaTemplate.send("order-server-arrival-request", JSON.toJSONString(requestObj));

            CompletableFuture<String> future = new CompletableFuture<>();
            futures.put(requestId, future); // Store the future so that we can complete it later
            return future.thenApply(this::parseResponse);
        }catch (Exception e){
            logger.error("Error while executing accept()",e);
            CompletableFuture<Result> errorFuture = new CompletableFuture<>();
            errorFuture.completeExceptionally(e);
            return errorFuture;
        }
    }

    @KafkaListener(topics = {"order-server-accept-response"}, groupId = "ship-server-1")
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
