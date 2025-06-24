package com.prp.tickets.mappers;


import com.prp.tickets.domain.dto.ListTicketResponseDto;
import com.prp.tickets.domain.dto.ListTicketTicketTypeResponseDto;
import com.prp.tickets.domain.entities.Ticket;
import com.prp.tickets.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {
  ListTicketTicketTypeResponseDto toListTicketTicketTypeResponseDto(TicketType ticketType);
  
  ListTicketResponseDto toListTicketResponseDto(Ticket ticket);
}
