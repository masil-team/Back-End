package com.masil.domain.storage.service;

import com.masil.domain.storage.exception.FileNotFoundException;
import com.masil.domain.storage.exception.InvalidImageExtensionException;
import com.masil.domain.storage.exception.InvalidImageFileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class AwsS3ServiceTest {

    @Autowired
    private AwsS3Service awsS3Service;

    private static final String IMAGE_PATH = "src/test/resources/image/";    // 이미지 저장 경로
    private static final String VALID_IMAGE_NAME = "test.jpg";               // 유효한 이미지 이름
    private static final String INVALID_IMAGE_NAME = "txt.jpg";              // 유효하지 않은 이미지 이름
    @Nested
    @DisplayName("multipartFile가 ")
    class GetValidBufferedImage {

        @Test
        @DisplayName("유효한 이미지 파일이면 BufferedImage을 반환한다.")
        void success() throws IOException {
            MockMultipartFile multipartFile = new MockMultipartFile("file",
                    VALID_IMAGE_NAME,
                    "image/jpg",
                    new FileInputStream(IMAGE_PATH + VALID_IMAGE_NAME));

            BufferedImage bufferedImage = awsS3Service.getValidBufferedImage(multipartFile);
            assertThat(bufferedImage).isNotNull();
        }

        @DisplayName("확장자는 jpg이지만 유효하지 않은 파일일 경우 InvalidImageFileException 예외를 반환한다.")
        @Test
        void invalidFile() throws IOException {
            MockMultipartFile multipartFile = new MockMultipartFile("file",
                    INVALID_IMAGE_NAME,
                    "image/jpg",
                    new FileInputStream(IMAGE_PATH + INVALID_IMAGE_NAME));

            assertThatThrownBy(() -> awsS3Service.getValidBufferedImage(multipartFile))
                    .isInstanceOf(InvalidImageFileException.class);
        }

        @DisplayName("이미지 확장자가 아닐 경우 InvalidImageExtensionException 예외를 반환한다.")
        @Test
        void invalidExtension() throws IOException {
            MockMultipartFile multipartFile = new MockMultipartFile("file",
                    VALID_IMAGE_NAME,
                    "image/txt",
                    new FileInputStream(IMAGE_PATH + VALID_IMAGE_NAME));

            assertThatThrownBy(() -> awsS3Service.getValidBufferedImage(multipartFile))
                    .isInstanceOf(InvalidImageExtensionException.class);
        }

        @DisplayName("null일 경우 FileNotFoundException 예외를 반환한다.")
        @Test
        void nullFile() {
            assertThatThrownBy(() -> awsS3Service.getValidBufferedImage(null))
                    .isInstanceOf(FileNotFoundException.class);
        }
    }

}