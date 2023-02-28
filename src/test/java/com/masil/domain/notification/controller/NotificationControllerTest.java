package com.masil.domain.notification.controller;

import com.masil.common.annotation.ControllerMockApiTest;
import com.masil.domain.member.dto.response.MemberResponse;
import com.masil.domain.notification.dto.NotificationResponse;
import com.masil.domain.notification.dto.NotificationsResponse;
import com.masil.domain.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        NotificationController.class,
})
@AutoConfigureMockMvc(addFilters = false)
public class NotificationControllerTest extends ControllerMockApiTest {

    @MockBean
    private NotificationService notificationService;

    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    private static final MemberResponse MEMBER_RESPONSE = MemberResponse.builder()
            .id(1L)
            .nickname("닉네임1")
            .build();

    @Test
    @DisplayName("sse 연결을 성공한다.")
    void createConnection() throws Exception {

        // given
        given(notificationService.createConnection(any())).willReturn(new SseEmitter());

        // when
        ResultActions resultActions = requestCreateConnection("/sse");

        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("notification/sse",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("알람 조회 요청을 성공한다.")
    void findNotifications() throws Exception {

        // given
        List<NotificationResponse> notifications = new ArrayList<>();
        notifications.add(NotificationResponse.builder()
                .id(1L)
                .sender(MEMBER_RESPONSE)
                .content("님이 게시글내용어쩌구저쩌구...에 좋아요를 하였습니다.")
                .url("/post/1")
                .isRead(false)
                .createDate(LocalDateTime.now())
                .build());
        NotificationsResponse notificationsResponse = new NotificationsResponse(notifications);
        given(notificationService.findNotifications(any())).willReturn(notificationsResponse);

        // when
        ResultActions resultActions = requestFindNotifications("/notifications");

        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("notification/find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("notifications.[].id").description("알람 id"),
                                fieldWithPath("notifications.[].sender.id").description("발신자 id"),
                                fieldWithPath("notifications.[].sender.nickname").description("발신자 닉네임"),
                                fieldWithPath("notifications.[].content").description("알람 내용"),
                                fieldWithPath("notifications.[].url").description("발생된 url"),
                                fieldWithPath("notifications.[].isRead").description("알람 읽음 여부"),
                                fieldWithPath("notifications.[].createDate").description("생성 날짜")
                        )
                ));
    }

    @Test
    @DisplayName("알람 읽음 요청을 성공한다.")
    void readNotification() throws Exception {

        // given
        given(notificationService.readNotification(any(), any())).willReturn(true);

        // when
        ResultActions resultActions = requestReadNotification("/notifications/1");

        // then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andDo(document("notification/read",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("isDisplay").description("알람 표시 여부")
                        )
                ));
    }
    private ResultActions requestCreateConnection(String url) throws Exception {
        return mockMvc.perform(get(url)
                        .accept(MediaType.TEXT_EVENT_STREAM)
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE))
                .andDo(print());
    }

    private ResultActions requestFindNotifications(String url) throws Exception {
        return mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE))
                .andDo(print());
    }

    private ResultActions requestReadNotification(String url) throws Exception {
        return mockMvc.perform(patch(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE))
                .andDo(print());
    }
}
