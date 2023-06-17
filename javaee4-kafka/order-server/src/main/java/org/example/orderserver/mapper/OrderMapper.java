package org.example.orderserver.mapper;



import org.apache.ibatis.annotations.Mapper;
import org.example.orderserver.entity.Order;

import java.util.List;


@Mapper
public interface OrderMapper {
    int insertOrder(Order order);
    List<Order> getNoGo();

    int updateIsGo(Integer orderId,int isGo);

    Order getOrderById(Integer orderId);

    int payOrder(Integer orderId);

}
