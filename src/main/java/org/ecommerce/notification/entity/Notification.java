package org.ecommerce.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ecommerce.notification.dto.NotificationStatus;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 500)
    private String message;

    private String contact;
    private int tries;
    private Date created_at;
    private Date received_at;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
}
