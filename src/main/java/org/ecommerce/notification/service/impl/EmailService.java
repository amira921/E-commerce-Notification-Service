package org.ecommerce.notification.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.*;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification.dto.*;
import org.ecommerce.notification.entity.Notification;
import org.ecommerce.notification.mapper.NotificationMapper;
import org.ecommerce.notification.repository.NotificationRepository;
import org.ecommerce.notification.service.NotificationService;
import org.ecommerce.notification.util.dataFormatAdapterPattern.EmailFormatAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import java.util.*;

@Service
@Slf4j
public class EmailService implements NotificationService<NotificationDTO> {
    @Value("${spring.mail.username}")
    private String from;

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final EmailFormatAdapter emailFormatAdapter;

    public EmailService(TemplateEngine templateEngine, JavaMailSender mailSender,
                        NotificationRepository repository, NotificationMapper mapper,
                        EmailFormatAdapter emailFormatAdapter){
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.repository = repository;
        this.mapper = mapper;
        this.emailFormatAdapter = emailFormatAdapter;
    }

    @Override
    public boolean sendNotification(String to, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(new InternetAddress(from));
            helper.setTo(to);
            helper.setSubject("Order Confirmation - Your Recent Purchase");
            helper.setText(emailFormatAdapter.toHtmlTemplate(content), true);
            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }catch(MailSendException e){
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public NotificationDTO insertRecordInDB(NotificationDTO emailInfo) {
        emailInfo.setStatus(NotificationStatus.FAILED);
        emailInfo.setCreated_at(new Date());
        emailInfo.setTries(0);
        Notification record = repository.save(mapper.mapToEntity(emailInfo));
        return mapper.mapToDTO(record);
    }

    @Override
    public NotificationDTO updateRecordInDB(NotificationDTO emailInfo) {
        Notification existingRecord = repository.findById(emailInfo.getId()).orElse(null);
        if(existingRecord == null){
            log.info("record not found with id:" + emailInfo.getId());
        }
        existingRecord.setStatus(NotificationStatus.SUCCESS);
        existingRecord.setReceived_at(new Date());
        return mapper.mapToDTO(
                repository.save(existingRecord)
        );
    }

    @Override
    public List<NotificationDTO> getFailedRecords() {
        return repository.findByTriesLessThanAndStatus(3, NotificationStatus.FAILED)
                .stream()
                .map(mapper::mapToDTO)
                .toList();
    }
}
