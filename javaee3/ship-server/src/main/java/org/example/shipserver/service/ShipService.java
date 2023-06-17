package org.example.shipserver.service;

import org.example.shipserver.mapper.ShipMapper;
import org.example.shipserver.entity.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipService implements ShipMapper {

    @Autowired
    private ShipMapper shipMapper;

    @Override
    public List<Ship> getAllShip() {
        return shipMapper.getAllShip();
    }

    @Override
    public int countAllShip() {
        return shipMapper.countAllShip();
    }

    @Override
    public List<Ship> getShipsByPage(int start, int pageSize) {
        return shipMapper.getShipsByPage(start,pageSize);
    }

    @Override
    public Ship getShipById(Integer id) {
        return shipMapper.getShipById(id);
    }

    @Override
    public List<Ship> getShipsByLevel(String level) {
        return shipMapper.getShipsByLevel(level);
    }
}
