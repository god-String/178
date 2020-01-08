package com.baizhi.zjy.dao;

import com.baizhi.zjy.entity.Task;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface TaskDao extends Mapper<Task>, InsertListMapper<Task>, DeleteByIdListMapper<Task,String> {

}
