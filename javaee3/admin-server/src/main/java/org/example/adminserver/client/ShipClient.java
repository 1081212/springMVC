package org.example.adminserver.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "ship-server",fallback = ShipClientFalback.class)
public interface ShipClient {

    @GetMapping("/ship-server/getAllShips/{pageNum}")
    String getAllShips(@PathVariable("pageNum") int pageNum);

}
