package com.baizhi.zjy.controller;


import com.baizhi.zjy.dao.TaskDao;
import com.baizhi.zjy.dao.UserDao;
import com.baizhi.zjy.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("task")
public class TaskController {
    @Autowired
    TaskDao optionDao;
    @RequestMapping("selectAll")
    public Map selectAll(String id){
        HashMap hashMap = new HashMap();
        Task task = new Task();
        task.getId();
        List<Task> list = optionDao.select(task);
        hashMap.put("status",200);
        hashMap.put("option",list);
        return hashMap;
    }

    @RequestMapping("insertAll")
    public Map insertAll(String id,String title){
        HashMap hashMap = new HashMap();
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setTitle(title);
        optionDao.insert(task);
        List<Task> list = optionDao.select(task);
        hashMap.put("status",200);
        hashMap.put("option",list);
        return hashMap;
    }
}
