package org.ecommerce.notification_service.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification_service.service.impl.EmailServiceImpl;
import org.ecommerce.notification_service.util.DataConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RabbitMQConsumer implements MessageListener {
    private final DataConverter consumerUtil;
    private final EmailServiceImpl emailService;

    /**
     * This method processes the incoming message by logging its content,
     * extracting relevant information, and performing actions such as
     * adding email details to the database, creating email content,
     * sending emails, and update email details in database after sending successfully
     *
     * @param message The incoming message from RabbitMQ.
     */
    @Override
    public void onMessage(Message message) {
        log.info(String.format("Message received from RabbitMQ successfully.\nMessage:\nre%s\n" , new String(message.getBody())));
        JsonNode messageNode = consumerUtil.convertMessageBodyToJsonNode(message);
        String emailContent = emailService.createEmailContent(messageNode);

        String customerEmail = messageNode.get("customer").get("email").asText();
        emailService.sendEmail(customerEmail, emailContent);
    }
}
