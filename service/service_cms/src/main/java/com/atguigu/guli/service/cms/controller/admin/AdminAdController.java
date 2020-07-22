package com.atguigu.guli.service.cms.controller.admin;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.Vo.AdVo;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/cms/ad")
public class AdminAdController {

    @Autowired
    AdService adService;


    @GetMapping("/list")
    public R getList(){

        List<Ad> list = adService.list();
        return R.ok().data("items",list);

    }

    @ApiOperation("新增推荐")
    @PostMapping("save")
    public R save(@ApiParam(value = "推荐对象", required = true) @RequestBody Ad ad) {

        boolean result = adService.save(ad);
        if (result) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }
    @ApiOperation("更新推荐")
    @PutMapping("update")
    public R updateById(@ApiParam(value = "讲师推荐", required = true) @RequestBody Ad ad) {
        boolean result = adService.updateById(ad);
        if (result) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id获取推荐信息")
    @GetMapping("get/{id}")
    public R getById(@ApiParam(value = "推荐ID", required = true) @PathVariable String id) {
        Ad ad = adService.getById(id);
        if (ad != null) {
            return R.ok().data("item", ad);
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation(value = "根据ID删除推荐")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value = "推荐ID", required = true) @PathVariable String id) {

        //删除图片
        adService.removeAdImageById(id);

        //删除推荐
        boolean result = adService.removeById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @GetMapping("/getAd/{page}/{limit}")
    public R getAd(@ApiParam(value = "当前页",required = true) @PathVariable Long page,
                   @ApiParam(value = "每页记录数",required = true)@PathVariable Long limit){


          Page<AdVo> adVoPage = adService.selectPage(page,limit);

        List<AdVo> records = adVoPage.getRecords();
        long total = adVoPage.getTotal();
        return R.ok().data("items",records).data("total",total);
    }
}
