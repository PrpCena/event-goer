package com.prp.tickets.services.impl;

import com.prp.tickets.domain.entities.QrCode;
import com.prp.tickets.domain.entities.Ticket;
import com.prp.tickets.domain.entities.TicketValidation;
import com.prp.tickets.domain.enums.QrCodeStatusEnum;
import com.prp.tickets.domain.enums.TicketValidationMethod;
import com.prp.tickets.domain.enums.TicketValidationStatusEnum;
import com.prp.tickets.exception.QrCodeNotFoundException;
import com.prp.tickets.exception.TicketNotFoundException;
import com.prp.tickets.repositories.QrCodeRepository;
import com.prp.tickets.repositories.TicketRepository;
import com.prp.tickets.repositories.TicketValidationRepository;
import com.prp.tickets.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketValidationServiceImpl
  implements TicketValidationService {
  
  private final TicketValidationRepository ticketValidationRepository;
  private final TicketRepository ticketRepository;
  private final QrCodeRepository qrCodeRepository;
  
  @Override
  public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
	QrCode qrCode = qrCodeRepository
					  .findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE)
					  .orElseThrow(
						() -> new QrCodeNotFoundException(String.format("QR Code with ID %s was not found", qrCodeId)));
	Ticket ticket = qrCode.getTicket();
	return validateTicket(ticket);
  }
  
  @Override
  public TicketValidation validateTicketManually(UUID ticketId) {
	Ticket ticket = ticketRepository
					  .findById(ticketId)
					  .orElseThrow(
						() -> new TicketNotFoundException(String.format("Ticket with ID %s was not found", ticketId)));
	return validateTicket(ticket);
  }
  
  private TicketValidation validateTicket(Ticket ticket) {
	TicketValidation ticketValidation = new TicketValidation();
	ticketValidation.setTicket(ticket);
	ticketValidation.setValidationMethod(TicketValidationMethod.QR_SCAN);
	TicketValidationStatusEnum ticketValidationStatus = ticket
														  .getValidations()
														  .stream()
														  .filter(
															v -> TicketValidationStatusEnum.VALID.equals(v.getStatus()))
														  .findFirst()
														  .map(v -> TicketValidationStatusEnum.INVALID)
														  .orElse(TicketValidationStatusEnum.VALID);
	ticketValidation.setStatus(ticketValidationStatus);
	return ticketValidationRepository.save(ticketValidation);
  }
  
}
