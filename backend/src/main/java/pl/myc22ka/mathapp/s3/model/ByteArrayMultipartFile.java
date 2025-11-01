package pl.myc22ka.mathapp.s3.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Helper class to convert byte[] to MultipartFile for S3Service compatibility.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
public final class ByteArrayMultipartFile implements MultipartFile {
    private final byte[] content;
    private final String filename;
    private final String contentType;

    /**
     * Constructs a ByteArrayMultipartFile with the given content, filename, and content type.
     *
     * @param content     the byte array content of the file
     * @param filename    the name of the file
     * @param contentType the MIME type of the file
     */
    public ByteArrayMultipartFile(byte[] content, String filename, String contentType) {
        this.content = content;
        this.filename = filename;
        this.contentType = contentType;
    }

    @NotNull
    @Override
    public String getName() {
        return filename;
    }

    @Override
    public String getOriginalFilename() {
        return filename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @NotNull
    @Override
    public byte[] getBytes() {
        return content;
    }

    @NotNull
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(@NotNull File destination) throws IOException, IllegalStateException {
        Files.write(destination.toPath(), content);
    }

    @Override
    public void transferTo(@NotNull Path path) throws IOException, IllegalStateException {
        Files.write(path, content);
    }
}
