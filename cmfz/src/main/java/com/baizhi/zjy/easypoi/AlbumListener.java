package com.baizhi.zjy.easypoi;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baizhi.zjy.entity.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumListener extends AnalysisEventListener<Album> {
    List<Album> list = new ArrayList<>();
    @Override
    public void invoke(Album album, AnalysisContext analysisContext) {
        list.add(album);
        System.out.println(album);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println(list);
    }
}
