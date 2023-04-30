package org.example.dao;

import org.example.pojo.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/springMVC.xml")
public class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void insertOrder() {
        Order order = new Order();
        order.setShipId(2);
        order.setUid(5);
        order.setStartCityAdcode(110000);
        order.setStartCityAdcode(110100);
        order.setEndProvinceAdcode(220000);
        order.setEndCityAdcode(220500);
        order.setWeight(1.2);
        order.setTotalPrice(1080.0);
        order.setTrueName("Junit Test");
        order.setTruePhone("88888888");
        orderMapper.insertOrder(order);
    }

    @Test
    public void getNoGo() {
        List<Order> orderList = orderMapper.getNoGo();
        assertNotNull(orderList);
        assertTrue(orderList.size()>0);
    }

    @Test
    public void updateIsGo() {
        orderMapper.updateIsGo(1);
    }
}