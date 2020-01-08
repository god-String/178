package com.baizhi.zjy.interceptor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inter")
public class Controller1 {
    @RequestMapping("/test1")
    public String test1(){
        System.out.println("in inter. test1.....");
        return "index";
    }
}
