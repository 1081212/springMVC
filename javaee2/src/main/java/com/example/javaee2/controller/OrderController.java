package com.example.javaee2.controller;

import com.example.javaee2.pojo.Order;
import com.example.javaee2.pojo.Result;
import com.example.javaee2.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    @GetMapping("/hello")
    public String hello(){
        return "Hello World!";
    }

    @PostMapping("/save")
    public Result saveOrder(@RequestBody Order order) throws Exception {
        int a = orderService.insertOrder(order);
        if (a!=0){
            return new Result(true,"save order successful!",200);
        }else {
            return new Result(false,"save order error",422);
        }

    }

    @GetMapping("/getNoGo")
    public Result getNoGo() {
        try {
            List<Order> orders = orderService.getNoGo();
            if (orders.isEmpty()) {
                return new Result(true, "Don't have any ship!", 200);
            } else {
                return new Result(true, "Research successful!", 200, orders);
            }
        } catch (Exception e) {
            logger.error("Error while executing getNoGo()", e);
            return new Result(false, "Research error: " + e.getMessage(), 422);
        }
    }


    @PatchMapping("/dispatch/{orderId}")
    public Result dispatch(@PathVariable("orderId") Integer orderId)throws Exception{
        try {
            int dispatchNum = orderService.updateIsGo(orderId);
            if (dispatchNum == 0){
                return new Result(true,"Order already dispatch OR not exist!",404);
            }else {
                return new Result(true,"Dispatch successful!",200);
            }
        }catch (Exception e){
            logger.error("Error while executing Dispatch()",e);
            return new Result(false,"Dispatch error:"+e.getMessage(),422);
        }
    }



}
