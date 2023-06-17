package org.example.userserver.client;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name = "ship-server",fallback = ShipClientFalback.class)
public interface ShipClient {

    @GetMapping("/ship-server/getAllShips/{pageNum}")
    String getAllShips(@PathVariable("pageNum") int pageNum);

}
