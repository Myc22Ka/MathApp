package pl.myc22ka.mathapp.s3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;


    @Value("${spring.aws.s3.bucket}")
    private String bucketName;

    public void uploadFile(MultipartFile file) {
        try {
            ensureBucketExists();

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public byte[] downloadFile(String fileName) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return s3Client.getObject(request).readAllBytes();
    }

    private void ensureBucketExists() {
        try {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        } catch (BucketAlreadyOwnedByYouException ignored) {
        }
    }
}

