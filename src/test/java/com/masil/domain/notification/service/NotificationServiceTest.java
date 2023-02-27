package com.masil.domain.notification.service;

import com.masil.common.annotation.ServiceTest;
import com.masil.domain.address.entity.EmdAddress;
import com.masil.domain.address.repository.EmdAddressRepository;
import com.masil.domain.board.repository.BoardRepository;
import com.masil.domain.fixture.MemberFixture;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.notification.dto.NotificationResponse;
import com.masil.domain.notification.dto.NotificationsResponse;
import com.masil.domain.notification.entity.Notification;
import com.masil.domain.notification.entity.NotificationType;
import com.masil.domain.notification.repository.NotificationRepository;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.postlike.service.PostLikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static com.masil.domain.fixture.PostFixture.일반_게시글_JJ;
import static org.assertj.core.api.Assertions.assertThat;

class NotificationServiceTest extends ServiceTest {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostLikeService postLikeService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private EmdAddressRepository emdAddressRepository;


    @Test
    @DisplayName("클라이언트의 알림 연결을 성공한다.")
    void createConnection_success() {
        //given
        Member member = MemberFixture.일반_회원_JJ.엔티티_생성();
        String lastEventId = "";

        // when
        SseEmitter subscribe = notificationService.createConnection(member.getId(), lastEventId);

        // then
        assertThat(subscribe).isNotNull();
    }


    @Test
    @DisplayName("알림 전송을 성공한다.")
    void send_success() {
        /**
         * KK가 JJ의 게시글을 좋아요를 할 경우
         * JJ는 알림을 받는다.
         */

        // given
        EmdAddress emdAddress = emdAddressRepository.findById(11110111).get();

        Post post = 일반_게시글_JJ.엔티티_생성(emdAddress);
        Member JJ = memberRepository.save(post.getMember());
        boardRepository.save(post.getBoard());
        postRepository.save(post);

        Member KK = MemberFixture.일반_회원_KK.엔티티_생성();
        memberRepository.save(KK);

        // when
        postLikeService.toggleLikePost(post.getId(), KK.getId());

        // then
        Notification notification = notificationRepository.findById(1L).get();

        assertThat(notification.getNotificationType()).isEqualTo(NotificationType.POST_LIKE);
    }

    @Test
    @DisplayName("알림 목록 조회를 성공한다.")
    void findNotifications_success() {
        /**
         * KK가 JJ의 게시글을 좋아요를 할 경우
         * JJ는 알림을 받는다.
         */

        // given
        EmdAddress emdAddress = emdAddressRepository.findById(11110111).get();

        Post post = 일반_게시글_JJ.엔티티_생성(emdAddress);
        Member JJ = memberRepository.save(post.getMember());
        boardRepository.save(post.getBoard());
        postRepository.save(post);

        Member KK = MemberFixture.일반_회원_KK.엔티티_생성();
        memberRepository.save(KK);

        postLikeService.toggleLikePost(post.getId(), KK.getId());

        // when
        NotificationsResponse notifications = notificationService.findNotifications(JJ.getId());
        List<NotificationResponse> notificationResponses = notifications.getNotificationResponses();
        NotificationResponse notificationResponse = notificationResponses.get(0);

        // then
        assertThat(notificationResponse.getIsRead()).isFalse();
        assertThat(notificationResponse.getId()).isEqualTo(1L);
        assertThat(notificationResponse.getSender().getId()).isEqualTo(KK.getId());
    }

    @Test
    @DisplayName("알림 목록 조회를 읽음 처리한다.")
    void readNotification_success() {
        /**
         * KK가 JJ의 게시글을 좋아요를 할 경우
         * JJ는 알림을 받는다.
         * 해당 알림을 읽은 처리 한다.
         */

        // given
        EmdAddress emdAddress = emdAddressRepository.findById(11110111).get();

        Post post = 일반_게시글_JJ.엔티티_생성(emdAddress);
        Member JJ = memberRepository.save(post.getMember());
        boardRepository.save(post.getBoard());
        postRepository.save(post);

        Member KK = MemberFixture.일반_회원_KK.엔티티_생성();
        memberRepository.save(KK);

        postLikeService.toggleLikePost(post.getId(), KK.getId());

        // when
        boolean isDisplay = notificationService.readNotification(1L, JJ.getId());

        // then
        Notification notification = notificationRepository.findById(1L).get();
        assertThat(notification.getIsRead()).isTrue();
        assertThat(isDisplay).isFalse();
    }

}