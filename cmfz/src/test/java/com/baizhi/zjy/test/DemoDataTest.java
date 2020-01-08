package com.baizhi.zjy.test;


import com.alibaba.excel.EasyExcel;
import com.baizhi.zjy.DTO.MapDto;
import com.baizhi.zjy.test.entity.DemoData;
import com.baizhi.zjy.test.entity.DemoDataListener;
import com.baizhi.zjy.test.entity.ImageData;
import io.goeasy.GoEasy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DemoDataTest {

    //导出
    @Test
    public void test1(){
       String fileName="F:\\Excel\\"+new Date().getTime()+".xlsx";
        EasyExcel.write(fileName, DemoData.class).sheet("测试")
                .doWrite(Arrays.asList(new DemoData(UUID.randomUUID().toString(),new Date(),18.0,"rxx"),
                        new DemoData(UUID.randomUUID().toString(),new Date(),19.0,"cxx"),
                        new DemoData(UUID.randomUUID().toString(),new Date(),20.0,"dxx"),
                        new DemoData(UUID.randomUUID().toString(),new Date(),21.0,"xxx")
                ));
    }


    //图片 导出
    @Test
    public void test4(){
           String fileName="F:\\Excel\\"+new Date().getTime()+".xlsx";
        ImageData imageData = new ImageData("img/1577602279886_xyj.jpg");
        EasyExcel.write(fileName, ImageData.class).sheet("c测试")
                .doWrite(Arrays.asList(imageData));
    }

    //上传

    @Test
    public void Test(){
        String url="F:\\Excel\\1577959063256.xlsx";
        EasyExcel.read(url,DemoData.class,new DemoDataListener()).sheet().doRead();
    }


    @Test
    public void test3(){
        String fileName = "F:\\Excel\\"+new Date().getTime()+".xlsx";
        HashSet<String> hashSet = new HashSet<>();
        // excludeColumnFiledNames 排除不需要显示的字段 hashset.add("属性名")
        hashSet.add("id");
        //hashSet.add("age");
        EasyExcel.write(fileName,DemoData.class).excludeColumnFiledNames(hashSet)
                .sheet().doWrite(Arrays.asList(
                new DemoData(UUID.randomUUID().toString(),new Date(),15.0,"rxx"),
                new DemoData(UUID.randomUUID().toString(),new Date(),16.0,"lxx"),
                new DemoData(UUID.randomUUID().toString(),new Date(),17.0,"kxx"),
                new DemoData(UUID.randomUUID().toString(),new Date(),18.0,"yxx")
        ));
    }


    @Test
    public void testgoeasy(){
        GoEasy goEasy = new GoEasy( "http://rest-hangzhou.goeasy.io", "BC-ae0f38db150e43b5af37a74c37c4cbe1");
        goEasy.publish("cmfz", "Hello, GoEasy!");
    }




      /*  @Test
        public void dsads(){
             ArrayList<MapDto> manList = new ArrayList();
                for (MapDto mapDto : manList) {
                String name = mapDto.getName();
                Integer value = mapDto.getValue();
                    System.out.println(name);
                    System.out.println(value);
        }
    }
*/
}
