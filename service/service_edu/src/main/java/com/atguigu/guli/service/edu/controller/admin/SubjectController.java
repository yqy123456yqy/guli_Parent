package com.atguigu.guli.service.edu.controller.admin;



import com.atguigu.guli.common.base.result.utils.ExceptionUtils;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.common.base.result.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-04-03
 */
@Slf4j
@Api("上传文件")
@RestController
@RequestMapping("/admin/edu/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @PostMapping("/import")
    public R imports(@ApiParam("文件名") @RequestParam("file") MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);

            return R.ok().message("批量导入成功");
        } catch (Exception e) {
           log.error(ExceptionUtils.getMessage(e));
           throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }

    @ApiOperation("嵌套数据列表")
    @GetMapping("/nested-list")
    public R nestedList(){

        List<SubjectVo> subjectVos =  subjectService.nestedList();
        return R.ok().data("items",subjectVos);
    }

}

