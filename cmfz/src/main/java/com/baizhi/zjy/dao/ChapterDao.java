package com.baizhi.zjy.dao;

import com.baizhi.zjy.entity.Chapter;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface ChapterDao extends Mapper<Chapter>, InsertListMapper<Chapter>, DeleteByIdListMapper<Chapter,String> {
}
