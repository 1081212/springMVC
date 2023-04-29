package org.example.dao;

import org.example.pojo.Order;

import java.util.List;

public interface OrderMapper {
    void insertOrder(Order order);
    List<Order> getNoGo();

    void updateIsGo(Integer orderId);
}
