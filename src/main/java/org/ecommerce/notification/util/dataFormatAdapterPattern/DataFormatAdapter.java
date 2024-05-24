package org.ecommerce.notification.util.dataFormatAdapterPattern;

import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
public interface DataFormatAdapter{
    Object toDomainObject(Message message);
    String toHtmlTemplate(String content);
}
