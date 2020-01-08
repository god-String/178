package com.baizhi.zjy.controller;

import com.baizhi.zjy.dao.AdminDao;
import com.baizhi.zjy.entity.Admin;

import com.baizhi.zjy.until.CreateValidateCode;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Controller
@RequestMapping("/admin")
@RestController
public class AdminController {
    @Autowired
    AdminDao adminDao;
    @RequestMapping("login")
    public Map login(String username,String password,String clientCode,HttpSession session){
        // 1. 创建需要返回的Map集合
        HashMap hashMap = new HashMap();
        // 2. 构建查询条件
        Admin admin = new Admin();
        admin.setUsername(username);
        // 3. 调用Dao查询
        Admin adminDB = adminDao.selectOne(admin);
        // 4. 判断情况封装错误信息
        session.setAttribute("admin",adminDB);
        String serverCode = session.getAttribute("ServerCode").toString();
        if (clientCode.equals(serverCode)) {
            if (adminDB == null) {
                hashMap.put("status", 400);
                hashMap.put("msg", "该用户不存在");
            } else if (!adminDB.getPassword().equals(password)) {
                hashMap.put("status", 400);
                hashMap.put("msg", "密码错误");
            } else {
                hashMap.put("status", 200);
            }
        }else {
            hashMap.put("msg","验证码错误");
        }
            // 5. 返回集合
            return hashMap;
    }

    @RequestMapping("createImg")
    public void createImg(HttpServletResponse response, HttpSession session) throws IOException {
        CreateValidateCode vcode = new CreateValidateCode();
        String code = vcode.getCode(); // 随机验证码
        vcode.write(response.getOutputStream()); // 把图片输出client
        // 把生成的验证码 存入session
        session.setAttribute("ServerCode", code);
    }

}
