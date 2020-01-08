package com.baizhi.zjy.controller;

import com.baizhi.zjy.dao.AlbumDao;
import com.baizhi.zjy.dao.ChapterDao;
import com.baizhi.zjy.entity.Album;
import com.baizhi.zjy.entity.Article;
import com.baizhi.zjy.entity.Chapter;
import com.baizhi.zjy.entity.User;
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
@RequestMapping("album")
public class AlbumController {
    @Autowired
    AlbumDao albumDao;
    /*
    * 分页查询
    * records 总条数  page当前页 rows数据  total 总页数
    * 调用方法
    * */
    @RequestMapping("showAllAlbums")
    public Map showAllAlbums(Integer page,Integer rows,HttpSession session,Album album){
        //records 总条数 page当前页 rows 数据  total总页数
        HashMap map = new HashMap();
        //不传入任何条件查所有
        Integer records = albumDao.selectCount(null);
        //计算总条数
        Integer total=records%rows==0?records/rows:records/rows+1;
        //调用方法  分页查询
        List<Album> albums = albumDao.selectByRowBounds(null, new RowBounds((page - 1) * rows, rows));

        map.put("records",records);
        map.put("page",page);
        map.put("total",total);
        map.put("rows",albums);
        return map;
    }

    /*
     *jqGird增删改查
     *
     * 1.添加使用UUID的加密方式 加入ID
     *
     * 2.修改调用业务方法 进行修改获得ID
     *
     * 3.批量删除 调用业务方法 获得数组类型ID
     */
    @RequestMapping("editAlbum")
    public Map editAlbum(String oper,Album album,String[] id){
        HashMap hashMap = new HashMap();
        //进行逻辑
        if (oper.equals("add")){
            String albumId = UUID.randomUUID().toString();
            album.setId(albumId);
            albumDao.insert(album);
            hashMap.put("albumId",albumId);
        }else if (oper.equals("edit")){
            albumDao.updateByPrimaryKeySelective(album);
            hashMap.put("albumId",album.getId());
        }else{
            albumDao.deleteByIdList(Arrays.asList(id));
        }
        return hashMap;
    }
    /*
    * 上传 和 下载
    * MultipartFile url(上传的文件),String albumId(轮播图Id 更新使用)
    *
    * */
    @RequestMapping("uploadAlbum")
    public Map uploadAlbum(MultipartFile cover, String albumId, HttpSession session, HttpServletRequest request) throws UnknownHostException {
        /*
        * 1.相对路径：/upload/img/xxx.mp3  优点：方便处理 缺点：耦合度较高，网络环境下难以定位
        * 2.网络路径：http：//localhost:8989/cmfz/upload/img/xxx.mp3 优点：定位精准度较高，缺点：处理麻烦
        * */
        HashMap hashMap = new HashMap();
        //获取真实路径
        String realPath = session.getServletContext().getRealPath("/upload/img/");
        //判断文件夹是否存在
        File file = new File(realPath);
        if (!file.exists()){
            file.mkdirs();
        }

        //防止重名
        String originalFilename=new Date().getTime()+"_"+cover.getOriginalFilename();
        /*
         *获取网络路径
         * */
        //获取协议头
        String scheme = request.getScheme();
        //获取ip
        String localhost = InetAddress.getLocalHost().toString();
        //获取端口号
        int serverPort = request.getServerPort();
        //获取项目名字
        String contextPath = request.getContextPath();

        String uri=scheme+"://"+localhost.split("/")[1]+":"+serverPort+contextPath+"/upload/img/"+originalFilename;

        //文件上传
        try {
            cover.transferTo(new File(realPath,originalFilename));
            //更新数据库信息
            Album album = new Album();
            album.setId(albumId);
            album.setCover(uri);
            albumDao.updateByPrimaryKeySelective(album);
        } catch (IOException e) {
            e.printStackTrace();
        }
        hashMap.put("status",200);
        return hashMap;
    }


    public Album selectOne(HttpSession session,Album album){
        Album album1 = albumDao.selectOne(album);
        session.setAttribute("albums",album1);
        return album1;
    }
//---------------------------------------------
    @Autowired
    ChapterDao chapterDao;
    /*专辑详情接口*/
    @RequestMapping("albumGet")
    public Map albumGet(String uid,String id){
        HashMap hashMap = new HashMap();
        Album album = albumDao.selectByPrimaryKey(id);
        Chapter chapter = chapterDao.selectByPrimaryKey(uid);
        chapter.setAlbumId(album.getId());
        List<Chapter> list = chapterDao.selectAll();
        hashMap.put("ablum",album);
        hashMap.put("list",list);
        return hashMap;
    }
}
