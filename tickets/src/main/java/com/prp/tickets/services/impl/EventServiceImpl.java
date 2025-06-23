package com.prp.tickets.services.impl;


import com.prp.tickets.domain.CreateEventRequest;
import com.prp.tickets.domain.UpdateEventRequest;
import com.prp.tickets.domain.UpdateTicketTypeRequest;
import com.prp.tickets.domain.entities.Event;
import com.prp.tickets.domain.entities.TicketType;
import com.prp.tickets.domain.entities.User;
import com.prp.tickets.exception.EventNotFoundException;
import com.prp.tickets.exception.EventUpdateException;
import com.prp.tickets.exception.TicketTypeNotFoundException;
import com.prp.tickets.exception.UserNotFoundException;
import com.prp.tickets.repositories.EventRepository;
import com.prp.tickets.repositories.UserRepository;
import com.prp.tickets.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl
  implements EventService {
  
  private final UserRepository userRepository;
  private final EventRepository eventRepository;
  
  @Override
  @Transactional
  public Event createEvent(UUID organizerId, CreateEventRequest event) {
	User organizer = userRepository
					   .findById(organizerId)
					   .orElseThrow(
						 () -> new UserNotFoundException(String.format("User with ID '%s' not found", organizerId)));
	
	Event eventToCreate = new Event();
	
	List<TicketType> ticketTypesToCreate = event
											 .getTicketTypes()
											 .stream()
											 .map(ticketType -> {
											   TicketType ticketTypeToCreate = new TicketType();
											   ticketTypeToCreate.setName(ticketType.getName());
											   ticketTypeToCreate.setPrice(ticketType.getPrice());
											   ticketTypeToCreate.setDescription(ticketType.getDescription());
											   ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
											   ticketTypeToCreate.setEvent(eventToCreate);
											   return ticketTypeToCreate;
											 })
											 .toList();
	
	eventToCreate.setName(event.getName());
	eventToCreate.setStart(event.getStart());
	eventToCreate.setEnd(event.getEnd());
	eventToCreate.setVenue(event.getVenue());
	eventToCreate.setSalesStart(event.getSalesStart());
	eventToCreate.setSalesEnd(event.getSalesEnd());
	eventToCreate.setStatus(event.getStatus());
	eventToCreate.setOrganizer(organizer);
	eventToCreate.setTicketTypes(ticketTypesToCreate);
	return eventRepository.save(eventToCreate);
  }
  
  @Override
  public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
	return eventRepository.findByOrganizerId(organizerId, pageable);
  }
  
  @Override
  public Optional<Event> getEventForOrganizer(UUID organizerId, UUID eventId) {
	return eventRepository.findByIdAndOrganizerId(eventId, organizerId);
  }
  
  @Override
  @Transactional
  public Event updateEventForOrganizer(UUID organizerId, UUID eventId, UpdateEventRequest event) {
	if (event.getId() == null)
	  throw new EventUpdateException("Event id can not be null");
	
	if (!eventId.equals(event.getId())) {
	  throw new EventUpdateException("Event id does not match");
	}
	
	Event existingEvent = eventRepository
							.findByIdAndOrganizerId(eventId, organizerId)
							.orElseThrow(() -> new EventNotFoundException("Event with ID '" + eventId + "' not found"));
	
	existingEvent.setName(event.getName());
	existingEvent.setStart(event.getStart());
	existingEvent.setEnd(event.getEnd());
	existingEvent.setVenue(event.getVenue());
	existingEvent.setSalesStart(event.getSalesStart());
	existingEvent.setSalesEnd(event.getSalesEnd());
	existingEvent.setStatus(event.getStatus());
	
	Set<UUID> requestTicketTypeIds = event
									   .getTicketTypes()
									   .stream()
									   .map(UpdateTicketTypeRequest::getId)
									   .filter(Objects::nonNull)
									   .collect(Collectors.toSet());
	existingEvent
	  .getTicketTypes()
	  .removeIf(existingTicketType -> !requestTicketTypeIds.contains(existingTicketType.getId()));
	Map<UUID, TicketType> existingTicketTypesIndex = existingEvent
													   .getTicketTypes()
													   .stream()
													   .collect(
														 Collectors.toMap(TicketType::getId, Function.identity()));
	for (UpdateTicketTypeRequest ticketType : event.getTicketTypes()) {
	  if (null == ticketType.getId()) {
		// Create
		TicketType ticketTypeToCreate = new TicketType();
		ticketTypeToCreate.setName(ticketType.getName());
		ticketTypeToCreate.setPrice(ticketType.getPrice());
		ticketTypeToCreate.setDescription(ticketType.getDescription());
		ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
		ticketTypeToCreate.setEvent(existingEvent);
		existingEvent
		  .getTicketTypes()
		  .add(ticketTypeToCreate);
	  } else if (existingTicketTypesIndex.containsKey(ticketType.getId())) {
		// Update
		TicketType existingTicketType = existingTicketTypesIndex.get(ticketType.getId());
		existingTicketType.setName(ticketType.getName());
		existingTicketType.setPrice(ticketType.getPrice());
		existingTicketType.setDescription(ticketType.getDescription());
		existingTicketType.setTotalAvailable(ticketType.getTotalAvailable());
	  } else {
		throw new TicketTypeNotFoundException(
		  String.format("Ticket type with ID '%s' does not exist", ticketType.getId()));
	  }
	}
	return eventRepository.save(existingEvent);
	
  }
  
  @Override
  @Transactional
  public void deleteEventForOrganizer(UUID organizerId, UUID eventId) {
	 getEventForOrganizer(organizerId, eventId).ifPresent(eventRepository::delete);
	 
  }
  
  @Override
  public Page<Event> listPublicEvents(Pageable pageable) {
	return null;
  }
  
}
