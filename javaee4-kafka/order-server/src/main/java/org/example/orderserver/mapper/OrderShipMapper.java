package org.example.orderserver.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderShipMapper {
    int insertOne(Integer orderId,Integer shipId);

    int acceptOrder(Integer orderId,Integer shipId);
}
