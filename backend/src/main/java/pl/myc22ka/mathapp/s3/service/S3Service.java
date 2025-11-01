package pl.myc22ka.mathapp.s3.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.myc22ka.mathapp.s3.component.helper.S3Helper;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

/**
 * Service for managing files in Amazon S3.
 * Provides methods to upload, download, delete files, and clear the S3 bucket.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Helper s3Helper;

    @Value("${spring.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.aws.s3.region}")
    private String region;

    /**
     * Uploads a file to S3.
     *
     * @param file the MultipartFile to upload
     * @param key  the S3 key under which to store the file
     * @return the URL of the uploaded file
     * @throws IOException if an I/O error occurs during upload
     */
    public String uploadFile(@NotNull MultipartFile file, String key) throws IOException {
        s3Helper.ensureBucketExists();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        return s3Helper.buildFileUrl(key);
    }

    /**
     * Downloads a file from S3.
     *
     * @param key the S3 key of the file to download
     * @return the file content as a byte array
     */
    public byte[] downloadFile(String key) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            return s3Client.getObject(request).readAllBytes();
        } catch (NoSuchKeyException e) {
            throw new RuntimeException("File not found in S3: " + key, e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }

    /**
     * Deletes a file from S3.
     *
     * @param fileUrl the URL of the file to delete
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;

        try {
            String key = s3Helper.extractKeyFromUrl(fileUrl);

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);

        } catch (NoSuchKeyException ignored) {
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    /**
     * Clears all files from the S3 bucket.
     */
    public void clearBucket() {
        try {
            s3Helper.ensureBucketExists();

            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(request);

            if (response.contents() != null && !response.contents().isEmpty()) {
                for (S3Object s3Object : response.contents()) {
                    DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3Object.key())
                            .build();
                    s3Client.deleteObject(deleteRequest);
                }
                System.out.println("[S3] Deleted " + response.contents().size() + " objects from bucket: " + bucketName);
            } else {
                System.out.println("[S3] Bucket is already empty: " + bucketName);
            }
        } catch (Exception e) {
            System.err.println("[S3] Error clearing bucket: " + e.getMessage());
            throw new RuntimeException("Failed to clear S3 bucket", e);
        }
    }
}
