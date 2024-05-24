package org.ecommerce.notification.util.dataFormatAdapterPattern;

import com.fasterxml.jackson.databind.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.notification.dto.OrderItem;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.*;

@Component
@AllArgsConstructor
@Slf4j
public class JsonFormatAdaptee {
    private final ObjectMapper objectMapper;

     List<OrderItem> convertJsonToList(JsonNode jsonNode) {
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

    <T> JsonNode convertToJson(T content){
         JsonNode node = null;
         try {
            if(content instanceof String) node = objectMapper.readTree((String) content);
            else if(content instanceof Message) node = objectMapper.readTree(((Message) content).getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return node;
    }
}
