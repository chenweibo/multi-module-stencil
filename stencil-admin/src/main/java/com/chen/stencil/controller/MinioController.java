package com.chen.stencil.controller;


import com.chen.stencil.common.response.CommonResult;
import com.chen.stencil.dto.MinioUploadDto;
import io.minio.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MinIO对象存储管理
 */
@Api(tags = "MinIO对象存储管理")
@Controller
@RequestMapping("/minio")
public class MinioController {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.chen.stencil.controller.MinioController.class);
    @Value("${minio.endpoint}")
    private String ENDPOINT;
    @Value("${minio.bucketName}")
    private String BUCKET_NAME;
    @Value("${minio.accessKey}")
    private String ACCESS_KEY;
    @Value("${minio.secretKey}")
    private String SECRET_KEY;

    @ApiOperation("文件上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            //创建一个MinIO的Java客户端

            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(ENDPOINT)
                            .credentials(ACCESS_KEY, SECRET_KEY)
                            .build();

            //判断存储桶是否存在
            boolean isExist =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());

            if (isExist) {
                LOGGER.info("存储桶已经存在！");
            } else {
                //创建存储桶并设置只读权限
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
                //  minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(BUCKET_NAME)..build());
            }
            String filename = file.getOriginalFilename();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            // 设置存储对象名称
            String objectName = sdf.format(new Date()) + "/" + filename;
            // 使用putObject上传一个文件到存储桶中
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME).object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType()).build());


            LOGGER.info("文件上传成功!");
            MinioUploadDto minioUploadDto = new MinioUploadDto();
            minioUploadDto.setName(filename);
            minioUploadDto.setUrl(ENDPOINT + "/" + BUCKET_NAME + "/" + objectName);
            return CommonResult.success(minioUploadDto);
        } catch (Exception e) {
            LOGGER.info("上传发生错误: {}！", e.getMessage());
        }
        return CommonResult.failed();
    }

    @ApiOperation("文件删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("objectName") String objectName) {
        try {

            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(ENDPOINT)
                            .credentials(ACCESS_KEY, SECRET_KEY)
                            .build();

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectName)
                            .build());

            return CommonResult.success(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }
}
