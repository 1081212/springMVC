package org.example.adminserver.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import org.example.adminserver.client.OrderClient;
import org.example.adminserver.client.ShipClient;
import org.example.adminserver.client.UserClient;
import org.example.orderserver.entity.Result;
import org.example.shipserver.entity.Ship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.service.ApiDescription;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin-server")
public class AdminController {

    @Autowired
    private UserClient userClient;

    @Autowired
    private ShipClient shipClient;

    @Autowired
    private OrderClient orderClient;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/getAllUser/{pageNum}")
    @ApiOperation(value = "外部接口-获取所有用户")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class),
            @ApiResponse(code = 503,message = "inner error!",response = Result.class)

    })
    public Result getAllUser(@ApiParam(value = "页号",required = true)@PathVariable("pageNum") int pageNum) {
        try {
            String r = userClient.getAllUser(pageNum);
            System.out.println("r:"+r);
            if (r.equals("Falback")){
                logger.info("getAllUser() Falback!");
                return new Result(false,"Falback",503);
            }else {
                JSONObject Object = JSON.parseObject(r);
                if (Object.getBoolean("flag").equals(true)){
                    JSONArray jsonArray = Object.getJSONArray("data");
                    List<Ship> ships = new ArrayList<>();

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Ship ship = jsonObject.toJavaObject(Ship.class);
                        ships.add(ship);
                    }
                    logger.info("getAllUser() use successful!");
                    return new Result(true,"get Useer Admin",200,ships);
                }else {
                    logger.info("getAllUser() client error");
                    return new Result(false,Object.getString("msg"),Object.getInteger("statusCode"));
                }
            }
        }catch (Exception e){
            logger.error("Error while executing getAllUser()",e);
            return new Result(false,"Search error:"+e.getMessage(),503);
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
    public Result getAllShips(@ApiParam(value = "页号",required = true)@PathVariable("pageNum") int pageNum) {
        try {
            String r = shipClient.getAllShips(pageNum);
            System.out.println("r:"+r);
            if (r.equals("Falback")){
                logger.info("getAllShips() Falback!");
                return new Result(false,"Falback",503);
            }else {
                JSONObject Object = JSON.parseObject(r);
                if (Object.getBoolean("flag").equals(true)){
                    JSONArray jsonArray = Object.getJSONArray("data");
                    List<Ship> ships = new ArrayList<>();

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Ship ship = jsonObject.toJavaObject(Ship.class);
                        ships.add(ship);
                    }
                    logger.info("getAllShips() use successful!");
                    return new Result(true,"get Ship Admin",200,ships);
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

    @PostMapping("/dispatch/{orderId}/{shipId}")
    @ApiOperation(value = "外部接口-获取所有船只")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class),
            @ApiResponse(code = 503,message = "inner error!",response = Result.class)

    })
    public Result dispatch(@PathVariable("orderId") Integer orderId,@PathVariable("shipId") Integer shipId){
        try {
            String r = orderClient.dispatch(orderId,shipId);
            if (r.equals("Falback")){
                logger.info("dispatch() Falback!");
                return new Result(false,"Falback",503);
            }else {
                JSONObject Object = JSON.parseObject(r);
                if (Object.getBoolean("flag").equals(true)){
                    logger.info("dispatch() use successful!");
                    return new Result(true,"dispatch order admin",200);
                }else {
                    logger.info("dispatch() client error");
                    return new Result(false,Object.getString("msg"),Object.getInteger("statusCode"));
                }
            }
        }catch (Exception e){
            logger.error("Error while executing dispatch()",e);
            return new Result(false,"Search error:"+e.getMessage(),503);
        }
    }

    @GetMapping("/getNoGo")
    @ApiOperation(value = "外部接口-获取所有未派遣订单")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = Result.class),
            @ApiResponse(code = 503,message = "inner error!",response = Result.class)

    })
    public Result getNoGo(){
        try {
            String r = orderClient.getNoGo();
            if (r.equals("Falback")){
                logger.info("getNoGo() Falback!");
                return new Result(false,"Falback",503);
            }else {
                JSONObject Object = JSON.parseObject(r);
                if (Object.getBoolean("flag").equals(true)){
                    logger.info("getNoGo() use successful!");
                    return new Result(true,"dispatch order admin",200,Object);
                }else {
                    logger.info("getNoGo() client error");
                    return new Result(false,Object.getString("msg"),Object.getInteger("statusCode"));
                }
            }
        }catch (Exception e){
            logger.error("Error while executing getNoGo()",e);
            return new Result(false,"Search error:"+e.getMessage(),503);
        }

    }


}
