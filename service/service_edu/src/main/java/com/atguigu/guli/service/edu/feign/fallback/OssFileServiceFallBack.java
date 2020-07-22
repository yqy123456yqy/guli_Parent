package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.service.edu.feign.feiginTest;
import org.springframework.stereotype.Service;

@Service
public class OssFileServiceFallBack implements feiginTest {
    @Override
    public void test() {

    }

    @Override
    public R deleteFile(String url) {
        return R.error();
    }


}
