package org.ecommerce.notification.util.dataFormatAdapterPattern;

import com.fasterxml.jackson.databind.*;
import lombok.AllArgsConstructor;
import org.ecommerce.notification.dto.*;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.List;

@Component
@AllArgsConstructor
public class EmailFormatAdaptee {
    private final JsonFormatAdaptee jsonFormatAdaptee;
    private final TemplateEngine templateEngine;

    public EmailInfoDTO convertToDomainObject(Message orderMessage){
        JsonNode node = jsonFormatAdaptee.convertToJson(orderMessage);
        EmailInfoDTO emailInfoDTO = new EmailInfoDTO();
        emailInfoDTO.setMessage(node.toString());
        emailInfoDTO.setEmailTo(node.get("customer").get("email").asText());
        return emailInfoDTO;
    }

    public String convertToHtmlContent(String emailContent){
        Context context = new Context();
        JsonNode node = jsonFormatAdaptee.convertToJson(emailContent);
        context.setVariable("customerEmail", node.get("customer").get("email").asText());
        context.setVariable("customerName", node.get("customer").get("name").asText());
        context.setVariable("orderNumber", node.get("order").get("number").asText());
        context.setVariable("orderDate", node.get("order").get("date").asText());
        context.setVariable("billingAddress", node.get("order").get("billing").get("address").asText());
        context.setVariable("paymentMethod", node.get("order").get("billing").get("paymentMethod").asText());
        context.setVariable("totalAmount", node.get("order").get("billing").get("totalAmount").asText());
        context.setVariable("shippingAddress", node.get("order").get("shipping").get("address").asText());
        List<OrderItem> items = jsonFormatAdaptee.convertJsonToList(node.get("order").get("items"));
        context.setVariable("items", items);
        return templateEngine.process("emailTemplate.html", context);
    }
}
