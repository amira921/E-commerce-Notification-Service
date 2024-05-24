package org.ecommerce.notification_service.util.dataFormatAdapterPattern;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailFormatAdapter implements DataFormatAdapter {
    private final EmailFormatAdaptee emailFormatAdaptee;

    @Override
    public Object toDomainObject(Message message) {
        return emailFormatAdaptee.convertToDomainObject(message);
    }

    @Override
    public String toHtmlContent(String content) {
        return emailFormatAdaptee.convertToHtmlContent(content);
    }
}
