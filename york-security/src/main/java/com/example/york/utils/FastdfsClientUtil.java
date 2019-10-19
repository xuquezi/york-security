package com.example.york.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class FastdfsClientUtil {
    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private ThumbImageConfig thumbImageConfig;

    //上传文件
    public String upload(MultipartFile myfile) throws Exception{
        //文件名
        String originalFilename = myfile.getOriginalFilename().substring(myfile.getOriginalFilename().lastIndexOf(".") + 1);
        // 文件扩展名
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());

        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(myfile.getInputStream(), myfile.getSize(),originalFilename , null);

        String path = storePath.getFullPath();

        return path;
    }
}
