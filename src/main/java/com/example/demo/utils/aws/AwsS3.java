package com.example.demo.utils.aws;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.IOException;

public class AwsS3 {
    private S3Client client;
    private final static Region region = Region.AP_NORTHEAST_2;
    private final static String bucketName = "rising-test-today-house-s3";

    public static void createBucket(S3Client s3Client, String bucketName, Region region) {
        S3Waiter s3Waiter = s3Client.waiter();

        try {
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .createBucketConfiguration(
                            CreateBucketConfiguration.builder()
                                    .locationConstraint(region.id())
                                    .build()
                    ).build();

            s3Client.createBucket(bucketRequest);
            HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
            waiterResponse.matched().response().ifPresent(System.out::println);

            System.out.println(bucketName + "is ready");
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public void upload(MultipartFile multipartFile) throws IOException {

        client = S3Client.builder()
                .region(region)
                .build();

        System.out.println("초기");

        //요청 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key("aa")
                .build();

        System.out.println("중기 - 요청생성");

        RequestBody requestBody = RequestBody.fromInputStream(
                multipartFile.getInputStream(), multipartFile.getSize()
        );

        System.out.println("말기 - 바디에서 값 가져오기");

        client.putObject(putObjectRequest, requestBody);

        System.out.println("끝 - 요청 보내기 성공");
    }

}
