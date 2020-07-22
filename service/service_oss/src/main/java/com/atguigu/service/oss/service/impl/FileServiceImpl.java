package com.atguigu.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.service.oss.service.FileService;
import com.atguigu.service.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    OssProperties ossProperties;

    @Override
    public String upload(InputStream inputStream, String modul, String originalFilename) {
        String endpoint = ossProperties.getEndpoint();
        String bucketname = ossProperties.getBucketname();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);

        if(!ossClient.doesBucketExist(bucketname)){
            ossClient.createBucket(bucketname);
            ossClient.setBucketAcl(bucketname, CannedAccessControlList.PublicRead);
        }

        //avatar/2020/04/17/a43d9836-9fe3-426f-a9ed-482673281b4a.jpg
        String folder = new DateTime().toString("yyyy/MM/dd");
        String fileExtension =originalFilename.substring(originalFilename.lastIndexOf("."));
        String foldername = UUID.randomUUID().toString();
        String key = modul+"/"+folder+"/"+foldername+fileExtension;
        // 上传文件流。
        ossClient.putObject(bucketname, key, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        return "https://" + bucketname + "." + endpoint + "/" + key;
    }

    @Override
    public void removeFile(String url) {

        String endpoint = ossProperties.getEndpoint();
        String bucketname = ossProperties.getBucketname();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();
        String host = "https://"+bucketname+"."+endpoint+"/";
        //https://guli-filess-1.oss-cn-shanghai.aliyuncs.com/avater/2020/04/18/e5c17b2c-e97c-45be-8764-b52ef28c912a.jpg
        String objectName = url.substring(host.length());
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketname, objectName);
        ossClient.shutdown();

    }


}
