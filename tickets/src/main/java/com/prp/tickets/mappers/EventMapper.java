package com.prp.tickets.mappers;

import com.prp.tickets.domain.CreateEventRequest;
import com.prp.tickets.domain.CreateTicketTypeRequest;
import com.prp.tickets.domain.dto.CreateEventRequestDto;
import com.prp.tickets.domain.dto.CreateEventResponseDto;
import com.prp.tickets.domain.dto.CreateTicketTypeRequestDto;
import com.prp.tickets.domain.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
  CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);
  
  CreateEventRequest fromDto(CreateEventRequestDto dto);
  
  CreateEventResponseDto toDto(Event event);
}
