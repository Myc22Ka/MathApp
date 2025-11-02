package pl.myc22ka.mathapp.user.component.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.s3.component.helper.S3Helper;
import pl.myc22ka.mathapp.s3.model.ByteArrayMultipartFile;
import pl.myc22ka.mathapp.s3.model.images.ProfilePhoto;
import pl.myc22ka.mathapp.s3.repository.ProfilePhotoRepository;
import pl.myc22ka.mathapp.s3.service.S3Service;
import pl.myc22ka.mathapp.user.dto.UserInitData;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserInitializer {

    private final UserRepository userRepository;
    private final ProfilePhotoRepository profilePhotoRepository;
    private final S3Service s3Service;
    private final S3Helper s3Helper;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Initializes default users from JSON and uploads their profile photos to S3.
     *
     * @throws IOException if reading or parsing the JSON file fails
     */
    @PostConstruct
    public void init() throws IOException {
        System.out.println("[INIT] UserInitializer");

        if (userRepository.count() > 0) {
            System.out.println("[INIT] Users already exist, skipping initialization.");
            return;
        }

        s3Service.clearBucket();

        try (InputStream inputStream = new ClassPathResource(
                "data/static/user/users.json"
        ).getInputStream()) {

            UserInitData[] usersData = objectMapper.readValue(inputStream, UserInitData[].class);

            List<User> savedUsers = new ArrayList<>();
            for (UserInitData userData : usersData) {
                User user = userData.toUser();
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                User savedUser = userRepository.save(user);
                savedUsers.add(savedUser);
            }

            for (int i = 0; i < usersData.length; i++) {
                if (usersData[i].profilePhotoPath() != null && !usersData[i].profilePhotoPath().isBlank()) {
                    uploadAndAttachProfilePhoto(savedUsers.get(i), usersData[i].profilePhotoPath());
                }
            }

            System.out.println("[INIT] " + usersData.length + " users initialized.");
        }
    }

    /**
     * Uploads a profile photo from resources to S3 and attaches it to a user.
     *
     * @param user              the user to attach the photo to
     * @param profilePhotoPath  path to the photo in resources (e.g., "data/static/images/avatars/photo.jpg")
     */
    private void uploadAndAttachProfilePhoto(User user, String profilePhotoPath) {
        try {
            ClassPathResource resource = new ClassPathResource(profilePhotoPath);

            if (!resource.exists()) {
                System.out.println("[INIT] Profile photo not found: " + profilePhotoPath);
                return;
            }

            try (InputStream inputStream = resource.getInputStream()) {
                String originalFilename = resource.getFilename();

                if (originalFilename == null || originalFilename.isBlank()) {
                    System.out.println("[INIT] Could not determine filename for: " + profilePhotoPath);
                    return;
                }

                String s3Key = s3Helper.generateS3Key(originalFilename);
                String fileUrl = s3Helper.buildFileUrl(s3Key);

                byte[] fileContent = inputStream.readAllBytes();
                String contentType = getContentType(originalFilename);

                s3Service.uploadFile(
                        new ByteArrayMultipartFile(fileContent, originalFilename, contentType),
                        s3Key
                );

                ProfilePhoto profilePhoto = ProfilePhoto.create(
                        user.getId(),
                        new ByteArrayMultipartFile(fileContent, originalFilename, contentType),
                        fileUrl,
                        s3Key
                );

                profilePhotoRepository.save(profilePhoto);
                System.out.println("[INIT] Profile photo uploaded and attached to user: " + user.getEmail());
            }
        } catch (IOException e) {
            System.err.println("[INIT] Failed to upload profile photo for user " + user.getEmail() + ": " + e.getMessage());
        }
    }

    @NotNull
    private String getContentType(@NotNull String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        }
        return "application/octet-stream";
    }
}
