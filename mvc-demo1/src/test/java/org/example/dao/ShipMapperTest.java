package org.example.dao;

import org.example.pojo.Ship;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/springMVC.xml")
public class ShipMapperTest {

    @Autowired
    private ShipMapper shipMapper;

    @Test
    public void getAllShip() {
        List<Ship> shipList = shipMapper.getAllShip();
        assertNotNull(shipList);
        assertTrue(shipList.size()>0);
    }

    @Test
    public void countAllShip() {
        int shipNum = shipMapper.countAllShip();
        assertTrue(shipNum>0);
        assertEquals((int)2L,shipNum);
    }

    @Test
    public void getShipsByPage() {
        List<Ship> shipList = shipMapper.getShipsByPage(1,12);
        assertTrue(shipList.size()>0);
    }

    @Test
    public void getShipById() {
        Ship ship = shipMapper.getShipById(1);
        assertNotNull(ship);
        assertEquals("王大船",ship.getName());
    }
}