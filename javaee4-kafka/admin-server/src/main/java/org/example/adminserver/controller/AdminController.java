package org.example.adminserver.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import org.example.orderserver.entity.Order;
import org.example.orderserver.entity.Result;
import org.example.shipserver.entity.Ship;
import org.example.userserver.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.service.ApiDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/admin-server")
public class AdminController {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    private ConcurrentHashMap<String, CompletableFuture<String>> futures = new ConcurrentHashMap<>();


    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/getAllUser/{pageNum}")
    @ApiOperation(value = "外部接口-获取所有用户")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class),
            @ApiResponse(code = 503,message = "inner error!",response = Result.class)

    })
    public CompletableFuture<Result> getAllUser(@ApiParam(value = "页号",required = true)@PathVariable("pageNum") int pageNum) {
        try {
            String requestId = UUID.randomUUID().toString(); // Create a unique ID for this request
            JSONObject requestObj = new JSONObject();
            requestObj.put("requestId", requestId);
            requestObj.put("pageNum", pageNum);
            kafkaTemplate.send("user-server-getAllUser-request", JSON.toJSONString(requestObj));

            CompletableFuture<String> future = new CompletableFuture<>();
            futures.put(requestId, future); // Store the future so that we can complete it later
//            return future.thenApply(this::parseResponseList);
            return future.thenApply(response -> parseResponseList(response, User.class));
        } catch (Exception e) {
            logger.error("Error while executing getAllUser()", e);
            CompletableFuture<Result> errorFuture = new CompletableFuture<>();
            errorFuture.completeExceptionally(e);
            return errorFuture;
        }
    }

    @PostMapping("/insertUser")
    @ApiOperation("添加用户")
    public String insertUser(){
        return "";
    }

    @DeleteMapping("/deleteUser")
    @ApiOperation("删除用户")
    public String deleteUser(){
        return "";
    }

    @PostMapping("updateUser")
    @ApiOperation("修改用户信息")
    public String updateUser(){
        return "";
    }


    @GetMapping("/getAllShips/{pageNum}")
    @ApiOperation(value = "外部接口-根据页号获取所有船只")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class),
            @ApiResponse(code = 503,message = "inner error!",response = Result.class)

    })
    public CompletableFuture<Result> getAllShips(@ApiParam(value = "页号",required = true)@PathVariable("pageNum") int pageNum) {
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

    @PostMapping("/order-server/dispatch/{orderId}/{shipId}")
    @ApiOperation(value = "外部接口-派单")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class),
            @ApiResponse(code = 503,message = "inner error!",response = Result.class)

    })
    public CompletableFuture<Result> dispatch(@PathVariable("orderId") Integer orderId, @PathVariable("shipId") Integer shipId){
        try {
            String requestId = UUID.randomUUID().toString(); // Create a unique ID for this request
            JSONObject requestObj = new JSONObject();
            requestObj.put("requestId", requestId);
            requestObj.put("orderId", orderId);
            requestObj.put("shipId",shipId);
            kafkaTemplate.send("order-server-dispatch-request", JSON.toJSONString(requestObj));

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

    @GetMapping("/order-server/getNoGo")
    @ApiOperation(value = "外部接口-获取所有未派遣订单")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class),
            @ApiResponse(code = 503,message = "inner error!",response = Result.class)

    })
    public CompletableFuture<Result> getNoGo(){
        try {
            String requestId = UUID.randomUUID().toString(); // Create a unique ID for this request
            JSONObject requestObj = new JSONObject();
            requestObj.put("requestId", requestId);
            kafkaTemplate.send("order-server-getNoGo-request", JSON.toJSONString(requestObj));

            CompletableFuture<String> future = new CompletableFuture<>();
            futures.put(requestId, future); // Store the future so that we can complete it later
            return future.thenApply(this::parseResponseObj);
        }catch (Exception e){
            logger.error("Error while executing getNoGo()",e);
            CompletableFuture<Result> errorFuture = new CompletableFuture<>();
            errorFuture.completeExceptionally(e);
            return errorFuture;
        }

    }

    @KafkaListener(topics = {"user-server-getAllUser-response","ship-server-getAllShips-response",
        "order-server-dispatch-response","order-server-getNoGo-response"}, groupId = "admin-server-1")
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

    private <T>  Result parseResponseObj(String response) {
        JSONObject object = JSON.parseObject(response);
        object = object.getJSONObject("result");
        if (object.getBoolean("flag").equals(true)) {
            JSONObject data = object.getJSONObject("data");
            logger.info("parseResponseObj() use successful!");
            return new Result(true, "suc", 200, data);
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
