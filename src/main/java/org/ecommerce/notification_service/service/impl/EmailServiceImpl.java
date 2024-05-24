package org.ecommerce.notification_service.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.*;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification_service.dto.*;
import org.ecommerce.notification_service.entity.EmailInfo;
import org.ecommerce.notification_service.mapper.EmailMapper;
import org.ecommerce.notification_service.repository.EmailRepository;
import org.ecommerce.notification_service.service.EmailService;
import org.ecommerce.notification_service.util.dataFormatAdapterPattern.EmailFormatAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import java.util.*;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String from;

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final EmailRepository repository;
    private final EmailMapper emailMapper;
    private final EmailFormatAdapter emailFormatAdapter;

    public EmailServiceImpl(TemplateEngine templateEngine, JavaMailSender mailSender,
                            EmailRepository repository, EmailMapper mapper,
                            EmailFormatAdapter emailFormatAdapter){
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.repository = repository;
        this.emailMapper = mapper;
        this.emailFormatAdapter = emailFormatAdapter;
    }

    @Override
    public boolean sendEmail(String to, String content){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(to);
            helper.setSubject("Order Confirmation - Your Recent Purchase");
            helper.setFrom(new InternetAddress(from));
            helper.setText(emailFormatAdapter.toHtmlContent(content), true);
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
    public EmailInfo addEmailInfoToDB(EmailInfoDTO emailDetailsRecord) {
        emailDetailsRecord.setStatus(EmailStatus.FAILED);
        emailDetailsRecord.setCreated_at(new Date());
        emailDetailsRecord.setTries(0);
        EmailInfo record = repository.save(emailMapper.mapToEntity(emailDetailsRecord));
        log.info("new Email Details record added to database successfully");
        return record;
    }

    @Override
    public void updateEmailDetailsStatus(EmailInfo updatedEmailRecord) {
        EmailInfo existingRecord = repository.findById(updatedEmailRecord.getId()).orElse(null);
        if(existingRecord == null){
            log.info("record not found with id:" + updatedEmailRecord.getId());
            return;
        }
        existingRecord.setStatus(EmailStatus.SUCCESS);
        existingRecord.setReceived_at(new Date());
        repository.save(existingRecord);
        log.info("Email Details record updated successfully");
    }

    public List<EmailInfo> getFailedEmails() {
        return repository.findByTriesLessThanAndStatus(3,EmailStatus.FAILED);
    }
}
