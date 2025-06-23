package com.prp.tickets.services;

import com.prp.tickets.domain.CreateEventRequest;
import com.prp.tickets.domain.UpdateEventRequest;
import com.prp.tickets.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventService {
  Event createEvent(UUID organizerId, CreateEventRequest event);
  Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable);
  Optional<Event> getEventForOrganizer(UUID organizerId,  UUID eventId);
  Event updateEventForOrganizer(UUID organizerId, UUID eventId, UpdateEventRequest event);
}
