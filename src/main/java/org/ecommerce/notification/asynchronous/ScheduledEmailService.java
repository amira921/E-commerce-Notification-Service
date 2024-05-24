package org.ecommerce.notification.asynchronous;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification.dto.*;
import org.ecommerce.notification.service.NotificationService;
import org.ecommerce.notification.util.dataFormatAdapterPattern.DataFormatAdapter;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduledEmailService{
    private final NotificationService emailService;
    private final DataFormatAdapter emailFormatAdapter;

    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void resendFailedEmails() {
        for(EmailInfoDTO info: getFailedEmails()){
            String template = convertToHTML(info.getMessage());
            boolean isSent = resendEmail(info.getEmailTo(),template);
            if(isSent)updateFailedEmailsInDB(info,EmailStatus.SUCCESS);
            else updateFailedEmailsInDB(info,EmailStatus.FAILED);
        }
    }

    private List<EmailInfoDTO> getFailedEmails(){
        return emailService.getFailedRecords();
    }

    private String convertToHTML(String emailInfo){
        return emailFormatAdapter.toHtmlTemplate(emailInfo);
    }

    private boolean resendEmail(String to, String content){
        return emailService.sendNotification(to,content);
    }

    private void updateFailedEmailsInDB(EmailInfoDTO record, EmailStatus status){
        record.setTries(record.getTries() + 1);
        record.setStatus(status);
        if(status == EmailStatus.SUCCESS) record.setReceived_at(new Date());
        emailService.updateRecordInDB(record);
    }
}
