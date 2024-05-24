package org.ecommerce.notification.repository;

import org.ecommerce.notification.dto.EmailStatus;
import org.ecommerce.notification.entity.EmailInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmailRepository extends JpaRepository<EmailInfo,Integer> {
    List<EmailInfo> findByTriesLessThanAndStatus(int tries, EmailStatus status);

}
