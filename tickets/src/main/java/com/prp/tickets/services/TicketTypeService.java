package com.prp.tickets.services;

import com.prp.tickets.domain.entities.Ticket;

import java.util.UUID;

public interface TicketTypeService {
  Ticket purchaseTicket(UUID userId, UUID ticketTypeId);
}
