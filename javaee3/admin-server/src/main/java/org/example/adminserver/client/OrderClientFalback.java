package org.example.adminserver.client;

import org.example.orderserver.entity.Order;
import org.springframework.stereotype.Component;


@Component
public class OrderClientFalback implements OrderClient {

    @Override
    public String dispatch(Integer orderId, Integer shipId) {
        return "Falback";
    }

    @Override
    public String getNoGo() {
        return "Falback";
    }
}
