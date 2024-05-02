package org.ecommerce.notification_service.service;

import org.ecommerce.notification_service.entity.EmailDetails;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface ScheduledEmailService {
    List<EmailDetails> getFailedEmails();
    void resendFailedNotifications();
}
