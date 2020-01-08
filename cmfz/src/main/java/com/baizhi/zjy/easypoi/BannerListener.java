package com.baizhi.zjy.easypoi;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baizhi.zjy.entity.Banner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.context.support.UiApplicationContextUtils;

import java.util.ArrayList;
import java.util.List;

public class BannerListener extends AnalysisEventListener<Banner> {
    List<Banner> list = new ArrayList<>();
    @Override
    public void invoke(Banner banner, AnalysisContext analysisContext) {
        list.add(banner);

        System.out.println(list);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println(list);
    }
}
