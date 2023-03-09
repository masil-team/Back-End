package com.masil.domain.postFile.service;

import com.masil.domain.postFile.entity.PostFile;
import com.masil.domain.postFile.repository.PostFileRepository;
import com.masil.domain.storage.dto.FileInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostFileService {

    private final PostFileRepository postFileRepository;

    @Transactional
    public Long createPostFile(MultipartFile multipartFile, FileInfoDto fileInfoDto) {

        PostFile postFile = PostFile.builder()
                .post(null)
                .name(fileInfoDto.getS3Name())
                .originName(multipartFile.getOriginalFilename())
                .byteSize((int) multipartFile.getSize())
                .width(fileInfoDto.getWidth())
                .height(fileInfoDto.getHeight())
                .build();

        return postFileRepository.save(postFile).getId();
    }
}
