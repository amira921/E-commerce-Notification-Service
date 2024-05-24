package org.ecommerce.notification.exception.validation;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationError {
    private String field;
    private String message;
}

