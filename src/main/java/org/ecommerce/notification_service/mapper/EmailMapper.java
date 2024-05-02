package org.ecommerce.notification_service.mapper;

import org.ecommerce.notification_service.dto.EmailDetailsDTO;
import org.ecommerce.notification_service.entity.EmailDetails;
import org.mapstruct.Mapper;

@Mapper
public interface EmailMapper {
    EmailDetails mapToEntity(EmailDetailsDTO dto);
    EmailDetailsDTO mapToDTO(EmailDetails entity);
}
