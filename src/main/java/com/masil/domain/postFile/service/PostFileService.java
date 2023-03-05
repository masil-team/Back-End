package com.masil.domain.postFile.service;

import com.masil.domain.postFile.entity.PostFile;
import com.masil.domain.postFile.repository.PostFileRepository;
import com.masil.domain.storage.dto.FileInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PostFileService {

    private final PostFileRepository postFileRepository;

    @Transactional
    public Long createPostFile(MultipartFile multipartFile, FileInfoDto fileInfoDto) {

        int width = 0;
        int height = 0;

        // 이미지 높이 너비 추출
        try {
            BufferedImage read = ImageIO.read(multipartFile.getInputStream());
            width = read.getWidth();
            height = read.getHeight();
        } catch (IOException e) {
            // TODO : 추주 예외 변경
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }

        PostFile postFile = PostFile.builder()
                .post(null)
                .name(fileInfoDto.getS3Name())
                .originName(multipartFile.getOriginalFilename())
                .byteSize((int) multipartFile.getSize())
                .width(width)
                .height(height)
                .build();

        return postFileRepository.save(postFile).getId();
    }
}
