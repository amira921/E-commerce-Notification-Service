package org.ecommerce.notification_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.ecommerce.notification_service.dto.EmailDetailsDTO;
import org.ecommerce.notification_service.entity.EmailDetails;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    boolean sendEmail(String to, String content);
    String createEmailContent(JsonNode node);
    EmailDetails addEmailInfoToDB(EmailDetailsDTO emailDetailsRecord);
    void updateEmailDetailsStatus(EmailDetails updatedEmailDetailsRecord);
}
