package org.ecommerce.notification.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.*;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification.dto.*;
import org.ecommerce.notification.entity.EmailInfo;
import org.ecommerce.notification.mapper.EmailInfoMapper;
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
public class EmailService implements NotificationService<EmailInfoDTO> {
    @Value("${spring.mail.username}")
    private String from;

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final NotificationRepository repository;
    private final EmailInfoMapper mapper;
    private final EmailFormatAdapter emailFormatAdapter;

    public EmailService(TemplateEngine templateEngine, JavaMailSender mailSender,
                        NotificationRepository repository, EmailInfoMapper mapper,
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
    public EmailInfoDTO insertRecordInDB(EmailInfoDTO emailInfo) {
        emailInfo.setStatus(EmailStatus.FAILED);
        emailInfo.setCreated_at(new Date());
        emailInfo.setTries(0);
        EmailInfo record = repository.save(mapper.mapToEntity(emailInfo));
        return mapper.mapToDTO(record);
    }

    @Override
    public EmailInfoDTO updateRecordInDB(EmailInfoDTO emailInfo) {
        EmailInfo existingRecord = repository.findById(emailInfo.getId()).orElse(null);
        if(existingRecord == null){
            log.info("record not found with id:" + emailInfo.getId());
        }
        existingRecord.setStatus(EmailStatus.SUCCESS);
        existingRecord.setReceived_at(new Date());
        return mapper.mapToDTO(
                repository.save(existingRecord)
        );
    }

    @Override
    public List<EmailInfoDTO> getFailedRecords() {
        return repository.findByTriesLessThanAndStatus(3, EmailStatus.FAILED)
                .stream()
                .map(mapper::mapToDTO)
                .toList();
    }

    /*public boolean sendEmail(String to, String content){
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

    public EmailInfoDTO addEmailInfoToDB(EmailInfoDTO emailDetailsRecord) {
        emailDetailsRecord.setStatus(EmailStatus.FAILED);
        emailDetailsRecord.setCreated_at(new Date());
        emailDetailsRecord.setTries(0);
        EmailInfo record = repository.save(mapper.mapToEntity(emailDetailsRecord));
        return mapper.mapToDTO(record);
    }

    public EmailInfoDTO updateEmailInfoStatus(EmailInfoDTO updatedEmailRecord) {
        EmailInfo existingRecord = repository.findById(updatedEmailRecord.getId()).orElse(null);
        if(existingRecord == null){
            log.info("record not found with id:" + updatedEmailRecord.getId());
            //return;
        }
        existingRecord.setStatus(EmailStatus.SUCCESS);
        existingRecord.setReceived_at(new Date());
        return mapper.mapToDTO(
                repository.save(existingRecord)
        );
    }

    public List<EmailInfoDTO> getFailedEmails() {
        return repository.findByTriesLessThanAndStatus(3, EmailStatus.FAILED)
                .stream()
                .map(mapper::mapToDTO)
                .toList();
    }
    */

}
