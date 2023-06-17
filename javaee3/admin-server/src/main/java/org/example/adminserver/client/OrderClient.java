package org.example.adminserver.client;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.example.orderserver.entity.Order;
import org.example.orderserver.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "order-server",fallback = OrderClientFalback.class)
public interface OrderClient {


    @PostMapping("/order-server/dispatch/{orderId}/{shipId}")
    String dispatch(@PathVariable("orderId") Integer orderId,@PathVariable("shipId") Integer shipId);

    @GetMapping("/order-server/getNoGo")
    String getNoGo() ;
}
