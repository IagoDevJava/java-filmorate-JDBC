package ru.yandex.practicum.filmorate.storage.feed;

public interface FeedStorage {

    void createFeedEntity(Long userId, Long entityId, String eventType, String operation);
}
