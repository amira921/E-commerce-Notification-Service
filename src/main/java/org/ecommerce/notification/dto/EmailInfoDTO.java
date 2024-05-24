package org.ecommerce.notification.dto;

import lombok.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailInfoDTO {
    private Integer id;
    private String message;
    private String emailTo;
    private int tries;
    private Date created_at;
    private Date received_at;
    private EmailStatus status;
}
