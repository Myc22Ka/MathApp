package pl.myc22ka.mathapp.s3.dto;

import pl.myc22ka.mathapp.s3.model.ImageType;

public record DeleteImageRequest(Long id, ImageType type) {
}
