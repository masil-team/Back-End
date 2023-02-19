package com.masil.domain.notification.service;

import com.masil.domain.member.entity.Member;
import com.masil.domain.notification.dto.NotificationDto;
import com.masil.domain.notification.entity.Notification;
import com.masil.domain.notification.entity.NotificationType;
import com.masil.domain.notification.repository.EmitterRepository;
import com.masil.domain.notification.repository.NotificationRepository;
import com.masil.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 3000L;

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;

    public SseEmitter createConnection(Long memberId, String lastEventId) {
        String emitterId = memberId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // 오류 및 시간초과 발생 시 emitter 제거
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // SseEmitter 의 유효 시간동안 어느 데이터도 전송되지 않는다면 503 에러 발생
        // 이를 방지하기 위한 더미 이벤트 전송
        sendNotification(emitter, emitterId, "EventStream Created. [memberId=" + memberId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

        return emitter;
    }

    @Transactional
    public void send(Member sender, Member receiver, NotificationDto notificationDto) {

        Notification notification = addNotification(sender, receiver, notificationDto);

        String receiverId = String.valueOf(receiver.getId());
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(receiverId);

        // 연결된 클라이언트가 없는 경우 EventCache 에 저장하고 연결된 클라이언트가 있는 경우 데이터 전송
        if (sseEmitters.isEmpty()) {
            sseEmitters.forEach((id, emitter) -> emitterRepository.saveEventCache(id, notification));
        } else {
            sseEmitters.forEach((id, emitter) -> sendNotification(emitter, id, notification));
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    /**
     * lastEventId 이전에 발생한 알람리스트 전송
     */
    private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithById(String.valueOf(memberId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, emitterId, entry.getValue()));
    }

    // TODO : send 할 때 id값 나중에 체크
    private void sendNotification(SseEmitter emitter, String emitterId, Object data) {
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
}
