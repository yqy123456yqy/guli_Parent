package com.atguigu.guli.service.cms.service.impl;

import com.alibaba.nacos.client.utils.StringUtils;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.Vo.AdVo;
import com.atguigu.guli.service.cms.feign.OssFileService;
import com.atguigu.guli.service.cms.mapper.AdMapper;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-05-02
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {
    @Autowired
    OssFileService ossFileService;

    @Override
    public boolean removeAdImageById(String id) {

        Ad ad = baseMapper.selectById(id);
        if (ad != null) {
            String imagesUrl = ad.getImageUrl();
            if (!StringUtils.isEmpty(imagesUrl)) {
                //删除图片
                R r = ossFileService.removeFile(imagesUrl);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Override
    public Page<AdVo> selectPage(Long page, Long limit) {

        QueryWrapper<AdVo> adVoQueryWrapper = new QueryWrapper<>();

        adVoQueryWrapper.orderByDesc("sort","id");
        Page<AdVo> pageParam = new Page<>(page,limit);
        List<AdVo> records =  baseMapper.selectAdVo(pageParam, adVoQueryWrapper);

                pageParam.setRecords(records);
                return pageParam;


    }

    @Cacheable(value = "index",key = "'selectAd'")
    @Override
    public List<Ad> selectAd(String adTypeId) {

        QueryWrapper<Ad> adQueryWrapper = new QueryWrapper<>();
        adQueryWrapper.eq("type_id", adTypeId);
        adQueryWrapper.orderByDesc("sort","id");


        List<Ad> ads = baseMapper.selectList(adQueryWrapper);
        return ads;

    }

}
