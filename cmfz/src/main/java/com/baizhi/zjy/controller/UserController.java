package com.baizhi.zjy.controller;


import com.alibaba.druid.support.json.JSONUtils;
import com.baizhi.zjy.DTO.MapDto;
import com.baizhi.zjy.dao.UserDao;
import com.baizhi.zjy.entity.User;
import com.baizhi.zjy.until.SmsUtil;
import io.goeasy.GoEasy;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserDao userDao;

    @RequestMapping("showAllUsers")
    public Map showAllUsers(Integer page,Integer rows){
        HashMap hashMap = new HashMap();
        int i = userDao.selectCount(null);
        Integer total=i%rows==0?i/rows:i/rows+1;
        List<User> use = userDao.selectByRowBounds(null, new RowBounds((page - 1) * rows, rows));
        hashMap.put("records",i);
        hashMap.put("page",page);
        hashMap.put("total",total);
        hashMap.put("rows",use);
        return hashMap;
    }

    @RequestMapping("edituser")
    public Map edituser(String oper, User user, String[] id){
        HashMap hashMap = new HashMap();
        // 添加逻辑
        if (oper.equals("add")){
            String userId = UUID.randomUUID().toString();
            user.setId(userId);
            user.setRigest_date(new Date());
            userDao.insert(user);
            GoEasy goEasy = new GoEasy( "http://rest-hangzhou.goeasy.io", "BC-ae0f38db150e43b5af37a74c37c4cbe1");
            Map map = showUserTime();
            String s = JSONUtils.toJSONString(map);
            System.out.println(s);
            goEasy.publish("cmfz", s);
            hashMap.put("userId",userId);
            // 修改逻辑
        }else if(oper.equals("edit")){
            userDao.updateByPrimaryKeySelective(user);
            hashMap.put("userId",user.getId());
            // 删除
        }else {
            userDao.deleteByIdList(Arrays.asList(id));
        }
        return hashMap;
    }


    @RequestMapping("showUserTime")
    public Map showUserTime(){
        HashMap hashMap = new HashMap();
        ArrayList manList = new ArrayList();
        manList.add(userDao.queryUserByTime("0",1));
        manList.add(userDao.queryUserByTime("0",7));
        manList.add(userDao.queryUserByTime("0",30));
        manList.add(userDao.queryUserByTime("0",365));
        ArrayList womenList = new ArrayList();
        womenList.add(userDao.queryUserByTime("1",1));
        womenList.add(userDao.queryUserByTime("1",7));
        womenList.add(userDao.queryUserByTime("1",30));
        womenList.add(userDao.queryUserByTime("1",365));
        hashMap.put("man",manList);
        hashMap.put("women",womenList);
        return hashMap;
    }

    @RequestMapping("map")
    public Map map(){
        HashMap hashMap = new HashMap();
        List<MapDto> manList = userDao.queryUserLocation("1");
        List<MapDto> womenList = userDao.queryUserLocation("0");
        hashMap.put("man",manList);
        hashMap.put("women",womenList);
        return hashMap;
    }
    //-----------------------------------------------
/*接口文档*/
    @RequestMapping("login")
    public Map login(String phone, String password) {
        HashMap hashMap = new HashMap();
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        User user1 = userDao.selectOne(user);
        System.out.println(user1);
        if (user1 == null) {
            hashMap.put("status", -200);
            hashMap.put("message","");
        }
        hashMap.put("status",200);
        hashMap.put("user",user1);
        return hashMap;
    }


    @RequestMapping("sendCode")
    public Map sendCode(String phone){
        String s = UUID.randomUUID().toString();
        String code = s.substring(0, 3);
        SmsUtil.send(phone,code);
        // 将验证码保存值Redis  Key phone_186XXXX Value code 1分钟有效
        HashMap hashMap = new HashMap();
        hashMap.put("status","200");
        hashMap.put("message","短信发送成功");
        return hashMap;
    }
    /* 注册接口文档 */
    @RequestMapping("sendAdd")
    public Map sendAdd(User user,String salt) {
                    HashMap hashMap = new HashMap();
                     String s = UUID.randomUUID().toString();
                     user.setId(s);
                    user.setRigest_date(new Date());
                    user.setSalt("12312");
                    user.setSex("1");
                    user.setPhone("1243413");
                    user.setNick_name("1351");
                    userDao.insert(user);
                    hashMap.put("status",200);
                    hashMap.put("message","");
                    return hashMap;
    }
    /* 补充信息接口 */
    @RequestMapping("sendAdd1")
    public Map sendAdd1(User user){
        HashMap hashMap = new HashMap();
        String s = UUID.randomUUID().toString();
        user.setId(s);
        int insert = userDao.insert(user);
        if (insert==0){
            hashMap.put("status", -200);
            hashMap.put("message","");
        }
        hashMap.put("status",200);
        hashMap.put("user",user);
        return hashMap;
    }



}
