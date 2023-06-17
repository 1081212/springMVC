package org.example.shipserver.mapper;

import org.example.shipserver.entity.Ship;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShipMapper {
        List<Ship> getAllShip();

        int countAllShip();

        List<Ship> getShipsByPage( int start, int pageSize);

        Ship getShipById(Integer id);

        List<Ship> getShipsByLevel(String level);

}
