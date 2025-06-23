package com.prp.tickets.mappers;

import com.prp.tickets.domain.CreateEventRequest;
import com.prp.tickets.domain.CreateTicketTypeRequest;
import com.prp.tickets.domain.UpdateEventRequest;
import com.prp.tickets.domain.UpdateTicketTypeRequest;
import com.prp.tickets.domain.dto.*;
import com.prp.tickets.domain.entities.Event;
import com.prp.tickets.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
  CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);
  
  CreateEventRequest fromDto(CreateEventRequestDto dto);
  
  CreateEventResponseDto toDto(Event event);
  
  ListEventTicketTypeResponseDto toDto(TicketType ticketType);
  
  ListEventResponseDto toListEventResponseDto(Event event);
  
  GetEventDetailsTicketTypesResponseDto toGetEventDetailsTicketTypesResponseDto(TicketType ticketType);
  
  GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);
  
  UpdateTicketTypeRequest fromDto(UpdateTicketTypeRequestDto dto);
  
  UpdateEventRequest fromDto(UpdateEventRequestDto dto);
  
  UpdateTicketTypeResponseDto toUpdateTicketTypeResponseDto(TicketType ticketType);
  
  UpdateEventResponseDto toUpdateEventResponseDto(Event event);
  
  ListPublishedEventResponseDto toListPublishedEventResponseDto(Event event);
  
  GetPublishedEventDetailsTicketTypesResponseDto toGetPublishedEventDetailsTicketTypesResponseDto(TicketType ticketType);
  
  GetPublishedEventDetailsResponseDto toGetPublishedEventDetailsResponseDto(Event event);
}
