package org.ecommerce.notification_service.repository;

import org.ecommerce.notification_service.dto.EmailStatus;
import org.ecommerce.notification_service.entity.EmailInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmailRepository extends JpaRepository<EmailInfo,Integer> {
    List<EmailInfo> findByTriesLessThanAndStatus(int tries, EmailStatus status);

}
