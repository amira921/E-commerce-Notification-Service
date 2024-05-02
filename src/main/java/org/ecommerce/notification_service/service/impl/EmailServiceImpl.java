package org.ecommerce.notification_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.*;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification_service.dto.*;
import org.ecommerce.notification_service.entity.EmailDetails;
import org.ecommerce.notification_service.mapper.EmailMapper;
import org.ecommerce.notification_service.repository.EmailRepository;
import org.ecommerce.notification_service.service.EmailService;
import org.ecommerce.notification_service.util.DataConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.*;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String from;

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final DataConverter converter;
    private final EmailRepository emailRepository;
    private final EmailMapper emailMapper;

    public EmailServiceImpl(TemplateEngine templateEngine, JavaMailSender mailSender,
                            DataConverter converter, EmailRepository repository,
                            EmailMapper mapper){
        this.converter = converter;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.emailRepository = repository;
        this.emailMapper = mapper;
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
        log.info("Message converted from JsonNode to existing email template to be ready for sending.");
        return emailContent;
    }

    @Override
    public boolean sendEmail(String to, String content){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(to);
            helper.setSubject("Order Confirmation - Your Recent Purchase");
            helper.setFrom(new InternetAddress(from));
            helper.setText(content, true);
            mailSender.send(message);
            log.info(String.format("Mail send successfully to a customer."));
            return true;
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }catch(MailSendException e){
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public EmailDetails addEmailInfoToDB(EmailDetailsDTO emailDetailsRecord) {
        emailDetailsRecord.setStatus(EmailStatus.FAILED);
        emailDetailsRecord.setCreated_at(new Date());
        emailDetailsRecord.setTries(0);
        EmailDetails record = emailRepository.save(emailMapper.mapToEntity(emailDetailsRecord));
        log.info("new Email Details record added to database successfully");
        return record;
    }

    @Override
    public void updateEmailDetailsStatus(EmailDetails updatedEmailRecord) {
        EmailDetails existingRecord = emailRepository.findById(updatedEmailRecord.getId()).orElse(null);
        if(existingRecord == null){
            log.info("record not found with id:" + updatedEmailRecord.getId());
            return;
        }
        existingRecord.setStatus(EmailStatus.SUCCESS);
        existingRecord.setReceived_at(new Date());
        emailRepository.save(existingRecord);
        log.info("Email Details record updated successfully");
    }
}
