package com.atguigu.service.oss.controller.admin;
import com.atguigu.guli.common.base.result.utils.ExceptionUtils;
import com.atguigu.guli.common.base.result.result.R;
import com.atguigu.guli.common.base.result.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.service.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Api(description = "文件上传")
@RestController
@RequestMapping("/admin/oss/file")
@Slf4j
public class OssController {

    @Autowired
    FileService fileService;



    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public R upload(@ApiParam(value = "文件",required = true) @RequestParam(value = "file") MultipartFile file,@ApiParam(value = "模块",required = true) @RequestParam(value = "modul") String modul){

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String uploadURL = fileService.upload(inputStream, modul, originalFilename);

            return R.ok().message("上传成功").data("url",uploadURL);
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
           throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    @GetMapping("/test")
    public void test(){
        log.info("feign被调用了");

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @ApiOperation("删除文件")
    @DeleteMapping("/remove")
    public R removeFile(@ApiParam(value = "删除文件的url",required = true) @RequestBody String url){
        fileService.removeFile(url);

        return R.ok().message("文件删除成功");
    }



}
