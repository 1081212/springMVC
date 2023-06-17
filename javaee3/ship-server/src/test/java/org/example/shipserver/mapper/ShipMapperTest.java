package org.example.shipserver.mapper;

import org.example.shipserver.entity.Ship;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShipMapperTest {
    @Autowired
    private ShipMapper shipMapper;

    @Test
    void countAllShip() {
        int cou = shipMapper.countAllShip();
        assertTrue(cou>0);
    }


    @Test
    void getShipById() {
        Ship ship = shipMapper.getShipById(1);
        assertTrue(ship.getShipId().equals(1));
    }

    @Test
    void getShipsByLevel() {
        List<Ship> ships = shipMapper.getShipsByLevel("vip");
        assertTrue(ships.size()>0);
    }
}