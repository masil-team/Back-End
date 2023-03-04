package com.masil.domain.storage.controller;

import com.masil.domain.storage.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    @PostMapping("/post-image")
    public ResponseEntity<String> upload(@RequestPart("file") MultipartFile multipartFile) {
        String url = awsS3Service.upload(multipartFile, "post-image");
        return ResponseEntity.ok(url);
    }
}
