package com.example.javaee2.dao;



import com.example.javaee2.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface OrderMapper {
    int insertOrder(Order order);
    List<Order> getNoGo();

    int updateIsGo(Integer orderId);
}
