package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.example.pojo.Ship;

import java.util.List;

public interface ShipMapper {
        List<Ship> getAllShip();
        int countAllShip();
        List<Ship> getShipsByPage(@Param("start") int start, @Param("pageSize") int pageSize);

}
