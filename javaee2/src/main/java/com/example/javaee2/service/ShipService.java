package com.example.javaee2.service;

import com.example.javaee2.dao.ShipMapper;
import com.example.javaee2.pojo.Ship;
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
}
