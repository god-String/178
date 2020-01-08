package com.baizhi.zjy.until;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class HttpUtil {
    public static String getHttp(MultipartFile file,HttpServletRequest request,String path){
        String realPath = request.getSession().getServletContext().getRealPath(path);
        // 防止重名
        String originalFilename = new Date().getTime()+"_"+file.getOriginalFilename();
        try {
            file.transferTo(new File(realPath,originalFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取网络路径
        // 获取协议头
        String scheme = request.getScheme();
        // 获取IP地址
        String localHost = null;
        try {
            localHost = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // 获取端口号
        int serverPort = request.getServerPort();
        // 获取项目名
        String contextPath = request.getContextPath();
        String uri = scheme +"://"+ localHost.split("/")[1] + ":" + serverPort + contextPath + path + originalFilename;
        return uri;
    }
}
