package org.example.userserver.client;

import org.example.orderserver.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderClientFalback implements OrderClient{

    @Override
    public String saveOrder(Order order) {
        return "Falback";
    }

    @Override
    public String over(Integer orderId) {
        return "Falback";
    }

    @Override
    public String payOrder(Integer orderId) {
        return "Falback";
    }
}
