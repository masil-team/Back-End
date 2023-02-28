package com.masil.domain.notification.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String id, SseEmitter sseEmitter);
    
    void saveEventId(String id);
    
    void deleteById(String id);

    void deleteByEventId(String id);

    List<String> findEventIdsStartWithById(String id);

    Map<String, SseEmitter> findAllStartWithById(String id);
}
