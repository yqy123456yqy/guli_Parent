package com.atguigu.guli.service.edu.controller.admin;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherQueryVo;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.atguigu.guli.service.edu.feign.feiginTest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-04-03
 */
@Slf4j
@Api(description = "讲师管理")
@RestController
@RequestMapping("/admin/edu/teacher")
public class TeacherController {

    @Autowired
    TeacherService teacherService;
    @ApiOperation(value = "查看所有讲师")
    @GetMapping("/listAll")
    public R list(){
        List<Teacher> list = teacherService.list();
        log.info("讲师列表-----------");
        return  R.ok().data("itmes",list).message("获取讲师列表成功");
    }
    @ApiOperation(value = "根据id删除讲师",notes = "根据id删除讲师")
    @DeleteMapping("/delete/{id}")
    public R delete(@ApiParam(value = "讲师ID",required = true) @PathVariable String id){

        teacherService.removeAvatarById(id);

        boolean result = teacherService.removeById(id);
        if(result){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("删除失败");
        }

    }
    @ApiOperation("分页查询")
    @GetMapping("/list/{page}/{limit}")
    public R getpage(@ApiParam(value = "当前页码",required = true)
                                 @PathVariable Long page,
                     @ApiParam (value="当前页记录数",required = true) @PathVariable Long limit,@ApiParam("讲师查询列表") TeacherQueryVo teacherQueryVo){
        Page<Teacher> pageParam = new Page<>(page,limit);

        Page<Teacher> pageModel = teacherService.selectPage(pageParam,teacherQueryVo);
        List<Teacher> records = pageModel.getRecords();
        long total = pageModel.getTotal();
        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation("添加讲师")
    @PostMapping("/add")
    public R add(@RequestBody Teacher teacher){
      teacherService.save(teacher);
        return R.ok().message("添加成功");
    }

    @ApiOperation(value = "根据id修改讲师")
    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@ApiParam(value = "讲师id")@PathVariable String id){

        Teacher result = teacherService.getById(id);

        if(result == null){
            return R.error().message("信息不存在");
        }else{
            return R.ok().data("rows",result).message("查看成功");
        }

    }
    @ApiOperation(value = "根据id修改讲师")
    @PutMapping("/update")
    public R update(@ApiParam(value = "讲师对象")@RequestBody Teacher teacher){
        boolean result = teacherService.updateById(teacher);
        if(result){
            return R.ok().message("修改成功");
        }else{
            return R.error().message("修改失败");
        }

    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("/batch")
    public R batchdelete(@ApiParam(value = "讲师列表") @RequestBody List<String> ids){
        boolean result = teacherService.removeByIds(ids);
        if(result){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("删除失败");
        }


    }
    @ApiOperation("根据关键字查询讲师列表")
    @GetMapping("/list/name/{key}")
    public R listName(@ApiParam(value = "关键字",required = true)@PathVariable String key){

       List<Map<String , Object>> nameList =  teacherService.selectNameList(key);

        return R.ok().data("nameList",nameList);
    }

    @Autowired
    feiginTest feigintest;

    @ApiOperation("测试并发")
    @GetMapping("/test")
    public R test(){

        feigintest.test();
        log.info("调用成功");
        return R.ok();
    }

    @ApiOperation("测试Concureent")
    @GetMapping("/getConcurrent")
    public R getConcurrent(){
        return R.ok();
    }

    @GetMapping("/message1")
    public String message1() {
        return "message1";
    }

    @GetMapping("/message2")
    public String message2() {
        return "message2";
    }




}

