package com.baizhi.zjy.test.entity;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class DemoData {
    /*
    * 基础数据类
    * @ExcelProperty 指定列名
    * @ExcelIgnore 忽略字段
    * */
    @ExcelProperty("班级")
    private String id;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;
    /*
    * 忽略这个字段
    * */
    @ExcelIgnore
    private String ignore;
}
