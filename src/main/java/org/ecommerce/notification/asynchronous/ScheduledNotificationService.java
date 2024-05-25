package org.ecommerce.notification.asynchronous;

import org.springframework.stereotype.Service;

@Service
public interface ScheduledNotificationService {
    void resendFailedEmails();
}
