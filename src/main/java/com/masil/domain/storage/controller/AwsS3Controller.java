package com.masil.domain.storage.controller;

import com.masil.domain.postFile.service.PostFileService;
import com.masil.domain.storage.dto.FileInfoDto;
import com.masil.domain.storage.dto.FileInfoResponse;
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
    private final PostFileService postFileService;

    @PostMapping("/post-image")
    public ResponseEntity<FileInfoResponse> upload(@RequestPart("file") MultipartFile multipartFile) {
        FileInfoDto fileInfoDto = awsS3Service.upload(multipartFile, "post-image");
        Long postFileId = postFileService.createPostFile(multipartFile, fileInfoDto);
        return ResponseEntity.ok(FileInfoResponse.of(postFileId, fileInfoDto.getUrl()));
    }
}
