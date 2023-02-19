package com.masil.domain.notification.repository;

import com.masil.domain.notification.entity.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String id, SseEmitter sseEmitter);
    
    void saveEventCache(String id, Object data);
    
    void deleteById(String id);
    
    Map<String, Object> findAllEventCacheStartWithById(String id);

    Map<String, SseEmitter> findAllStartWithById(String id);
}
