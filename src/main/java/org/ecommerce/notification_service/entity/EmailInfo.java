package org.ecommerce.notification_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ecommerce.notification_service.dto.EmailStatus;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 500)
    private String message;

    private String emailTo;
    private int tries;
    private Date created_at;
    private Date received_at;

    @Enumerated(EnumType.STRING)
    private EmailStatus status;
}
