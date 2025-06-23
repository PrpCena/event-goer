package com.prp.tickets.services;


import com.prp.tickets.domain.entities.QrCode;
import com.prp.tickets.domain.entities.Ticket;

public interface QrCodeService {
  
  QrCode generateQrCode(Ticket ticket);
}
