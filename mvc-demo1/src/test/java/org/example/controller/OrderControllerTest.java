package org.example.controller;

import cn.hutool.json.JSONUtil;
import org.apache.ibatis.session.SqlSession;
import org.example.dao.OrderMapper;
import org.example.pojo.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/springMVC.xml")
public class OrderControllerTest {

    @Autowired
    private OrderController orderController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void saveOrder() throws Exception {
        // 创建一个假的订单对象
        Order order = new Order();
        order.setShipId(2);
        order.setUid(5);
        order.setStartProvinceAdcode(110000);
        order.setStartCityAdcode(110100);
        order.setEndProvinceAdcode(220000);
        order.setEndCityAdcode(220500);
        order.setWeight(1.2);
        order.setTotalPrice(1080.0);
        order.setTrueName("Junit Test Controller");
        order.setTruePhone("88888888");
        String a = JSONUtil.toJsonStr(order);;
        System.out.println("a:"+a);



        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/order/save")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(a))
                        .andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());

    }

    @Test
    public void getNoGo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/order/getNoGo")).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    public void dispatch() throws Exception {
        int orderId = 1;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/order/dispatch?orderId="+orderId)).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }
}