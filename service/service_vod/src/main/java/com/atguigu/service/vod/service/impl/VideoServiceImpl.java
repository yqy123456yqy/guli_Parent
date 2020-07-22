package com.atguigu.service.vod.service.impl;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.guli.common.base.result.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.service.vod.service.VideoService;
import com.atguigu.service.vod.util.AliyunVodSDKUtils;
import com.atguigu.service.vod.util.VodProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
   private VodProperties vodProperties;

    @Override
    public String uploadVideo(InputStream inputStream, String originalFilename) {

        String accessKeyId = vodProperties.getKeyid();
        String accessKeySecret = vodProperties.getKeysecret();
        String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, originalFilename, inputStream);


        /* 模板组ID(可选) */
       // request.setTemplateGroupId(vodProperties.getTemplateGroupId());
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);

        String videoId = response.getVideoId();
        System.out.println("videoId = " + videoId);
        System.out.println(" = ==========");
        if(StringUtils.isEmpty(videoId)){
            log.error("阿里云上传失败:"+response.getCode()+"-"+response.getMessage());
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }
        return videoId;
    }

    @Override
    public void removeVideo(String vodId) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());

        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(vodId);
        client.getAcsResponse(request);

    }

    @Override
    public void removeVods(List<String> ids) throws ClientException {

        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());

        DeleteVideoRequest request = new DeleteVideoRequest();
        int size = ids.size();
        StringBuffer idListStr = new StringBuffer();

        for (int i = 0; i < size; i++) {
            idListStr.append(ids.get(i));
            if(i == size -1 || i % 20 ==19){
                //支持传入多个视频ID，多个用逗号分隔
                request.setVideoIds(idListStr.toString());
                client.getAcsResponse(request);
                idListStr = new StringBuffer();
            }else if(i % 20 < 19){
                idListStr.append(",");
            }


        }

    }

    @Override
    public String selectPlayAuth(String videoSourceId) throws ClientException {

        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(),vodProperties.getKeysecret() );

        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoSourceId);


        GetVideoPlayAuthResponse response = client.getAcsResponse(request);


            //播放凭证
          return  response.getPlayAuth();



        }


}
