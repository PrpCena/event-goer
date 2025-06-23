package com.prp.tickets.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTicketTypeRequest {
  private UUID id;
  private String name;
  private Double price;
  private String description;
  private Integer totalAvailable;
}
