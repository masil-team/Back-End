package com.masil.domain.notification.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    void deleteById(String id);

    Map<String, Object> findAllEventCacheStartWithByEmitterId(String emitterId);
}
