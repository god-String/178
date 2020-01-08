package com.baizhi.zjy.controller;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.baizhi.zjy.dao.BannerDao;
import com.baizhi.zjy.easypoi.BannerListener;
import com.baizhi.zjy.entity.Banner;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.context.support.UiApplicationContextUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.*;

@RestController
@RequestMapping("banner")
public class BannerController {
        @Autowired
        BannerDao bannerDao;
        @RequestMapping("showAllBanners")
        public Map showAllBanners(Integer page,Integer rows){
                // records 总条数 page 当前页 rows 数据 total 总页数
                HashMap hashMap = new HashMap();
                // bannerDao.selectCount(null); 不传入任何条件 查所有
                Integer records = bannerDao.selectCount(null);
                // 三目运算符 求总页数
                Integer total = records%rows==0?records/rows:records/rows+1;
                List<Banner> banners = bannerDao.selectByRowBounds(null, new RowBounds((page - 1) * rows, rows));
                hashMap.put("records",records);
                hashMap.put("page",page);
                hashMap.put("total",total);
                hashMap.put("rows",banners);
                return hashMap;
        }
        @RequestMapping("editBanner")
        public Map editBanner(String oper,Banner banner,String[] id){
                HashMap hashMap = new HashMap();
                // 添加逻辑
                if (oper.equals("add")){
                        String bannerId = UUID.randomUUID().toString();
                        banner.setId(bannerId);
                        bannerDao.insert(banner);
                        hashMap.put("bannerId",bannerId);
                        // 修改逻辑
                }else if(oper.equals("edit")){
                        bannerDao.updateByPrimaryKeySelective(banner);
                        hashMap.put("bannerId",banner.getId());
                        // 删除
                }else {
                        bannerDao.deleteByIdList(Arrays.asList(id));
                }
                return hashMap;
        }
        @RequestMapping("uploadBanner")
        // MultipartFile url(上传的文件),String bannerId(轮播图Id 更新使用)
        public Map uploadBanner(MultipartFile url, String bannerId, HttpSession session, HttpServletRequest request) throws UnknownHostException {
        /*
            1. 相对路径 : /upload/img/XXX.jpg 优:方便处理 缺:耦合度较高,在网络环境下难以定位
            2. 网络路径 : http://localhost:9999/cmfz/upload/img/XXX.jpg 缺:处理麻烦(需要程序员手动处理) 优:定位,精准度较高
         */
                HashMap hashMap = new HashMap();
                // 获取真实路径
                String realPath = session.getServletContext().getRealPath("/upload/img/");
                // 判断文件夹是否存在
                File file = new File(realPath);
                if (!file.exists()){
                        file.mkdirs();
                }
                // 防止重名
                String originalFilename = new Date().getTime()+"_"+url.getOriginalFilename();
                // 获取网络路径
                // 获取协议头
                String scheme = request.getScheme();
                // 获取IP地址
                String localHost = InetAddress.getLocalHost().toString();
                // 获取端口号
                int serverPort = request.getServerPort();
                // 获取项目名
                String contextPath = request.getContextPath();
                String uri = scheme +"://"+ localHost.split("/")[1] + ":" + serverPort + contextPath + "/upload/img/" + originalFilename;
                // 文件上传
                try {
                        url.transferTo(new File(realPath,originalFilename));
                        // 更新数据库信息
                        Banner banner = new Banner();
                        banner.setId(bannerId);
                        banner.setUrl(uri);
                        bannerDao.updateByPrimaryKeySelective(banner);
                } catch (IOException e) {
                        e.printStackTrace();
                }
                hashMap.put("status",200);
                return hashMap;
        }
        @RequestMapping("selectAll")
        public List<Banner> selectAll(HttpServletResponse response,HttpSession session,String url) throws IOException {
                String fileName="F:\\Excel\\"+new Date().getTime()+".xlsx";
                 List<Banner> list = bannerDao.selectAll();
                for (Banner banner : list) {
                        String urll = banner.getUrl();
                       banner.setUrl(urll);
                }
                EasyExcel.write(fileName, Banner.class).sheet("测试")
                        .doWrite(list);

                // 处理url路径 找到文件
                /*String[] split = url.split("/");
                //String realPath = session.getServletContext().getRealPath("/upload/img/");
                String name = split[split.length-1];
                File file = new File(fileName, name);
                // 调用该方法时必须使用 location.href 不能使用ajax ajax不支持下载
                // 通过url获取本地文件
                response.setHeader("Content-Disposition", "attachment; filename="+fileName);
                ServletOutputStream outputStream = response.getOutputStream();
                FileUtils.copyFile(file,outputStream);*/
                // FileUtils.copyFile("服务器文件",outputStream)
                //FileUtils.copyFile();

                /*String encode = URLEncoder.encode("下载.xlsx", "Utf-8");
                response.setHeader("Content-Disposition", "attachment; filename="+encode);
                ServletOutputStream outputStream = response.getOutputStream();
                EasyExcel.write(outputStream);*/
                return null;
        }

        @RequestMapping("read")
        public void read(Banner banner) throws Exception {
                //创建导入对象
                ImportParams params = new ImportParams();
                params.setTitleRows(1); //表格标题行数,默认0
                params.setHeadRows(2);  //表头行数,默认1
                //获取导入数据
                List<Banner> teachers = ExcelImportUtil.importExcel(new FileInputStream(new File("F:\\Excel\\1578226334109.xlsx")),Banner.class, params);

                for (Banner teacher : teachers) {
                        bannerDao.insert(teacher);
                }
              /*
                String url="F:\\Excel\\1578226197349.xlsx";
                *//*bannerDao.insert(list);*//*
                EasyExcel.read(url,Banner.class,new BannerListener()).sheet().doRead();*/
        }

}
