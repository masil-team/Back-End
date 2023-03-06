package com.masil.domain.notification.service;

import com.masil.domain.member.entity.Member;
import com.masil.domain.notification.dto.NotificationDto;
import com.masil.domain.notification.dto.NotificationsResponse;
import com.masil.domain.notification.entity.Notification;
import com.masil.domain.notification.exception.NotificationAccessDeniedException;
import com.masil.domain.notification.exception.NotificationNotFoundException;
import com.masil.domain.notification.repository.EmitterRepository;
import com.masil.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 1000L * 60 * 5; // sse 연결 시간, 임시로 5분
    private static final int NUM_LATEST_NOTIFICATION = 15;

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;

    /**
     * 클라이언트와 sse 연결
     */
    public SseEmitter createConnection(Long memberId) {
        String emitterId = memberId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // 오류 및 시간초과 발생 시 emitter 제거
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // SseEmitter 의 유효 시간동안 어느 데이터도 전송되지 않는다면 503 에러 발생
        // 이를 방지하기 위한 더미 이벤트 전송
        // + 읽지 않은 알림이 있는지 여부에 대한 결과값 전송(true/false)
        sendToClient(emitter, emitterId, isDisplay(memberId));

        return emitter;
    }

    /**
     * 클라이언트에 알람 전송
     */
    @Transactional
    public void send(Member sender, Member receiver, NotificationDto notificationDto) {

        addNotification(sender, receiver, notificationDto);

        String receiverId = String.valueOf(receiver.getId());
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(receiverId);

        // eventId 에 저장하고 연결된 클라이언트가 있는 경우 데이터 전송
        if (!sseEmitters.isEmpty()) {
            sseEmitters.forEach((id, emitter) -> sendToClient(emitter, id, true));
        }
    }

    /**
     * 최신 15개 알람 조회
     */
    @Transactional(readOnly = true)
    public NotificationsResponse findNotifications(Long receiverId) {
        List<Notification> notifications = notificationRepository.findTop15ByReceiverIdOrderByCreateDateDesc(receiverId);
        return NotificationsResponse.ofNotifications(notifications);
    }

    /**
     * 알람 읽음 처리
     * @return 알림 표시여부 반환
     */
    @Transactional
    public boolean readNotification(Long notificationId, Long memberId) {

        Notification notification = findNotificationById(notificationId);

        // 해당 알람을 읽을 권한이 있는지 체크
        validateOwner(memberId, notification);

        notification.read();
        
        return isDisplay(memberId);
    }

    /**
     * 알림 표시여부 체크
     *  - 최신 15개의 알람 중 안 읽은 알람이 하나라도 존재할 경우 : 1=true
     *  - 최신 15개의 모든 알람을 읽었을 경우 : 0=false
     */
    private boolean isDisplay(Long memberId) {
        return notificationRepository.existsTop15UnreadByReceiverId(memberId, NUM_LATEST_NOTIFICATION) == 1;
    }

    /**
     * 실제 클라이언트로 알람 전송하는 로직
     */
    private void sendToClient(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private Notification addNotification(Member sender, Member receiver, NotificationDto notificationDto) {
        Notification notification = Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .content(notificationDto.getContent())
                .url(notificationDto.getUrl())
                .notificationType(notificationDto.getNotificationType())
                .build();
        notificationRepository.save(notification);
        return notification;
    }

    private void validateOwner(Long memberId, Notification notification) {
        if (!notification.isOwner(memberId)) {
            throw new NotificationAccessDeniedException();
        }
    }

    private Notification findNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);
    }
}
