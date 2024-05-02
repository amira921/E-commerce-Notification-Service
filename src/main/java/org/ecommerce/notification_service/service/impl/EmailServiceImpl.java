package org.ecommerce.notification_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.*;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification_service.dto.OrderItem;
import org.ecommerce.notification_service.service.EmailService;
import org.ecommerce.notification_service.util.DataConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.List;


@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String from;

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final DataConverter converter;

    public EmailServiceImpl(TemplateEngine templateEngine,
                            JavaMailSender mailSender,
                            DataConverter converter){
        this.converter = converter;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public String createEmailContent(JsonNode node) {
        Context context = new Context();
        context.setVariable("customerName", node.get("customer").get("name").asText());
        context.setVariable("orderNumber", node.get("order").get("number").asText());
        context.setVariable("orderDate", node.get("order").get("date").asText());
        context.setVariable("billingAddress", node.get("order").get("billing").get("address").asText());
        context.setVariable("paymentMethod", node.get("order").get("billing").get("paymentMethod").asText());
        context.setVariable("totalAmount", node.get("order").get("billing").get("totalAmount").asText());
        context.setVariable("shippingAddress", node.get("order").get("shipping").get("address").asText());
        List<OrderItem> items = converter.convertJsonNodeToList(node.get("order").get("items"));
        context.setVariable("items", items);
        String emailContent = templateEngine.process("emailTemplate.html", context);
        log.info(String.format("Message converted from JsonNode to existing email template to be ready for sending."));
        return emailContent;
    }

    @Override
    public void sendEmail(String to, String content){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(to);
            helper.setSubject("Order Confirmation - Your Recent Purchase");
            helper.setFrom(new InternetAddress(from));
            helper.setText(content, true);
            mailSender.send(message);
            log.info(String.format("Mail send successfully to a customer."));
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }catch(MailSendException e){
            log.error(e.getMessage());
        }
    }
}
