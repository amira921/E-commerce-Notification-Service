package org.ecommerce.notification.repository;

import org.ecommerce.notification.dto.NotificationStatus;
import org.ecommerce.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    List<Notification> findByTriesLessThanAndStatus(int tries, NotificationStatus status);
}
