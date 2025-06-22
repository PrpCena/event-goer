package com.prp.tickets.services;

import com.prp.tickets.domain.CreateEventRequest;
import com.prp.tickets.domain.entities.Event;

import java.util.UUID;

public interface EventService {
  Event createEvent(UUID organizerId, CreateEventRequest event);
}
