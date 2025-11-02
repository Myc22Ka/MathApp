package pl.myc22ka.mathapp.s3.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutBucketPolicyRequest;

@Component
@RequiredArgsConstructor
public class S3Helper {

    private final S3Client s3Client;

    @Value("${spring.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.aws.s3.endpoint}")
    private String endpoint;

    @Value("${spring.aws.s3.public-url:http://localhost:9000}")
    private String publicUrl;

    @NotNull
    public String extractKeyFromUrl(@NotNull String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }

    public void ensureBucketExists() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
            setPublicBucketPolicy();
        }
    }

    public String generateS3Key(String originalFilename) {
        return System.currentTimeMillis() + "_" + originalFilename;
    }

    private void setPublicBucketPolicy() {
        String policy = "{\n" +
                "  \"Version\": \"2012-10-17\",\n" +
                "  \"Statement\": [\n" +
                "    {\n" +
                "      \"Effect\": \"Allow\",\n" +
                "      \"Principal\": \"*\",\n" +
                "      \"Action\": \"s3:GetObject\",\n" +
                "      \"Resource\": \"arn:aws:s3:::" + bucketName + "/*\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        s3Client.putBucketPolicy(PutBucketPolicyRequest.builder()
                .bucket(bucketName)
                .policy(policy)
                .build());
    }

    @NotNull
    public String buildFileUrl(String key) {
        return String.format("%s/%s/%s", publicUrl, bucketName, key);
    }

    @NotNull
    public String buildInternalFileUrl(String key) {
        return String.format("%s/%s/%s", endpoint, bucketName, key);
    }
}
