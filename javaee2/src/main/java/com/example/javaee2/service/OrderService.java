package com.example.javaee2.service;

import com.example.javaee2.dao.OrderMapper;
import com.example.javaee2.pojo.Order;
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
    public int updateIsGo(Integer orderId) {
        return orderMapper.updateIsGo(orderId);
    }
}
