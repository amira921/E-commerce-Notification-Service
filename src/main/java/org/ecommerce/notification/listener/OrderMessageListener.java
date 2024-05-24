package org.ecommerce.notification.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification.dto.EmailInfoDTO;
import org.ecommerce.notification.mapper.EmailInfoMapper;
import org.ecommerce.notification.service.NotificationService;
import org.ecommerce.notification.util.dataFormatAdapterPattern.DataFormatAdapter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrderMessageListener implements MessageListener {
    private final DataFormatAdapter emailFormatAdapter;
    private final NotificationService service;
    private final EmailInfoMapper mapper;

    @Override
    public void onMessage(Message message) {
        if(!isMessageValid(message)) return;
        EmailInfoDTO emailInfo = saveEmailInfo(message);
        sendEmailToCustomer(emailInfo);
    }

    private boolean isMessageValid(Message message){
        if (message.getBody().length < 0) {
            log.error("Message received but not valid");
            return false;
        }
        return true;
    }

    private EmailInfoDTO saveEmailInfo(Message message){
        return saveIntoDB(
                convertMessageToDomainObject(message)
        );
    }

    private void sendEmailToCustomer(EmailInfoDTO emailInfo){
        if (!sendEmail(emailInfo)) {
            log.info("Email failed to send. It will be retried at midnight.");
        }
        updateIntoDB(emailInfo);
    }

    private EmailInfoDTO convertMessageToDomainObject(Message message){
        return (EmailInfoDTO) emailFormatAdapter.toDomainObject(message);
    }

    private EmailInfoDTO saveIntoDB(EmailInfoDTO emailInfo){
        return (EmailInfoDTO) service.insertRecordInDB(emailInfo);
    }

    private EmailInfoDTO updateIntoDB(EmailInfoDTO emailInfo){
        return (EmailInfoDTO) service.updateRecordInDB(emailInfo);
    }

    private boolean sendEmail(EmailInfoDTO emailInfo){
        return service.sendNotification(emailInfo.getEmailTo(), emailInfo.getMessage().toString());
    }
}
