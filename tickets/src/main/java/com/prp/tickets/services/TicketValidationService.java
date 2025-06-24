package com.prp.tickets.services;

import com.prp.tickets.domain.entities.TicketValidation;

import java.util.UUID;

public interface TicketValidationService {
  TicketValidation validateTicketByQrCode(UUID qrCodeId);
  
  TicketValidation validateTicketManually(UUID ticketId);
}
