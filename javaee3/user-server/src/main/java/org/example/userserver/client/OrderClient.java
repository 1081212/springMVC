package org.example.userserver.client;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.example.orderserver.entity.Order;
import org.example.orderserver.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "order-server",fallback = OrderClientFalback.class)
public interface OrderClient {

    @PostMapping("/order-server/saveOrder")
    String saveOrder(@RequestBody Order order);

    @PostMapping("/order-server/payOrder/{orderId}")
    String payOrder(@PathVariable("orderId") Integer orderId);

    @PostMapping("/order-server/over/{orderId}")
    String over(@PathVariable("orderId") Integer orderId);
}
