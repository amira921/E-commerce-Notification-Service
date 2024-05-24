package org.ecommerce.notification_service.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification_service.dto.EmailInfoDTO;
import org.ecommerce.notification_service.entity.EmailInfo;
import org.ecommerce.notification_service.service.impl.EmailServiceImpl;
import org.ecommerce.notification_service.util.dataFormatAdapterPattern.DataFormatAdapter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrderMessageListener implements MessageListener {
    private final DataFormatAdapter emailFormatAdapter;
    private final EmailServiceImpl emailService;
    @Override
    public void onMessage(Message message) {
        log.info(String.format("Message received from RabbitMQ successfully.\nMessage:\n%s\n", new String(message.getBody())));

        if (message.getBody().length < 0) {
            log.error("Message received but not valid");
            return;
        }

        EmailInfo emailInfo = emailService.addEmailInfoToDB(
                convertMessageToDomainObject(message)
        );

        boolean isSent = emailService.sendEmail(emailInfo.getEmailTo(), emailInfo.getMessage().toString());

        if (isSent) emailService.updateEmailDetailsStatus(emailInfo);

        log.info("request end..");
    }

    private EmailInfoDTO convertMessageToDomainObject(Message message){
        return (EmailInfoDTO) emailFormatAdapter.toDomainObject(message);
    }
}
