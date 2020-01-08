package com.baizhi.zjy.dao;

import com.baizhi.zjy.entity.Chapter;
import com.baizhi.zjy.entity.Guru;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface GuruDao extends Mapper<Guru>,InsertListMapper<Guru>, DeleteByIdListMapper<Guru,String> {
}
