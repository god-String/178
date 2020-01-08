package com.baizhi.zjy.controller;


import com.baizhi.zjy.dao.GuruDao;
import com.baizhi.zjy.entity.Guru;
import com.baizhi.zjy.until.HttpUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@RestController
@RequestMapping("guru")
public class GuruController {
    @Autowired
    GuruDao guruDao;
    /*@RequestMapping("showAllGuru")
    public List<Guru> showAllGuru(){
        List<Guru> gurus = guruDao.selectAll();
        System.out.println(gurus);
        return gurus;
    }
*/
    @RequestMapping("showAllGurus")
    public Map showAllGurus(Integer page,Integer rows){
        HashMap hashMap = new HashMap();
        int i = guruDao.selectCount(null);
        Integer total=i%rows==0?i/rows:i/rows+1;
        List<Guru> gurus = guruDao.selectByRowBounds(null, new RowBounds((page - 1) * rows, rows));
        hashMap.put("records",i);
        hashMap.put("page",page);
        hashMap.put("total",total);
        hashMap.put("rows",gurus);
        return hashMap;
    }


    @RequestMapping("editGuru")
    public Map editGuru(String oper,String[] id,Guru guru,MultipartFile photo,HttpServletRequest request){
        HashMap hashMap = new HashMap();
        if ("add".equals(oper)){
            String guruId = UUID.randomUUID().toString();
            guru.setId(guruId);
            guruDao.insert(guru);
            hashMap.put("guruId",guruId);
        }else if (oper.equals("edit")){
            //String ss = HttpUtil.getHttp(photo, request, "/upload/img/");
            //guru.setPhoto(ss);
            guruDao.updateByPrimaryKeySelective(guru);
            hashMap.put("guruId",guru.getId());
        }else {
            guruDao.deleteByIdList(Arrays.asList(id));
        }
        return hashMap;
    }

    @RequestMapping("guruUpload")
    public Map guruUpload(MultipartFile photo,String guruId ,HttpSession session, HttpServletRequest request) throws UnknownHostException {
        HashMap hashMap = new HashMap();
        String realPath = session.getServletContext().getRealPath("/upload/img/");
        File file = new File(realPath);
        if (!file.exists()){
            file.mkdirs();
        }
        String newFileName=new Date().getTime()+"_"+photo.getOriginalFilename();

        String scheme = request.getScheme();
        String s = InetAddress.getLocalHost().toString();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        String a=scheme+"://"+s.split("/")[1]+":"+serverPort+contextPath+"/upload/img/"+newFileName;
        try {
            photo.transferTo(new File(realPath,newFileName));
            Guru guru = new Guru();
            guru.setId(guruId);
            guru.setPhoto(a);
            guruDao.updateByPrimaryKeySelective(guru);
        } catch (IOException e) {
            e.printStackTrace();
        }
        hashMap.put("status",200);
        return hashMap;
    }
}
