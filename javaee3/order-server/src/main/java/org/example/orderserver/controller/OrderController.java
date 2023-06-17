package org.example.orderserver.controller;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.example.orderserver.entity.Order;
import org.example.orderserver.entity.Result;
import org.example.orderserver.service.OrderService;
import org.example.orderserver.service.OrderShipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-server")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderShipService orderShipService;

    private final int START = 0;
    private final int DISPATCH = 1;
    private final int ACCEPT = 2;
    private final int ARRIVAL = 3;
    private final int OVER = 4;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    @PostMapping("/saveOrder")
    @ApiOperation(value = "微服务间接口-保存订单,需传递订单实体")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = Result.class,responseContainer = "JSONString")
    })
    public String saveOrder(@ApiParam(value = "订单实体",type = "body",required = true)@RequestBody Order order) {
        try {
            int a = orderService.insertOrder(order);
            if (a!=0){
                logger.info("saveOrder() use successful!");
                return JSON.toJSONString(new Result(true,"save order successful!",200));
            }else {
                logger.info("saveOrder() use but insert error!");
                return JSON.toJSONString(new Result(false,"save order error",422));
            }
        }catch (Exception e) {
            logger.error("Error while executing saveOrder()", e);
            return JSON.toJSONString(new Result(false, "Research error: " + e.getMessage(), 422));
        }
    }

    @PostMapping("/payOrder/{orderId}")
    @ApiOperation(value = "微服务间接口-支付订单,需传递订单id")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = Result.class,responseContainer = "JSONString")
    })
    public String payOrder(@ApiParam(value = "订单id",required = true)@PathVariable("orderId") Integer orderId){
        try {
            int payNum = orderService.payOrder(orderId);
            if (payNum == 0 ){
                logger.info("payOrder() use but do nothing");
                return JSON.toJSONString(new Result(true,"pay False!",404));
            }else {
                logger.info("payOrder() use successful!");
                return JSON.toJSONString(new Result(true,"pay successful!",200));
            }
        }catch (Exception e){
            logger.error("Error while executing payOrder()",e);
            return JSON.toJSONString(new Result(false,"Dispatch error:"+e.getMessage(),422));
        }
    }

    @GetMapping("/getNoGo")
    @ApiOperation(value = "微服务间接口-获取已支付但未被派遣的订单")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = Result.class,responseContainer = "JSONString")
    })
    public String getNoGo() {
        try {
            List<Order> orders = orderService.getNoGo();
            if (orders.isEmpty()) {
                logger.info("getNoGo() use and all order was go");
                return JSON.toJSONString(new Result(true, "Don't have any ship!", 200));
            } else {
                logger.info("getNoGo() use successful!");
                return JSON.toJSONString(new Result(true, "Research successful!", 200, orders));
            }
        } catch (Exception e) {
            logger.error("Error while executing getNoGo()", e);
            return JSON.toJSONString(new Result(false, "Research error: " + e.getMessage(), 422));
        }
    }


    @PostMapping("/dispatch/{orderId}/{shipId}")
    @ApiOperation(value = "微服务间接口-将指定订单分给指定的船只")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = Result.class,responseContainer = "JSONString")
    })
    public String dispatch(@ApiParam(value = "订单id",required = true)@PathVariable("orderId") Integer orderId,@ApiParam(value = "货船id",required = true)@PathVariable("shipId") Integer shipId){
        try {
            int dispatchNum = orderService.updateIsGo(orderId,DISPATCH);
            int insertNum = orderShipService.insertOne(orderId,shipId);
            if (dispatchNum == 0 || insertNum == 0){
                logger.info("dispatch() use but do nothing");
                return JSON.toJSONString(new Result(true,"Order already dispatch OR not exist!",404));
            }else {
                logger.info("dispatch() use successful!");
                return JSON.toJSONString(new Result(true,"Dispatch successful!",200));
            }
        }catch (Exception e){
            logger.error("Error while executing Dispatch()",e);
            return JSON.toJSONString(new Result(false,"Dispatch error:"+e.getMessage(),422));
        }
    }

    @PostMapping("/arrival/{orderId}")
    @ApiOperation(value = "微服务间接口-船只到达目的地，需传递订单号")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = Result.class,responseContainer = "JSONString")
    })
    public String arrival(@ApiParam(value = "订单id",required = true)@PathVariable("orderId") Integer orderId){
        try {
            int arrivalNum = orderService.updateIsGo(orderId,ARRIVAL);
            if (arrivalNum == 0 ){
                logger.info("arrival() use but do nothing");
                return JSON.toJSONString(new Result(true,"Order already arrival OR not exist!",404));
            }else {
                logger.info("arrival() use successful!");
                return JSON.toJSONString(new Result(true,"arrival successful!",200));
            }
        }catch (Exception e){
            logger.error("Error while executing arrival()",e);
            return JSON.toJSONString(new Result(false,"arrival error:"+e.getMessage(),422));
        }
    }

    @PostMapping("/accept/{orderId}/{shipId}")
    @ApiOperation(value = "微服务间接口-支付订单,需传递订单id")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = Result.class,responseContainer = "JSONString")
    })
    public String accept(@PathVariable("orderId") Integer orderId,@PathVariable("shipId") Integer shipId){
        try {
            int acceptNum = orderService.updateIsGo(orderId,ACCEPT);
            int OSNum = orderShipService.acceptOrder(orderId,shipId);
            if (acceptNum == 0 || OSNum == 0){
                logger.info("arrival() use but do nothing");
                return JSON.toJSONString(new Result(true,"Order already arrival OR not exist!",404));
            }else {
                logger.info("arrival() use successful!");
                return JSON.toJSONString(new Result(true,"arrival successful!",200));
            }
        }catch (Exception e){
            logger.error("Error while executing arrival()",e);
            return JSON.toJSONString(new Result(false,"arrival error:"+e.getMessage(),422));
        }
    }

    @PostMapping("/over/{orderId}")
    @ApiOperation(value = "微服务间接口-用户确认，结束订单")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = Result.class,responseContainer = "JSONString")
    })
    public String over(@ApiParam(value = "订单id",required = true)@PathVariable("orderId") Integer orderId){
        try {
            int overNum = orderService.updateIsGo(orderId,OVER);
            if (overNum == 0 ){
                logger.info("over() use but do nothing");
                return JSON.toJSONString(new Result(true,"Order already over OR not exist!",404));
            }else {
                logger.info("over() use successful!");
                return JSON.toJSONString(new Result(true,"over successful!",200));
            }
        }catch (Exception e){
            logger.error("Error while executing over()",e);
            return JSON.toJSONString(new Result(false,"over error:"+e.getMessage(),422));
        }
    }



}
