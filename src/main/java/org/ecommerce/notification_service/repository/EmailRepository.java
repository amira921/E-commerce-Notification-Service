package org.ecommerce.notification_service.repository;

import org.ecommerce.notification_service.entity.EmailDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailDetails,Integer> {
}
