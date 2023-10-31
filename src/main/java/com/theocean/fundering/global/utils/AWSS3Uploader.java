package com.theocean.fundering.global.utils;


import com.theocean.fundering.global.config.AWSS3Config;
import com.theocean.fundering.global.errors.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Uri;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AWSS3Uploader {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AWSS3Config awss3Config;

    public String uploadToS3(MultipartFile mFile) {
        S3Client s3Client = awss3Config.s3Client();
        String fileName = UUID.randomUUID() + "_" + mFile.getOriginalFilename();
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(mFile.getInputStream(), mFile.getInputStream().available()));
        }catch (IOException e){
            throw new Exception500("file upload error");
        }
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build()).toString();
    }

}
