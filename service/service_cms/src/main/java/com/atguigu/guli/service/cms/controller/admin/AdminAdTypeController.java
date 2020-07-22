package com.atguigu.guli.service.cms.controller.admin;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.cms.entity.AdType;
import com.atguigu.guli.service.cms.service.AdTypeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/cms/ad-type")
public class AdminAdTypeController {

    @Autowired
    AdTypeService adTypeService;


    @PostMapping("/saveAdType")
    public R saveAdType(@ApiParam(value = "广告位",required = true) @RequestBody AdType adType){

        boolean result = adTypeService.save(adType);

        if(result){
            return R.ok().message("保存成功");
        }else{
            return R.error().message("保存失败");
        }
    }

    @DeleteMapping("deleteAdType/{id}")
    public R delete(@ApiParam(value = "广告位Id",required = true) @PathVariable String id){

        boolean result = adTypeService.removeById(id);
        if(result){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("删除失败");
        }
    }

    @PutMapping("update")
    public R update(@ApiParam(value = "广告位数据",required = true) @RequestBody AdType adType){

        boolean result = adTypeService.updateById(adType);
        if(result){
            return R.ok().message("修改成功");
        }else{
            return R.error().message("修改失败");
        }

    }

    @GetMapping("list")
    public R list(){
        List<AdType> adTypes = adTypeService.list();
        if(adTypes != null){
            return R.ok().data("adTypeList",adTypes);
        }else{
            return R.error().message("数据不存在");
        }
    }

    @GetMapping("getById/{id}")
    public R getById(@ApiParam(value = "广告Id",required = true) @PathVariable String id){
        AdType adType = adTypeService.getById(id);
        if(adType != null){
            return R.ok().data("adType",adType);
        }else{
            return R.error().message("数据不存在");
        }
    }

    @GetMapping("get/{page}/{limit}")
    public R getPage(@ApiParam(value = "当前页",required = true)@PathVariable Long page,
                     @ApiParam(value = "每页记录数",required = true)@PathVariable Long limit){


        Page<AdType> pageParam = new Page<>(page,limit);
        Page<AdType> adTypePage = adTypeService.page(pageParam);
        List<AdType> records = adTypePage.getRecords();
        long total = adTypePage.getTotal();

        return R.ok().data("items",records).data("total",total);



    }
}
