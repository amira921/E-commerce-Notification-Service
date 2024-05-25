package org.ecommerce.notification.asynchronous.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification.asynchronous.ScheduledNotificationService;
import org.ecommerce.notification.dto.*;
import org.ecommerce.notification.service.NotificationService;
import org.ecommerce.notification.util.dataFormatAdapterPattern.DataFormatAdapter;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduledEmailService implements ScheduledNotificationService {
    private final NotificationService emailService;
    private final DataFormatAdapter emailFormatAdapter;

    @Override
    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void resendFailedEmails() {
        for(NotificationDTO info: getFailedEmails()){
            String template = convertToHTML(info.getMessage());
            boolean isSent = resendEmail(info.getContact(),template);
            if(isSent)updateFailedEmailsInDB(info, NotificationStatus.SUCCESS);
            else updateFailedEmailsInDB(info, NotificationStatus.FAILED);
        }
    }

    private List<NotificationDTO> getFailedEmails(){
        return emailService.getFailedRecords();
    }

    private String convertToHTML(String emailInfo){
        return emailFormatAdapter.toHtmlTemplate(emailInfo);
    }

    private boolean resendEmail(String to, String content){
        return emailService.sendNotification(to,content);
    }

    private void updateFailedEmailsInDB(NotificationDTO record, NotificationStatus status){
        record.setTries(record.getTries() + 1);
        record.setStatus(status);
        if(status == NotificationStatus.SUCCESS) record.setReceived_at(new Date());
        emailService.updateRecordInDB(record);
    }
}
