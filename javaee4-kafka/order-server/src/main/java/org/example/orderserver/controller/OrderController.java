package org.example.orderserver.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.aspectj.weaver.ast.Or;
import org.example.orderserver.entity.Order;
import org.example.orderserver.entity.Result;
import org.example.orderserver.service.OrderService;
import org.example.orderserver.service.OrderShipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-server")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderShipService orderShipService;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;


    private final int START = 0;
    private final int DISPATCH = 1;
    private final int ACCEPT = 2;
    private final int ARRIVAL = 3;
    private final int OVER = 4;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    @KafkaListener(topics = "order-server-saveOrder-request",groupId = "order-server-1")
    public void saveOrder(String request) {
        String topic = "order-server-saveOrder-response";
        JSONObject responseObj = new JSONObject();
        JSONObject requestObj = JSON.parseObject(request);
        String requestId = requestObj.getString("requestId");
        try {
            Order order = requestObj.getObject("order",Order.class);
            int payNum = orderService.insertOrder(order);
            responseObj.put("requestId", requestId); // 将请求ID放入响应中
            if (payNum == 0 ){
                logger.info("saveOrder() use but do nothing");
                responseObj.put("result",JSON.toJSONString(new Result(true,"False!",404)));
                kafkaTemplate.send(topic,JSON.toJSONString(responseObj));

            }else {
                logger.info("saveOrder() use successful!");
                responseObj.put("result",JSON.toJSONString(new Result(true,"successful!",200)));
                kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
            }
        }catch (Exception e){
            logger.error("Error while executing saveOrder()",e);
            responseObj.put("requestId", requestId); // 将请求ID放入响应中
            responseObj.put("result",JSON.toJSONString(new Result(false,"error:"+e.getMessage(),422)));
            kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
        }
    }

    @KafkaListener(topics = "order-server-payOrder-request",groupId = "order-server-1")
    public void payOrder(String request){
        try {
            JSONObject requestObj = JSON.parseObject(request);
            String requestId = requestObj.getString("requestId");
            Integer orderId = requestObj.getInteger("orderId");
            int payNum = orderService.payOrder(orderId);
            JSONObject responseObj = new JSONObject();
            responseObj.put("requestId", requestId); // 将请求ID放入响应中

            if (payNum == 0 ){
                logger.info("payOrder() use but do nothing");
                responseObj.put("result",JSON.toJSONString(new Result(true,"pay False!",404)));
                kafkaTemplate.send("order-server-payOrder-response",JSON.toJSONString(responseObj));

            }else {
                logger.info("payOrder() use successful!");
                responseObj.put("result",JSON.toJSONString(new Result(true,"pay successful!",200)));
                kafkaTemplate.send("order-server-payOrder-response",JSON.toJSONString(responseObj));
            }
        }catch (Exception e){
            logger.error("Error while executing payOrder()",e);
            JSONObject responseObj = new JSONObject();
            JSONObject requestObj = JSON.parseObject(request);
            String requestId = requestObj.getString("requestId"); // 获取请求ID

            responseObj.put("requestId", requestId); // 将请求ID放入响应中
            responseObj.put("result",JSON.toJSONString(new Result(false,"Dispatch error:"+e.getMessage(),422)));
            kafkaTemplate.send("order-server-payOrder-response",JSON.toJSONString(responseObj));
        }
    }

    @KafkaListener(topics = "order-server-getNoGo-request",groupId = "order-server-1")
    public void getNoGo(String request) {
        String topic = "order-server-getNoGo-response";
        JSONObject responseObj = new JSONObject();
        JSONObject requestObj = JSON.parseObject(request);
        String requestId = requestObj.getString("requestId");
        try {
            List<Order> orders = orderService.getNoGo();
            responseObj.put("requestId", requestId); // 将请求ID放入响应中

            logger.info("dispatch() use but do nothing");
            responseObj.put("result",JSON.toJSONString(new Result(true,"suc!",200,orders)));
            kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
        }catch (Exception e){
            logger.error("Error while executing dispatch()",e);
            responseObj.put("requestId", requestId); // 将请求ID放入响应中
            responseObj.put("result",JSON.toJSONString(new Result(false,"dispatch error:"+e.getMessage(),422)));
            kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
        }
    }


    @KafkaListener(topics = "order-server-dispatch-request",groupId = "order-server-1")
    public void dispatch(String request){
        String topic = "order-server-dispatch-response";
        JSONObject responseObj = new JSONObject();
        JSONObject requestObj = JSON.parseObject(request);
        String requestId = requestObj.getString("requestId");
        try {

            Integer orderId = requestObj.getInteger("orderId");
            int payNum = orderService.updateIsGo(orderId,DISPATCH);
            responseObj.put("requestId", requestId); // 将请求ID放入响应中

            if (payNum == 0 ){
                logger.info("dispatch() use but do nothing");
                responseObj.put("result",JSON.toJSONString(new Result(true,"dispatch False!",404)));
                kafkaTemplate.send(topic,JSON.toJSONString(responseObj));

            }else {
                logger.info("dispatch() use successful!");
                responseObj.put("result",JSON.toJSONString(new Result(true,"dispatch successful!",200)));
                kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
            }
        }catch (Exception e){
            logger.error("Error while executing dispatch()",e);
            responseObj.put("requestId", requestId); // 将请求ID放入响应中
            responseObj.put("result",JSON.toJSONString(new Result(false,"dispatch error:"+e.getMessage(),422)));
            kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
        }
    }

    @KafkaListener(topics = "order-server-arrival-request",groupId = "order-server-1")
    public void arrival(String request){
        String topic = "order-server-arrival-response";
        JSONObject responseObj = new JSONObject();
        JSONObject requestObj = JSON.parseObject(request);
        String requestId = requestObj.getString("requestId");
        try {

            Integer orderId = requestObj.getInteger("orderId");
            int payNum = orderService.updateIsGo(orderId,ARRIVAL);
            responseObj.put("requestId", requestId); // 将请求ID放入响应中

            if (payNum == 0 ){
                logger.info("arrival() use but do nothing");
                responseObj.put("result",JSON.toJSONString(new Result(true,"arrival False!",404)));
                kafkaTemplate.send(topic,JSON.toJSONString(responseObj));

            }else {
                logger.info("arrival() use successful!");
                responseObj.put("result",JSON.toJSONString(new Result(true,"arrival successful!",200)));
                kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
            }
        }catch (Exception e){
            logger.error("Error while executing arrival()",e);
            responseObj.put("requestId", requestId); // 将请求ID放入响应中
            responseObj.put("result",JSON.toJSONString(new Result(false,"arrival error:"+e.getMessage(),422)));
            kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
        }
    }

    @KafkaListener(topics = "order-server-accept-request",groupId = "order-server-1")
    public void accept(String request){
        String topic = "order-server-accept-response";
        JSONObject responseObj = new JSONObject();
        JSONObject requestObj = JSON.parseObject(request);
        String requestId = requestObj.getString("requestId");
        try {

            Integer orderId = requestObj.getInteger("orderId");
            int payNum = orderService.updateIsGo(orderId,ACCEPT);
            responseObj.put("requestId", requestId); // 将请求ID放入响应中

            if (payNum == 0 ){
                logger.info("accept() use but do nothing");
                responseObj.put("result",JSON.toJSONString(new Result(true,"pay False!",404)));
                kafkaTemplate.send(topic,JSON.toJSONString(responseObj));

            }else {
                logger.info("accept() use successful!");
                responseObj.put("result",JSON.toJSONString(new Result(true,"pay successful!",200)));
                kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
            }
        }catch (Exception e){
            logger.error("Error while executing over()",e);
            responseObj.put("requestId", requestId); // 将请求ID放入响应中
            responseObj.put("result",JSON.toJSONString(new Result(false,"Dispatch error:"+e.getMessage(),422)));
            kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
        }
    }

    @KafkaListener(topics = "order-server-over-request",groupId = "order-server-1")
    public void over(String request){
        String topic = "order-server-over-response";
        JSONObject responseObj = new JSONObject();
        JSONObject requestObj = JSON.parseObject(request);
        String requestId = requestObj.getString("requestId");
        try {

            Integer orderId = requestObj.getInteger("orderId");
            int payNum = orderService.updateIsGo(orderId,OVER);
            responseObj.put("requestId", requestId); // 将请求ID放入响应中

            if (payNum == 0 ){
                logger.info("over() use but do nothing");
                responseObj.put("result",JSON.toJSONString(new Result(true,"pay False!",404)));
                kafkaTemplate.send(topic,JSON.toJSONString(responseObj));

            }else {
                logger.info("over() use successful!");
                responseObj.put("result",JSON.toJSONString(new Result(true,"pay successful!",200)));
                kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
            }
        }catch (Exception e){
            logger.error("Error while executing over()",e);
            responseObj.put("requestId", requestId); // 将请求ID放入响应中
            responseObj.put("result",JSON.toJSONString(new Result(false,"Dispatch error:"+e.getMessage(),422)));
            kafkaTemplate.send(topic,JSON.toJSONString(responseObj));
        }
    }



}
