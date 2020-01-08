package com.baizhi.zjy.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageData {
    @ExcelProperty(value = "图片",converter = ImageConverter.class)
    private String string;
}
