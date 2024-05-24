package org.ecommerce.notification_service.mapper;

import org.ecommerce.notification_service.dto.EmailInfoDTO;
import org.ecommerce.notification_service.entity.EmailInfo;
import org.mapstruct.Mapper;

@Mapper
public interface EmailMapper {
    EmailInfo mapToEntity(EmailInfoDTO dto);
    EmailInfoDTO mapToDTO(EmailInfo entity);
}
