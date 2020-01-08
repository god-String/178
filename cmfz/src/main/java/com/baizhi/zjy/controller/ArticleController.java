package com.baizhi.zjy.controller;


import com.baizhi.zjy.dao.ArticleDao;
import com.baizhi.zjy.dao.GuruDao;
import com.baizhi.zjy.dao.UserDao;
import com.baizhi.zjy.entity.Article;
import com.baizhi.zjy.entity.Guru;
import com.baizhi.zjy.entity.User;
import com.baizhi.zjy.until.HttpUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("article")
public class ArticleController {
    @Autowired
    ArticleDao articleDao;
    private Article article;

    @Autowired
    GuruDao guruDao;
    @RequestMapping("showAllGuru")
    public List<Guru> showAllGuru(){
        List<Guru> gurus = guruDao.selectAll();
        System.out.println(gurus);
        return gurus;
    }

    @RequestMapping("uploadImg")
    public Map uploadImg(MultipartFile imgFile, HttpSession session, HttpServletRequest request){
        // 该方法需要返回的信息 error 状态码 0 成功 1失败   成功时 url 图片路径
        HashMap hashMap = new HashMap();
        String realPath = session.getServletContext().getRealPath("/upload/img/");
        File file = new File(realPath);
        if (!file.exists()){
            file.mkdirs();
        }
        // 网络路径
        try{
            String http = HttpUtil.getHttp(imgFile, request, "/upload/img/");
            hashMap.put("error",0);
            hashMap.put("url",http);
        }catch (Exception e){
            hashMap.put("error",1);
            e.printStackTrace();
        }
        return hashMap;
    }

    @RequestMapping("showAllImg")
    public Map showAllImg(HttpServletRequest request,HttpSession session){
        HashMap hashMap = new HashMap();
        hashMap.put("current_url",request.getContextPath()+"/upload/img/");
        String realPath = session.getServletContext().getRealPath("/upload/img/");
        File file = new File(realPath);
        File[] files = file.listFiles();
        hashMap.put("total_count",files.length);
        ArrayList arrayList = new ArrayList();
        for (File file1 : files) {
            HashMap fileMap = new HashMap();
            fileMap.put("is_dir",false);
            fileMap.put("has_file",false);
            fileMap.put("filesize",file1.length());
            fileMap.put("is_photo",true);
            String name = file1.getName();
            String extension = FilenameUtils.getExtension(name);
            fileMap.put("filetype",extension);
            fileMap.put("filename",name);
            // 通过字符串拆分获取时间戳
            String time = name.split("_")[0];
            // 创建SimpleDateFormat对象 指定yyyy-MM-dd hh:mm:ss 样式
            //  simpleDateFormat.format() 获取指定样式的字符串(yyyy-MM-dd hh:mm:ss)
            // format(参数)  参数:时间类型   new Date(long类型指定时间)long类型  现有数据:字符串类型时间戳
            // 需要将String类型 转换为Long类型 Long.valueOf(str);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String format = simpleDateFormat.format(new Date(Long.valueOf(time)));
            fileMap.put("datetime",format);
            arrayList.add(fileMap);
        }
        hashMap.put("file_list",arrayList);
        return hashMap;
    }

    @RequestMapping("insertArticle")
    public String insertArticle(Article article,MultipartFile inputfile,HttpServletRequest request){
        //HashMap hashMap = new HashMap();
        if (article.getId()==null||"".equals(article.getId())){
            String articleId = UUID.randomUUID().toString();
            article.setId(articleId);
            String s = HttpUtil.getHttp(inputfile, request, "/upload/img/");
            //System.out.println(s);
            article.setImg(s);
            articleDao.insert(article);
            //hashMap.put("articleId",articleId);
        }else{
            String ss = HttpUtil.getHttp(inputfile, request, "/upload/img/");
            article.setImg(ss);
            articleDao.updateByPrimaryKeySelective(article);
            //hashMap.put("articleId",article.getId());
            //articleDao.deleteByIdList(Arrays.asList(article.getId()));
        }

        //System.out.println(article);
        //System.out.println(inputfile);
        return "ok";
    }

    @RequestMapping("del")
    public String del(String id){
        articleDao.deleteByPrimaryKey(id);
        return "ok";
    }


    @RequestMapping("showAllArticles")
    public Map showAllArticles(Integer page,Integer rows,String guruId){
        HashMap hashMap = new HashMap();
        Article article = new Article();
        article.setGuru_id(guruId);
        int i = articleDao.selectCount(null);
        Integer total=i%rows==0?i/rows:i/rows+1;
        List<Article> articles = articleDao.selectByRowBounds(null, new RowBounds((page - 1) * rows, rows));
        hashMap.put("records",i);
        hashMap.put("page",page);
        hashMap.put("total",total);
        hashMap.put("rows",articles);
        return hashMap;
    }

   /* @RequestMapping("editArticle")
    public Map editArticle(String[] id,String oper,Article article,String guruId){
        HashMap hashMap = new HashMap();
        if ("add".equals(oper)) {
            String articleId = UUID.randomUUID().toString();
            article.setId(articleId);
            article.setGuru_id(guruId);
            articleDao.insert(article);
            hashMap.put("articleId",articleId);
        }else if (oper.equals("edit")){
            articleDao.updateByPrimaryKeySelective(article);
            hashMap.put("articleId",article.getId());
        }else {
            articleDao.deleteByIdList(Arrays.asList(id));
        }
        return hashMap;
    }

    @RequestMapping("articleUpload")
    public Map articleUpload(MultipartFile img, String articleId,String guruId, HttpServletRequest request, HttpSession session) throws UnknownHostException {
        HashMap hashMap = new HashMap();
        String realPath = session.getServletContext().getRealPath("/upload/img/");
        File file = new File(realPath);
        if(!file.exists()){
            file.mkdirs();
        }
        String Filename=new Date().getTime()+"_"+img.getOriginalFilename();
        //获取协议头
        String scheme = request.getScheme();
        //获取ip
        String localhost = InetAddress.getLocalHost().toString();
        //获取端口号
        int serverPort = request.getServerPort();
        //获取项目名字
        String contextPath = request.getContextPath();

        String uri=scheme+"://"+localhost.split("/")[1]+":"+serverPort+contextPath+"/upload/img/"+Filename;

        try {
            img.transferTo(new File(realPath,Filename));
            Article article = new Article();
            article.setId(articleId);
            article.setImg(uri);
            articleDao.updateByPrimaryKeySelective(article);
        } catch (IOException e) {
            e.printStackTrace();
        }
        hashMap.put("status",200);
        return hashMap;
    }
*/

   //-----------------------------------------
    @Autowired
   UserDao userDao;
    @RequestMapping("selectAll")
    public Map selectAll(String uid,String id){
        HashMap hashMap = new HashMap();
        User user = userDao.selectByPrimaryKey(uid);
        Article article = articleDao.selectByPrimaryKey(id);
        article.setGuru_id(user.getId());
        hashMap.put("status",200);
        hashMap.put("article",article);
        hashMap.put("user",user);
        return hashMap;
    }



}
