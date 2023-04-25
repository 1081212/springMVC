package org.example.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.SqlSession;
import org.example.dao.ShipMapper;
import org.example.dao.UserMapper;
import org.example.pojo.Ship;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/ship")
public class ShipController {

    @Autowired
    private SqlSessionFactoryBean sqlSessionFactory;



    @RequestMapping("/getAll")
    public String getAllShips(Model model, @RequestParam(defaultValue = "1") int pageNum) throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.getObject().openSession()) {
            ShipMapper shipMapper = sqlSession.getMapper(ShipMapper.class);
            int pageSize = 12; // 每页显示12条数据
            int total = shipMapper.countAllShip(); // 获取总记录数
            int totalPages = (int) Math.ceil((double) total / pageSize); // 计算总页数
            if (pageNum < 1) {
                pageNum = 1;
            } else if (pageNum > totalPages) {
                pageNum = totalPages;
            }
            int start = (pageNum - 1) * pageSize; // 计算起始记录的下标
            List<Ship> shipList = shipMapper.getShipsByPage(start, pageSize);

            PageInfo<Ship> pageInfo = new PageInfo<>(shipList);
            pageInfo.setPageNum(pageNum);
            pageInfo.setTotal(total);
            pageInfo.setPages(totalPages);
            if (pageNum!=totalPages){
                pageInfo.setHasNextPage(true);
                pageInfo.setNextPage(pageNum+1);
            }
            if (pageNum!=1){
                pageInfo.setHasPreviousPage(true);
                pageInfo.setPrePage(pageNum-1);
            }

            model.addAttribute("page", pageInfo); // 把分页信息传递给前端
            System.out.println(pageInfo);

            return "trans";
        }
    }

    @RequestMapping("/details")
    public String details(Model model, @RequestParam(defaultValue = "1") Integer id){
        return "index";
    }



}
