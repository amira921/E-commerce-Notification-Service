package org.ecommerce.notification.mapper;

import org.ecommerce.notification.dto.NotificationDTO;
import org.ecommerce.notification.entity.Notification;
import org.mapstruct.Mapper;

@Mapper
public interface NotificationMapper {
    Notification mapToEntity(NotificationDTO dto);
    NotificationDTO mapToDTO(Notification entity);
}
