package org.ecommerce.notification_service.mapper;

import javax.annotation.processing.Generated;
import org.ecommerce.notification_service.dto.EmailDetailsDTO;
import org.ecommerce.notification_service.entity.EmailDetails;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2 (Oracle Corporation)"
)
@Component
public class EmailMapperImpl implements EmailMapper {

    @Override
    public EmailDetails mapToEntity(EmailDetailsDTO dto) {
        if ( dto == null ) {
            return null;
        }

        EmailDetails emailDetails = new EmailDetails();

        emailDetails.setId( dto.getId() );
        emailDetails.setMessage( dto.getMessage() );
        emailDetails.setEmailTo( dto.getEmailTo() );
        emailDetails.setTries( dto.getTries() );
        emailDetails.setCreated_at( dto.getCreated_at() );
        emailDetails.setReceived_at( dto.getReceived_at() );
        emailDetails.setStatus( dto.getStatus() );

        return emailDetails;
    }

    @Override
    public EmailDetailsDTO mapToDTO(EmailDetails entity) {
        if ( entity == null ) {
            return null;
        }

        EmailDetailsDTO.EmailDetailsDTOBuilder emailDetailsDTO = EmailDetailsDTO.builder();

        return emailDetailsDTO.build();
    }
}
