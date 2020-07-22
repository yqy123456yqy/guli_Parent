package com.atguigu.guli.service.edu.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    private SubjectMapper subjectMapper;


    public ExcelSubjectDataListener(SubjectMapper subjectMapper) {
        this.subjectMapper = subjectMapper;
    }

    public ExcelSubjectDataListener() {

    }

    @Override
    public void invoke(ExcelSubjectData data, AnalysisContext analysisContext) {
        log.info("解析到一条数据{}",data);
        String levelOneTitle = data.getLevelOneTitle();
        String levelTowTitle = data.getLevelTowTitle();
        String parentId = null;

        Subject subjectByTitle = getSubjectByTitle(levelOneTitle);

        if(subjectByTitle == null){
            Subject subject = new Subject();
            subject.setTitle(levelOneTitle);
            subject.setParentId("0");

            subjectMapper.insert(subject);
            parentId = subject.getId();
        }else{
            parentId = subjectByTitle.getId();
        }

        Subject subjectByTitle1 = this.getSubjectByTitle(levelTowTitle, parentId);
        if(subjectByTitle1 == null){
            Subject subject = new Subject();
            subject.setTitle(levelTowTitle);
            subject.setParentId(parentId);
            subjectMapper.insert(subject);
        }


    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("全部解析完成");

    }

    private Subject getSubjectByTitle(String title){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper();

            queryWrapper.eq("title", title);
            queryWrapper.eq("parent_id", "0");

        return subjectMapper.selectOne(queryWrapper);
    }
    private Subject getSubjectByTitle(String title,String parentId){

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id",parentId);

        return subjectMapper.selectOne(queryWrapper);
    }


}
