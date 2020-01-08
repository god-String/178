package com.baizhi.zjy.test.entity;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

public class DemoDataListener extends AnalysisEventListener<DemoData> {
    List<DemoData> list = new ArrayList<DemoData>();

    //针对每行的数据进行封装
    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        list.add(demoData);
        System.out.println(demoData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println(list);
    }
}
