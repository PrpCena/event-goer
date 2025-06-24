package com.prp.tickets.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListTicketTicketTypeResponseDto {
  private UUID id;
  private String name;
  private Double price;
}
