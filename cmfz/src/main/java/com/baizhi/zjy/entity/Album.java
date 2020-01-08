package com.baizhi.zjy.entity;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Album implements Serializable {
    @ExcelProperty("ID")
    @Id
    private String id;
    @ExcelProperty("标题")
    private String title;
    @ExcelProperty("评分")
    private String score;
    @ExcelProperty("作者")
    private String author;
    @ExcelProperty("播音员")
    private String broadcast;
    @ExcelProperty("集数")
    private Integer count;
    @ExcelProperty("描述")
    @Column(name = "`desc`")
    private String desc;
    @ExcelProperty("状态")
    private String status;
    @ExcelProperty("创建日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private String create_date;
    @ExcelProperty("头像")
    private String cover;
}
