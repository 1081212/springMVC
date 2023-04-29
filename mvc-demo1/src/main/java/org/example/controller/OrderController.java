package org.example.controller;

import org.apache.ibatis.session.SqlSession;
import org.example.dao.OrderMapper;
import org.example.dao.UserMapper;
import org.example.pojo.Order;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private SqlSessionFactoryBean sqlSessionFactory;

    @PostMapping("/save")
    public String saveOrder(Model model, Order order) throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.getObject().openSession()) {
            OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
            orderMapper.insertOrder(order);
            return "redirect:/ship/RegetAll?pageNum=1&uid="+order.getUid();
        }
    }

    @RequestMapping("/getNoGo")
    public String getNoGo(Model model) throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.getObject().openSession()) {
            OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
            List<Order> orders = orderMapper.getNoGo();
            model.addAttribute("orders",orders);
            return "index";
        }
    }

    @PatchMapping("/dispatch")
    public String dispatch(Model model, @RequestParam("orderId") Integer orderId)throws Exception{
        try (SqlSession sqlSession = sqlSessionFactory.getObject().openSession()) {
            OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
            orderMapper.updateIsGo(orderId);
            return "index";
        }
    }



}
