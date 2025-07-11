package com.prp.tickets.domain.enums;

public enum EventStatusEnum {
  DRAFT, // Initial state when creating an event
  PUBLISHED, // Event is live and tickets can be purchased
  CANCELLED, // Event will not take place
  COMPLETED // Event has finished
}
