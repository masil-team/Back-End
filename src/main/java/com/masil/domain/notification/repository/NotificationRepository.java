package com.masil.domain.notification.repository;

import com.masil.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findTop15ByReceiverIdOrderByCreateDateDesc(Long receiverId);


    @Query(value = "SELECT count(n1.id) > 0 " +
            "FROM notification n1 INNER JOIN (" +
                "SELECT id FROM notification " +
                "WHERE receiver_id = :memberId " +
                "ORDER BY create_date DESC " +
                "LIMIT :limit" +
            ") n2 ON n1.id = n2.id " +
            "WHERE n1.is_read = false;", nativeQuery = true)
    int existsTop15UnreadByReceiverId(@Param("memberId") Long memberId, @Param("limit") int limit);

}
