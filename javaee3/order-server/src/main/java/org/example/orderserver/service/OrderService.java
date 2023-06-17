package org.example.orderserver.service;


import org.example.orderserver.entity.Order;
import org.example.orderserver.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements OrderMapper {
    @Autowired
    private OrderMapper orderMapper;


    @Override
    public int insertOrder(Order order) {
        return orderMapper.insertOrder(order);
    }

    @Override
    public List<Order> getNoGo() {

        return orderMapper.getNoGo();
    }

    @Override
    public int payOrder(Integer orderId) {
        return orderMapper.payOrder(orderId);
    }

    @Override
    public int updateIsGo(Integer orderId,int isGo) {
        return orderMapper.updateIsGo(orderId,isGo);
    }

    @Override
    public Order getOrderById(Integer orderId) {
        return orderMapper.getOrderById(orderId);
    }
}
