package com.baizhi.zjy.controller;

import com.baizhi.zjy.dao.ChapterDao;
import com.baizhi.zjy.entity.Chapter;
import com.baizhi.zjy.until.HttpUtil;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.RowBounds;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/chapter")
public class ChapterController {
    @Autowired
    ChapterDao chapterDao;

    @RequestMapping("showAllChapters")
    public Map showAllChapters(Integer page,Integer rows,String albumId){
        HashMap hashMap = new HashMap();
        Chapter chapter = new Chapter();
        chapter.setAlbumId(albumId);
        int records = chapterDao.selectCount(chapter);
        Integer total=records%rows==0?records/rows:records/rows+1;
        List<Chapter> chapters = chapterDao.selectByRowBounds(chapter, new RowBounds((page - 1) * rows, rows));
        hashMap.put("records",records);
        hashMap.put("page",page);
        hashMap.put("total",total);
        hashMap.put("rows",chapters);
        return hashMap;
    }

    @RequestMapping("editChapter")
    public Map editChapter(String oper, Chapter chapter,String albumId, String[] id,HttpSession session){
        HashMap hashMap = new HashMap();
        // 添加逻辑
        if (oper.equals("add")){
            //String id = albums.getId();
            String chapterId = UUID.randomUUID().toString();
            chapter.setId(chapterId);
            chapter.setAlbumId(albumId);
            chapterDao.insert(chapter);
            hashMap.put("chapterId",chapterId);
            // 修改逻辑
        }else if(oper.equals("edit")){
            chapterDao.updateByPrimaryKeySelective(chapter);
            hashMap.put("chapterId",chapter.getId());
            // 删除
        }else {
            chapterDao.deleteByIdList(Arrays.asList(id));
        }
        return hashMap;
    }


    @RequestMapping("uploadChapter")
    // MultipartFile url(上传的文件),String bannerId(轮播图Id 更新使用)
    public Map uploadChapter(MultipartFile url, String chapterId, HttpSession session, HttpServletRequest request) throws IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException {
        String realPath = session.getServletContext().getRealPath("/upload/music/");
        File file = new File(realPath);
        if (!file.exists()){
            file.mkdirs();
        }
        String http = HttpUtil.getHttp(url, request, "/upload/music/");
        Chapter chapter = new Chapter();
        chapter.setId(chapterId);
        chapter.setUrl(http);
        // 计算文件大小
        Double size = Double.valueOf(url.getSize()/1024/1024);
        chapter.setSize(size);
        // 计算音频时长
        // 使用三方计算音频时间工具类 得出音频时长
        String[] split = http.split("/");
        // 获取文件名
        String name = split[split.length-1];
        // 通过文件获取AudioFile对象 音频解析对象
        AudioFile read = AudioFileIO.read(new File(realPath, name));
        // 通过音频解析对象 获取 头部信息 为了信息更准确 需要将AudioHeader转换为MP3AudioHeader
        MP3AudioHeader audioHeader = (MP3AudioHeader) read.getAudioHeader();
        // 获取音频时长 秒
        int trackLength = audioHeader.getTrackLength();
        String time = trackLength/60 + "分" + trackLength%60 + "秒";
        chapter.setTime(time);
        chapterDao.updateByPrimaryKeySelective(chapter);
        HashMap hashMap = new HashMap();
        hashMap.put("status",200);
        return hashMap;
    }


    @RequestMapping("downloadChapter")
    public void downloadChapter(String url, HttpServletResponse response, HttpSession session) throws IOException {
        // 处理url路径 找到文件
        String[] split = url.split("/");
        String realPath = session.getServletContext().getRealPath("/upload/img/");
        String name = split[split.length-1];
        File file = new File(realPath, name);
        // 调用该方法时必须使用 location.href 不能使用ajax ajax不支持下载
        // 通过url获取本地文件
        response.setHeader("Content-Disposition", "attachment; filename="+name);
        ServletOutputStream outputStream = response.getOutputStream();
        FileUtils.copyFile(file,outputStream);
        // FileUtils.copyFile("服务器文件",outputStream)
        //FileUtils.copyFile();
    }

}
