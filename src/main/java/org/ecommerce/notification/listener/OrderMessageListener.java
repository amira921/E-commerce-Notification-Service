package org.ecommerce.notification.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification.dto.NotificationDTO;
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

    @Override
    public void onMessage(Message message) {
        if(!isMessageValid(message)) return;
        NotificationDTO emailInfo = saveEmailInfo(message);
        sendEmailToCustomer(emailInfo);
    }

    private boolean isMessageValid(Message message){
        if (message.getBody().length < 0) {
            log.error("Message received but not valid");
            return false;
        }
        return true;
    }

    private NotificationDTO saveEmailInfo(Message message){
        return saveIntoDB(
                convertMessageToDomainObject(message)
        );
    }

    private void sendEmailToCustomer(NotificationDTO emailInfo){
        if (!sendEmail(emailInfo)) {
            log.info("Email failed to send. It will be retried at midnight.");
        }
        updateIntoDB(emailInfo);
    }

    private NotificationDTO convertMessageToDomainObject(Message message){
        return (NotificationDTO) emailFormatAdapter.toDomainObject(message);
    }

    private NotificationDTO saveIntoDB(NotificationDTO emailInfo){
        return (NotificationDTO) service.insertRecordInDB(emailInfo);
    }

    private NotificationDTO updateIntoDB(NotificationDTO emailInfo){
        return (NotificationDTO) service.updateRecordInDB(emailInfo);
    }

    private boolean sendEmail(NotificationDTO emailInfo){
        return service.sendNotification(emailInfo.getContact(), emailInfo.getMessage().toString());
    }
}
