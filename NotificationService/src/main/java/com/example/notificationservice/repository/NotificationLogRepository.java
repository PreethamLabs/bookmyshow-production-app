package com.example.notificationservice.repository;

import com.example.notificationservice.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository
        extends JpaRepository<NotificationLog, Long> {

    boolean existsByEventIdAndChannel(
            String eventId,
            String channel
    );
}
