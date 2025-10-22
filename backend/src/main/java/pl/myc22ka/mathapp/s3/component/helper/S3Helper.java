package pl.myc22ka.mathapp.s3.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@Component
@RequiredArgsConstructor
public class S3Helper {

    private final S3Client s3Client;

    @Value("${spring.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.aws.s3.endpoint:}")
    private String endpoint;

    @NotNull
    public String extractKeyFromUrl(@NotNull String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }

    public void ensureBucketExists() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        }
    }

    @NotNull
    public String buildFileUrl(String key) {
        return String.format("%s/%s/%s", endpoint, bucketName, key);
    }
}
