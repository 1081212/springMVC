package com.example.javaee2.dao;

import com.example.javaee2.pojo.Ship;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface ShipMapper {
        List<Ship> getAllShip();

        int countAllShip();

        List<Ship> getShipsByPage( int start, int pageSize);

        Ship getShipById(Integer id);

}
