package org.example.shipserver.client;

import org.example.orderserver.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderClientFalback implements OrderClient {

    @Override
    public String arrival(Integer orderId) {
        return "Falback";
    }

    @Override
    public String accept(Integer orderId, Integer shipId) {
        return "Falback";
    }
}
