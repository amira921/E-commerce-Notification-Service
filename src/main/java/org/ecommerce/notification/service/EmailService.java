package org.ecommerce.notification.service;

import org.ecommerce.notification.dto.EmailInfoDTO;
import org.ecommerce.notification.entity.EmailInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmailService {
    boolean sendEmail(String to, String content);
    EmailInfoDTO addEmailInfoToDB(EmailInfoDTO emailDetailsRecord);
    EmailInfoDTO updateEmailInfoStatus(EmailInfoDTO updatedEmailInfoRecord);
    List<EmailInfoDTO> getFailedEmails();
}
