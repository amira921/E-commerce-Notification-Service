package org.ecommerce.notification_service.util;

import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification_service.dto.OrderItem;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class DataConverter {

    /**
     * Converts the message body from RabbitMQ into a JsonNode.
     *
     * @param content The RabbitMQ message containing the body to be converted.
     * @return The JsonNode representing the converted message body.
     */
    public JsonNode convertMessageBodyToJsonNode(Message content){
        JsonNode node = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            node = mapper.readTree(content.getBody());
            log.info("Message body in RabbitMQ successfully converted into JsonNode");
        } catch (IOException e) {
            log.error("Failed to convert the message body in RabbitMQ to JsonNode.\n" + e.getMessage());
        }
        return node;
    }

    /**
     * Converts email content saved in database as JSON string to JsonNode object using the Jackson ObjectMapper.
     *
     * @param content The JSON string to be converted.
     * @return JsonNode
     * @throws `RabbitMQException` If an error occurs during the conversion process, RabbitMQException is logged.
     *                           The exception message provides details about the success or failure of the conversion.
     *                           If the conversion fails, the message includes the reason for failure.
     */
    public JsonNode ConvertStringToJsonNode(String content) {
        JsonNode node = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            node = mapper.readTree(content);
            log.info("The message in the database converted into JsonNode");
        } catch (IOException e) {
            log.info("The conversion from the message body in RabbitMQ to JsonNode failed" + e.getMessage());
        }
        return node;
    }

    public List<OrderItem> convertJsonNodeToList(JsonNode jsonNode) {
        List<OrderItem> itemList = new ArrayList<>();

        if (jsonNode.isArray()) {
            Iterator<JsonNode> elements = jsonNode.elements();
            while (elements.hasNext()) {
                JsonNode element = elements.next();
                OrderItem item = new OrderItem(
                        element.get("productName").asText(),
                        element.get("quantity").asText(),
                        element.get("price").asText()
                );
                itemList.add(item);
            }
        }
        return itemList;
    }

}
