package org.ecommerce.notification_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendEmail(String to, String content);
    String createEmailContent(JsonNode node);

}
