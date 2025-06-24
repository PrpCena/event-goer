package com.prp.tickets.services.impl;

import com.prp.tickets.domain.entities.Ticket;
import com.prp.tickets.repositories.TicketRepository;
import com.prp.tickets.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
  
  private final TicketRepository ticketRepository;
  
  @Override
  public Page<Ticket> listTicketsForUser(UUID userId, Pageable pageable) {
	return ticketRepository.findByPurchaserId(userId, pageable);
  }
}
