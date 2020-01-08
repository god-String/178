package com.baizhi.zjy.test;


import com.baizhi.zjy.dao.BannerDao;
import com.baizhi.zjy.entity.Banner;
import org.apache.poi.hssf.record.cf.FontFormatting;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestPois {
    @Autowired
    BannerDao bannerDao;
    @Test
    public void test01(){
        // 1. 创建Excle文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 2. 创建一个工作簿
        HSSFSheet sheet = workbook.createSheet();
        // 设置列宽度
        sheet.setColumnWidth(3,15*256);
        // 3. 创建行对象
        HSSFRow row = sheet.createRow(0);
        // 设置行高
        row.setHeight((short)2000);
        // 4. 创建单元格
        // 设置样式
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setColor((short)20);
        font.setFontHeightInPoints((short)20);
        // 需要计算机支持该字体
        font.setFontName("楷体");
        font.setItalic(true);    //斜体
        font.setUnderline(FontFormatting.U_SINGLE);  //下划线
        // 5起始行 10结束行 3起始列 7结束列
        // 合并单元格之后 单元格的值 使用 (5,3)值
        CellRangeAddress cellRangeAddress = new CellRangeAddress(5, 10, 3, 7);
        sheet.addMergedRegion(cellRangeAddress);
        // 将字体样式放入HSSFCellStyle中
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        HSSFCell cell = row.createCell(1);
        cell.setCellStyle(cellStyle);
        // 5. 为单元格赋值
        cell.setCellValue("王宝宝");
        // 6. 将Excle文档做本地输出
        try {
            // 不能操作打开中的文件
            workbook.write(new File("F:\\Excel\\"+new Date().getTime()+".xls"));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    // 实战示例
    /*
     *   poi与项目集成
     */
    public void test02(){
        List<Banner> banners = bannerDao.selectAll();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row = sheet.createRow(0);
        String[] str = {"ID","标题","图片","超链接","创建时间","描述","状态"};
        for (int i = 0; i < str.length; i++) {
            String s = str[i];
            row.createCell(i).setCellValue(s);
        }
        // 通过workbook对象获取样式对象
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        // 通过workbook对象获取数据格式化处理对象
        HSSFDataFormat dataFormat = workbook.createDataFormat();
        // 指定格式化样式 如 yyyy-MM-dd
        short format = dataFormat.getFormat("yyyy-MM-dd");
        // 为样式对象 设置格式化处理
        cellStyle.setDataFormat(format);
        for (int i = 0; i < banners.size(); i++) {
            Banner banner = banners.get(i);
            HSSFRow row1 = sheet.createRow(i + 1);
            row1.createCell(0).setCellValue(banner.getId());
            row1.createCell(1).setCellValue(banner.getTitle());
            row1.createCell(2).setCellValue(banner.getUrl());
            row1.createCell(3).setCellValue(banner.getHref());
            HSSFCell cell = row1.createCell(4);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(banner.getCreate_date());
            row1.createCell(5).setCellValue(banner.getDesc());
            row1.createCell(6).setCellValue(banner.getStatus());
        }
        try {
            workbook.write(new File("F:\\Excel\\"+new Date().getTime()+".xls"));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
        poi 导入
     */
    @Test
    public void test03() throws IOException {
        // 原始数据
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File("D:\\Excel\\1577932515608.xls")));
        // 数据的封装
        HSSFSheet sheet = workbook.getSheet("Sheet0");
        ArrayList<Banner> banners = new ArrayList<>();
        // sheet.getLastRowNum() 获取最后一行的行号 注意使用<= 遍历
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Banner banner = new Banner();
            HSSFRow row = sheet.getRow(i);
            String cell1 = row.getCell(0).getStringCellValue();
            String stringCellValue = row.getCell(6).getStringCellValue();
            double numericCellValue = row.getCell(7).getNumericCellValue();
            System.out.println(numericCellValue);
            banner.setId(cell1);
            banner.setStatus(stringCellValue);
            banners.add(banner);
        }
        System.out.println(banners);
    }
}
