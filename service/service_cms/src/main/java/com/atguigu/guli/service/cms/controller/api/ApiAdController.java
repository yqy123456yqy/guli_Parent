package com.atguigu.guli.service.cms.controller.api;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.service.AdService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cms/ad")
public class ApiAdController {

    @Autowired
    AdService adService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("getById/{adTypeId}")
    public R getById(@ApiParam(value = "广告位Id",required = true) @PathVariable String adTypeId){


      List<Ad> ads =  adService.selectAd(adTypeId);
      if(ads != null){
          return R.ok().data("items",ads);
      }else{
          return R.error().message("数据不存在");
      }

    }

    @PostMapping("save-test")
    public R save(@RequestBody  Ad ad){
        redisTemplate.opsForValue().set("index::ad", ad);
        return R.ok();

    }

    @GetMapping("get/{key}")
    public R get(@PathVariable String key){

        Object result =(Ad) redisTemplate.opsForValue().get(key);

        return R.ok().data("item",result);
    }


}
