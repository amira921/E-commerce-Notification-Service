package org.ecommerce.notification.mapper;

import org.ecommerce.notification.dto.EmailInfoDTO;
import org.ecommerce.notification.entity.EmailInfo;
import org.mapstruct.Mapper;

@Mapper
public interface EmailInfoMapper {
    EmailInfo mapToEntity(EmailInfoDTO dto);
    EmailInfoDTO mapToDTO(EmailInfo entity);
}
