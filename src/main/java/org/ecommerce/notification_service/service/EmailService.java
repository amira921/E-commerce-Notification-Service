package org.ecommerce.notification_service.service;

import org.ecommerce.notification_service.dto.EmailInfoDTO;
import org.ecommerce.notification_service.entity.EmailInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmailService {
    boolean sendEmail(String to, String content);
    EmailInfo addEmailInfoToDB(EmailInfoDTO emailDetailsRecord);
    void updateEmailDetailsStatus(EmailInfo updatedEmailInfoRecord);
    List<EmailInfo> getFailedEmails();
}
