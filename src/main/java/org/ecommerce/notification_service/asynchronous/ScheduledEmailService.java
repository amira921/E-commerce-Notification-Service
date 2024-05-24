package org.ecommerce.notification_service.asynchronous;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification_service.dto.EmailStatus;
import org.ecommerce.notification_service.entity.EmailInfo;
import org.ecommerce.notification_service.service.impl.EmailServiceImpl;
import org.ecommerce.notification_service.util.dataFormatAdapterPattern.EmailFormatAdapter;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduledEmailService{
    private final EmailServiceImpl emailService;
    private final EmailFormatAdapter emailFormatAdapter;

    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void resendFailedNotifications() {
        log.info("Scheduled Task started...\nResend Failed Emails...");
        for (EmailInfo record : emailService.getFailedEmails()) {
            EmailInfo emailInfoRecord = record;
            String emailContent = emailFormatAdapter.toHtmlContent(emailInfoRecord.getMessage());

            boolean isSent = emailService.sendEmail(emailInfoRecord.getEmailTo(), emailContent);
            if(isSent) updateFailedEmailsInDB(emailInfoRecord, EmailStatus.SUCCESS);
            else updateFailedEmailsInDB(emailInfoRecord, EmailStatus.FAILED);
            log.info("Scheduled Task completed");
        }
    }

    private void updateFailedEmailsInDB(EmailInfo record, EmailStatus status){
        record.setTries(record.getTries() + 1);
        record.setStatus(status);
        if(status == EmailStatus.SUCCESS) record.setReceived_at(new Date());
        emailService.updateEmailDetailsStatus(record);
    }
}
