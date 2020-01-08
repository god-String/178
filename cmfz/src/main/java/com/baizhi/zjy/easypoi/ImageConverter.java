package com.baizhi.zjy.easypoi;

import com.alibaba.excel.converters.string.StringImageConverter;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ImageConverter extends StringImageConverter {
    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws IOException {
        // 需要将value 由相对路径|网络路径 改为绝对路径
        String property = System.getProperty("user.dir");
        System.out.println(property);
        String[] split = value.split("/");
        value=split[split.length-1];
        String url=property+"\\src\\main\\webapp\\upload\\img\\"+value;
        return new CellData(FileUtils.readFileToByteArray(new File(url)));
    }
}
