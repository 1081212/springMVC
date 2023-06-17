package org.example.shipserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.checkerframework.checker.units.qual.A;
import org.example.shipserver.client.OrderClient;
import org.example.shipserver.entity.Result;
import org.example.shipserver.entity.Ship;
import org.example.shipserver.service.ShipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/ship-server")
public class ShipController {

    @Autowired
    private ShipService shipService;

    @Autowired
    private OrderClient orderClient;

    private static final Logger logger = LoggerFactory.getLogger(ShipController.class);


    @GetMapping("/getAllShips/{pageNum}")
    @ApiOperation(value = "微服务间接口-根据页号获取所有船只")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString")
    })
    public String getAllShips(@ApiParam(value = "页号",required = true)@PathVariable("pageNum") int pageNum) {
        try {
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
                logger.info("getAllShips() use but Don't have any ship!");
                return JSON.toJSONString(new Result(true,"Don't have any ship!",404));
            }else {
                logger.info("getAllShips() use successful!");
                return JSON.toJSONString(new Result(true,"Search ship successful!",200,pageInfo));
            }
        }catch (Exception e){
            logger.error("Error while executing getAllShips()!",e);
            return JSON.toJSONString(new Result(false,"Search error:"+e.getMessage(),422));
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
    public Result accept(@ApiParam(value = "订单id",required = true)@PathVariable("orderId") Integer orderId,@ApiParam(value = "货船id",required = true)@PathVariable("shipId") Integer shipId){
        try {
            String r = orderClient.accept(orderId,shipId);
            if (r.equals("Falback")){
                logger.info("accept() Falback!");
                return new Result(false,"Falback",503);
            }else {
                JSONObject Object = JSON.parseObject(r);
                if (Object.getBoolean("flag").equals(true)){
                    logger.info("accept() use successful!");
                    return new Result(true,"accept order ship",200);
                }else {
                    logger.info("accept() client error");
                    return new Result(false,Object.getString("msg"),Object.getInteger("statusCode"));
                }
            }
        }catch (Exception e){
            logger.error("Error while executing accept()",e);
            return new Result(false,"Search error:"+e.getMessage(),503);
        }
    }

    @PostMapping("/arrival/{orderId}")
    @ApiOperation(value = "外部接口-货船到达目的地")
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful!",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString"),
            @ApiResponse(code = 422,message = "database error",response = org.example.orderserver.entity.Result.class,responseContainer = "JSONString")
    })
    public Result arrival(@ApiParam(value = "订单id",required = true)@PathVariable("orderId") Integer orderId){
        try {
            String r = orderClient.arrival(orderId);
            if (r.equals("Falback")){
                logger.info("arrival() Falback!");
                return new Result(false,"Falback",503);
            }else {
                JSONObject Object = JSON.parseObject(r);
                if (Object.getBoolean("flag").equals(true)){
                    logger.info("arrival() use successful!");
                    return new Result(true,"arrival order ship",200);
                }else {
                    logger.info("arrival() client error");
                    return new Result(false,Object.getString("msg"),Object.getInteger("statusCode"));
                }
            }
        }catch (Exception e){
            logger.error("Error while executing arrival()",e);
            return new Result(false,"Search error:"+e.getMessage(),503);
        }
    }


}
