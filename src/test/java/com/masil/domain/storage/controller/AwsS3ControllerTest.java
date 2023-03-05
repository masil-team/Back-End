package com.masil.domain.storage.controller;

import com.masil.common.annotation.ControllerMockApiTest;
import com.masil.domain.postFile.service.PostFileService;
import com.masil.domain.storage.dto.FileInfoDto;
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
                "test.jpg",
                "image/jpg",
                new FileInputStream("src/test/resources/image/test.jpg"));

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
    private ResultActions requestUpload(String url, MockMultipartFile multipartFile) throws Exception {
        return mockMvc.perform(multipart(url)
                        .file(multipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE))
                .andDo(print());
    }
}