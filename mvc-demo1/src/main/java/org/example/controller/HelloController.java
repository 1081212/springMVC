package org.example.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @RequestMapping("/")
    public String index(){
        //返回视图名称
        return "login";
    }



    @RequestMapping("/target")
    public String toTarget(){
        return "target";
    }

    @RequestMapping("/toRegister")
    public String toRegister(){return "register";}

    @RequestMapping("/index")
    public String toIndex(){return "index";}
}
