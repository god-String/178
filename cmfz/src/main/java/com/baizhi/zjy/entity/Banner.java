package com.baizhi.zjy.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;

import com.baizhi.zjy.easypoi.ImageConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Banner implements Serializable {
        @ExcelProperty("ID")
        @Id
        private String id;
        @ExcelProperty("标题")
        private String title;
        @ExcelProperty(value = "图片",converter = ImageConverter.class)
        private String url;
        @ExcelIgnore
        private String href;
        @ExcelProperty("时间")
        @DateTimeFormat(pattern="yyyy-MM-dd")
        @JSONField(format = "yyyy-MM-dd")
        private Date create_date;
        @ExcelProperty("描述")
        @Column(name = "`desc`")
        private String desc;
        @ExcelProperty("状态")
        private String status;
 }
