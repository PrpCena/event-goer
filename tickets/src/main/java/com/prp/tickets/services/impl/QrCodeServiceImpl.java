package com.prp.tickets.services.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.prp.tickets.domain.entities.QrCode;
import com.prp.tickets.domain.entities.Ticket;
import com.prp.tickets.domain.enums.QrCodeStatusEnum;
import com.prp.tickets.exception.QrCodeGenerationException;
import com.prp.tickets.exception.QrCodeNotFoundException;
import com.prp.tickets.repositories.QrCodeRepository;
import com.prp.tickets.services.QrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class QrCodeServiceImpl
  implements QrCodeService {
  private static final int QR_HEIGHT = 300;
  private static final int QR_WIDTH = 300;
  private final QRCodeWriter qrCodeWriter;
  private final QrCodeRepository qrCodeRepository;
  
  @Override
  public QrCode generateQrCode(Ticket ticket) {
	try {
	  // Generate a unique ID for the QR code
	  UUID uniqueId = UUID.randomUUID();
	  String qrCodeImage = generateQrCodeImage(uniqueId);
	  // Create and save the QR code entity
	  QrCode qrCode = new QrCode();
	  qrCode.setId(uniqueId);
	  qrCode.setStatus(QrCodeStatusEnum.ACTIVE);
	  qrCode.setValue(qrCodeImage);
	  qrCode.setTicket(ticket);
	  return qrCodeRepository.saveAndFlush(qrCode);
	} catch (IOException | WriterException ex) {
	  throw new QrCodeGenerationException("Failed to generate QR Code", ex);
	}
  }
  
  @Override
  public byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId) {
	QrCode qrCode = qrCodeRepository
					  .findByTicketIdAndTicketPurchaserId(ticketId, userId)
					  .orElseThrow(QrCodeNotFoundException::new);
	try {
	  return Base64
			   .getDecoder()
			   .decode(qrCode.getValue());
	} catch (IllegalArgumentException ex) {
	  log.error("Invalid base64 QR Code for ticket ID: {}", ticketId, ex);
	  throw new QrCodeNotFoundException();
	}
  }
  
  private String generateQrCodeImage(UUID uuid) throws WriterException, IOException {
	BitMatrix bitMatrix = qrCodeWriter.encode(uuid.toString(), BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
	
	BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
	
	try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
	  ImageIO.write(qrCodeImage, "PNG", baos);
	  byte[] imageBytes = baos.toByteArray();
	  return Base64
			   .getEncoder()
			   .encodeToString(imageBytes);
	}
  }
}
