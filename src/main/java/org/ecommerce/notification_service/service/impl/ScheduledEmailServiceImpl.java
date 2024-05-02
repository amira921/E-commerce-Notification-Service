package org.ecommerce.notification_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification_service.dto.EmailStatus;
import org.ecommerce.notification_service.entity.EmailDetails;
import org.ecommerce.notification_service.repository.EmailRepository;
import org.ecommerce.notification_service.service.ScheduledEmailService;
import org.ecommerce.notification_service.util.DataConverter;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduledEmailServiceImpl implements ScheduledEmailService {
    private final EmailRepository repository;
    private final EmailServiceImpl emailService;
    private final DataConverter converter;


    /**
     * Asynchronous scheduled task to resend failed email notifications.
     *
     * This method execute asynchronously, and it is scheduled to run daily
     * at midnight using the cron expression.
     * The method retrieves failed email records from the database based on
     * status is failed and tries is less than 3, processes each record,
     * and attempts to resend the email. If the resend is successful,
     * the email status is updated to EmailStatus.SUCCESS.
     * If the resend fails, the email status is updated to EmailStatus.FAILED
     * and in each case, the records in database updated.
     *
     * @throws MailSendException If there is an exception while sending the email.
     *                           In case of failure, the email status is updated to FAILED in database.
     */
    @Async
    @Scheduled(cron = "0 42 20 * * *")
    @Override
    public void resendFailedNotifications() {
        log.info("Scheduled Task started...\nResend Failed Emails...");
        EmailDetails emailDetailsRecord = null;
        try{
            for (EmailDetails record : getFailedEmails()) {
                emailDetailsRecord = record;
                JsonNode node = converter.ConvertStringToJsonNode(emailDetailsRecord.getMessage());
                String emailContent = emailService.createEmailContent(node);
                emailService.sendEmail(emailDetailsRecord.getEmailTo(), emailContent);
                updateFailedEmailsInDB(emailDetailsRecord, EmailStatus.SUCCESS);
                log.info("Scheduled Task completed");
            }
        } catch (MailSendException e) {
            updateFailedEmailsInDB(emailDetailsRecord, EmailStatus.FAILED);
            log.info("Email failed to send to the customer again" + e.getMessage());
        }
    }

    @Override
    public List<EmailDetails> getFailedEmails() {
        return repository.findByTriesLessThanAndStatus(3,EmailStatus.FAILED);
    }

    private void updateFailedEmailsInDB(EmailDetails record, EmailStatus status){
        record.setTries(record.getTries() + 1);
        record.setStatus(status);
        record.setReceived_at(new Date());
        emailService.updateEmailDetailsStatus(record);
    }
}
