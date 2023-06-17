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
import org.example.userserver.client.OrderClient;
import org.example.userserver.client.ShipClient;
import org.example.userserver.entity.Result;
import org.example.userserver.entity.User;
import org.example.userserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user-server")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ShipClient shipClient;

    @Autowired
    private OrderClient orderClient;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    /**
     * 获取用户
     * @return
     */


    @GetMapping("/getAllUser/{pageNum}")
    @ApiOperation(value = "微服务间接口-根据页号获得所有用户")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString")
    })
    public String getAllUser(@ApiParam(value = "页号",required = true) @PathVariable("pageNum") int pageNum) {
        try {
            int pageSize = 20; // 每页显示12条数据
            int total = userService.countAllUsers(); // 获取总记录数
            int totalPages = (int) Math.ceil((double) total / pageSize); // 计算总页数
            if (pageNum < 1) {
                pageNum = 1;
            } else if (pageNum > totalPages) {
                pageNum = totalPages;
            }
            int start = (pageNum - 1) * pageSize; // 计算起始记录的下标
            List<User> userList = userService.getUsersByPage(start, pageSize);

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

            System.out.println("pageinfo:"+pageInfo);
            if (total==0){
                logger.info("getAllUser() use but Don't have any ship!");
                return JSON.toJSONString(new Result(true,"Don't have any user!",404));
            }else {
                logger.info("getAllUser() use successful!");
                return JSON.toJSONString(new Result(true,"Search user successful!",200,pageInfo));
            }
        }catch (Exception e){
            logger.error("Error while executing getAllUser()!",e);
            return JSON.toJSONString(new Result(false,"Search error:"+e.getMessage(),422));
        }
    }

    @GetMapping("/getAllShips/{pageNum}")
    @ApiOperation(value = "外部接口-根据页号获取所有货船")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class)
    })
    public Result getAllShips(@ApiParam(value = "页号",required = true)@PathVariable("pageNum") int pageNum){
        try {
            String r = shipClient.getAllShips(pageNum);
            System.out.println("r:"+r);
            if (r.equals("Falback")){
                logger.info("getAllShips() Falback!");
                return new Result(false,"Falback",503);
            }else {
                JSONObject Object = JSON.parseObject(r);
                if (Object.getBoolean("flag").equals(true)){
                    Object = Object.getJSONObject("data");
                    JSONArray jsonArray = Object.getJSONArray("list");
                    List<Ship> ships = new ArrayList<>();

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Ship ship = jsonObject.toJavaObject(Ship.class);
                        ships.add(ship);
                    }
                    logger.info("getAllShips() use successful!");
                    return new Result(true,"get Ship User",200,ships);
                }else {
                    logger.info("getAllShips() client error");
                    return new Result(false,Object.getString("msg"),Object.getInteger("statusCode"));
                }
            }
        }catch (Exception e){
            logger.error("Error while executing getAllShips()",e);
            return new Result(false,"Search error:"+e.getMessage(),503);
        }
    }

    @PostMapping("/saveOrder")
    @ApiOperation(value = "外部接口-用户下单")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class)
    })
    public Result saveOrder(@ApiParam(value = "订单实体",type = "body",required = true)@RequestBody Order order){
        try {
            String r = orderClient.saveOrder(order);
            if (r.equals("Falback")){
                logger.info("saveOrder() Falback!");
                return new Result(false,"Falback",503);
            }else {
                JSONObject Object = JSON.parseObject(r);
                if (Object.getBoolean("flag").equals(true)){
                    logger.info("saveOrder() use successful!");
                    return new Result(true,"save order user",200);
                }else {
                    logger.info("saveOrder() client error");
                    return new Result(false,Object.getString("msg"),Object.getInteger("statusCode"));
                }
            }
        }catch (Exception e){
            logger.error("Error while executing saveOrder()",e);
            return new Result(false,"Search error:"+e.getMessage(),503);
        }
    }

    @PostMapping("/payOrder/{orderId}")
    @ApiOperation("外部接口-用户支付")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class)
    })
    public Result payOrder(@ApiParam(value = "订单id",required = true)@PathVariable("orderId") Integer orderId){
        try {
            String r = orderClient.payOrder(orderId);
            if (r.equals("Falback")){
                logger.info("payOrder() Falback!");
                return new Result(false,"Falback",503);
            }else {
                JSONObject Object = JSON.parseObject(r);
                if (Object.getBoolean("flag").equals(true)){
                    logger.info("payOrder() use successful!");
                    return new Result(true,"pay order user",200);
                }else {
                    logger.info("payOrder() client error");
                    return new Result(false,Object.getString("msg"),Object.getInteger("statusCode"));
                }
            }
        }catch (Exception e){
            logger.error("Error while executing payOrder()",e);
            return new Result(false,"Search error:"+e.getMessage(),503);
        }
    }

    @PostMapping("/over/{orderId}")
    @ApiOperation("外部接口-用户确认订单接收")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class)
    })
    public Result over(@ApiParam(value = "订单id",required = true)@PathVariable("orderId") Integer orderId){
        try {
            String r = orderClient.over(orderId);
            if (r.equals("Falback")){
                logger.info("over() Falback!");
                return new Result(false,"Falback",503);
            }else {
                JSONObject Object = JSON.parseObject(r);
                if (Object.getBoolean("flag").equals(true)){
                    logger.info("over() use successful!");
                    return new Result(true,"over order user",200);
                }else {
                    logger.info("over() client error");
                    return new Result(false,Object.getString("msg"),Object.getInteger("statusCode"));
                }
            }
        }catch (Exception e){
            logger.error("Error while executing over()",e);
            return new Result(false,"Search error:"+e.getMessage(),503);
        }
    }

}
