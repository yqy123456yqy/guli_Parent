package com.atguigu.service.vod.service;

import com.aliyuncs.exceptions.ClientException;

import java.io.InputStream;
import java.util.List;

public interface VideoService {

    String uploadVideo(InputStream inputStream,String originalFilename);

    void removeVideo(String vodId) throws ClientException;

    void removeVods(List<String> ids) throws ClientException;

    String selectPlayAuth(String videoSourceId) throws ClientException;
}
