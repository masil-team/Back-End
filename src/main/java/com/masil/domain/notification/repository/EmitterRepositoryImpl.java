package com.masil.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository{

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final List<String> eventIds = new CopyOnWriteArrayList<>();

    @Override
    public SseEmitter save(String id, SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
        return sseEmitter;
    }
    @Override
    public void saveEventId(String id) {
        eventIds.add(id);
    }

    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }
    @Override
    public void deleteByEventId(String id) {
        eventIds.remove(id);
    }

    @Override
    public List<String> findEventIdsStartWithById(String id) {
        return eventIds.stream()
                .filter(eventId -> eventId.startsWith(id))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, SseEmitter> findAllStartWithById(String id) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
