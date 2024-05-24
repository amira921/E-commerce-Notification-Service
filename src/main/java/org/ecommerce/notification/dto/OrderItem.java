package org.ecommerce.notification.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private String productName;
    private String quantity;
    private String price;
}
