package pl.myc22ka.mathapp.s3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.myc22ka.mathapp.s3.service.S3Service;

import java.io.IOException;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadImage(@RequestParam("file") MultipartFile file) {
        s3Service.uploadFile(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws IOException {
        byte[] imageData = s3Service.downloadFile(fileName);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
    }
}

