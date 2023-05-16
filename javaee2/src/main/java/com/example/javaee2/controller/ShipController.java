package com.example.javaee2.controller;


import com.example.javaee2.pojo.Result;
import com.example.javaee2.pojo.Ship;
import com.example.javaee2.pojo.User;
import com.example.javaee2.service.ShipService;
import com.example.javaee2.service.UserService;
import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ship")
public class ShipController {

    @Autowired
    private ShipService shipService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(ShipController.class);

    @GetMapping("/hello")
    public String hello(){
        return "Hello Ship!";
    }


    @GetMapping("/getAll/{pageNum}/{uid}")
    public Result getAllShips(@PathVariable("pageNum") int pageNum, @PathVariable("uid") Integer uid) throws Exception {
        try {
            User user = userService.getUserById(uid);

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

            System.out.println("pageinfo:"+pageInfo);
            if (total==0){
                return new Result(true,"Don't have any ship!",404);
            }else {
                return new Result(true,"Search ship successful!",200,pageInfo);
            }
        }catch (Exception e){
            logger.error("Error while executing getAllShips()",e);
            return new Result(false,"Search error:"+e.getMessage(),422);
        }

    }

    @GetMapping("/detail/{shipId}/{uid}")
    public Result detail( @PathVariable("shipId") Integer shipId,@PathVariable("uid") Integer uid) throws Exception {
        try {
            Ship ship = shipService.getShipById(shipId);
            System.out.println("details user:" + uid);
            System.out.println(ship);
            if (ship!=null){
                return new Result(true,"Search successful!",200,ship);
            }else {
                return new Result(true,"Not find any ship!",404);
            }
        }catch (Exception e){
            logger.error("Error while executing detail()",e);
            return new Result(false,"Search error:"+e.getMessage(),422);
        }

    }

}
