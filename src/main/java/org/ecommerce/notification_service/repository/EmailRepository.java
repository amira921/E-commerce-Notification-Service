package org.ecommerce.notification_service.repository;

import org.ecommerce.notification_service.dto.EmailStatus;
import org.ecommerce.notification_service.entity.EmailDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmailRepository extends JpaRepository<EmailDetails,Integer> {
    List<EmailDetails> findByTriesLessThanAndStatus(int tries, EmailStatus status);

}
