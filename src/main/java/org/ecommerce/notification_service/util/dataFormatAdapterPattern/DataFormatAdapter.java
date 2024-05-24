package org.ecommerce.notification_service.util.dataFormatAdapterPattern;

import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
public interface DataFormatAdapter{
    Object toDomainObject(Message message);
    String toHtmlContent(String content);
}
