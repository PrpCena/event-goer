package com.prp.tickets.services;


import com.prp.tickets.domain.entities.QrCode;
import com.prp.tickets.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {
  
  QrCode generateQrCode(Ticket ticket);
  
  byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
