package com.atguigu.service.oss.service;

import java.io.InputStream;

public interface FileService {

    String upload(InputStream inputStream,String modul,String originalFilename);

    void removeFile(String url);


}
