package com.prp.tickets.services.impl;

import com.prp.tickets.domain.entities.Ticket;
import com.prp.tickets.domain.entities.TicketType;
import com.prp.tickets.domain.entities.User;
import com.prp.tickets.domain.enums.TicketStatusEnum;
import com.prp.tickets.exception.TicketSoldOutException;
import com.prp.tickets.exception.TicketTypeNotFoundException;
import com.prp.tickets.exception.UserNotFoundException;
import com.prp.tickets.repositories.TicketRepository;
import com.prp.tickets.repositories.TicketTypeRepository;
import com.prp.tickets.repositories.UserRepository;
import com.prp.tickets.services.QrCodeService;
import com.prp.tickets.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl
  implements TicketTypeService {
  
  private final UserRepository userRepository;
  private final TicketTypeRepository ticketTypeRepository;
  private final TicketRepository ticketRepository;
  private final QrCodeService qrCodeService;
  
  @Override
  @Transactional
  public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
	User user = userRepository
				  .findById(userId)
				  .orElseThrow(() -> new UserNotFoundException("User not found"));
	TicketType ticketType = ticketTypeRepository
							  .findByIdWithLock(ticketTypeId)
							  .orElseThrow(() -> new TicketTypeNotFoundException("Ticket type not found"));
	
	int purchasedTickets = ticketRepository.countByTicketTypeId(ticketType.getId());
	
	Integer totalAvailable = ticketType.getTotalAvailable();
	if (purchasedTickets + 1 > totalAvailable) {
	  throw new TicketSoldOutException();
	}
	
	Ticket ticket = new Ticket();
	ticket.setStatus(TicketStatusEnum.PURCHASED);
	ticket.setTicketType(ticketType);
	ticket.setPurchaser(user);
	Ticket savedTicket = ticketRepository.save(ticket);
	qrCodeService.generateQrCode(savedTicket);
	return ticketRepository.save(savedTicket);
	
	
  }
}
