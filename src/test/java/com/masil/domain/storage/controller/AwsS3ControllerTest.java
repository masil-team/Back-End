package com.masil.domain.storage.controller;

import com.masil.common.annotation.ControllerMockApiTest;
import com.masil.domain.postFile.service.PostFileService;
import com.masil.domain.storage.dto.FileInfoDto;
import com.masil.domain.storage.exception.InvalidImageExtensionException;
import com.masil.domain.storage.exception.InvalidImageFileException;
import com.masil.domain.storage.service.AwsS3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.io.FileInputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        AwsS3Controller.class,
})
@AutoConfigureMockMvc(addFilters = false)
class AwsS3ControllerTest extends ControllerMockApiTest {

    @MockBean
    private AwsS3Service awsS3Service;
    @MockBean
    private PostFileService postFileService;

    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    private static final String TEST_URL = "https://hello.net/post-image/hello.jpg";
    private static final String IMAGE_PATH = "src/test/resources/image/";    // 이미지 저장 경로
    private static final String VALID_IMAGE_NAME = "test.jpg";               // 유효한 이미지 이름
    private static final String INVALID_IMAGE_NAME = "txt.jpg";              // 유효하지 않은 이미지 이름

    @Test
    @DisplayName("이미지를 s3에 성공적으로 업로드한다.")
    void upload_success() throws Exception {

        // given
        FileInfoDto fileInfoDto = FileInfoDto.builder()
                .url(TEST_URL)
                .s3Name("/post-image/hello.jpg")
                .build();
        given(awsS3Service.upload(any(), any())).willReturn(fileInfoDto);
        given(postFileService.createPostFile(any(), any())).willReturn(1L);

        MockMultipartFile multipartFile = new MockMultipartFile("file",
                VALID_IMAGE_NAME,
                "image/jpg",
                new FileInputStream(IMAGE_PATH + VALID_IMAGE_NAME));

        // when
        ResultActions resultActions = requestUpload("/s3/post-image", multipartFile);

        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("s3/post-image",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").description("파일 아이디"),
                                fieldWithPath("url").description("이미지 주소")
                        )
                ));
    }

    @Test
    @DisplayName("이미지 파일이 아닌 경우 InvalidImageFileException 예외를 반환한다")
    void upload_InvalidImageFileException() throws Exception {

        // given
        given(awsS3Service.upload(any(), any())).willThrow(new InvalidImageFileException());

        MockMultipartFile multipartFile = new MockMultipartFile("file",
                INVALID_IMAGE_NAME,
                "image/jpg",
                new FileInputStream(IMAGE_PATH + INVALID_IMAGE_NAME));

        // when
        ResultActions resultActions = requestUpload("/s3/post-image", multipartFile);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andDo(document("s3/post-image/invalidImageFile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("message").description("에러 메세지"),
                                fieldWithPath("errors").description("에러 종류"),
                                fieldWithPath("code").description("코드")
                        )
                ));
    }

    @Test
    @DisplayName("이미지 확장자가 아닌 경우 InvalidImageExtensionException 예외를 반환한다")
    void upload_InvalidImageExtensionException() throws Exception {

        // given
        given(awsS3Service.upload(any(), any())).willThrow(new InvalidImageExtensionException());

        MockMultipartFile multipartFile = new MockMultipartFile("file",
                VALID_IMAGE_NAME,
                "image/txt",
                new FileInputStream(IMAGE_PATH + VALID_IMAGE_NAME));

        // when
        ResultActions resultActions = requestUpload("/s3/post-image", multipartFile);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andDo(document("s3/post-image/invalidImageExtension",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("message").description("에러 메세지"),
                                fieldWithPath("errors").description("에러 종류"),
                                fieldWithPath("code").description("코드")
                        )
                ));
    }

    private ResultActions requestUpload(String url, MockMultipartFile multipartFile) throws Exception {
        return mockMvc.perform(multipart(url)
                        .file(multipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE))
                .andDo(print());
    }
}