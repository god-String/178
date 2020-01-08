package com.baizhi.zjy.dao;


import com.baizhi.zjy.DTO.MapDto;
import com.baizhi.zjy.entity.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface UserDao extends Mapper<User>, InsertListMapper<User>, DeleteByIdListMapper<User,String> {
    Integer queryUserByTime(@Param("sex") String sex, @Param("day") Integer day);
    List<MapDto> queryUserLocation(String sex);
    //@Param("sex")String sex,@Param("name")String name,@Param("value")Integer value
}
