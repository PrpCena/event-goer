package com.prp.tickets.controller;


import com.prp.tickets.domain.CreateEventRequest;
import com.prp.tickets.domain.UpdateEventRequest;
import com.prp.tickets.domain.dto.*;
import com.prp.tickets.domain.entities.Event;
import com.prp.tickets.mappers.EventMapper;
import com.prp.tickets.services.EventService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.prp.tickets.util.JwtUtil.parseUserId;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/events")
public class EventController {
  
  final EventService eventService;
  final EventMapper eventMapper;
  
  @PostMapping
  public ResponseEntity<CreateEventResponseDto> createEvent(
	@RequestBody @Valid CreateEventRequestDto createEventRequestDto,
	@AuthenticationPrincipal Jwt jwt) {
	CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);
	UUID userId = parseUserId(jwt);
	Event event = eventService.createEvent(userId, createEventRequest);
	CreateEventResponseDto toDto = eventMapper.toDto(event);
	return new ResponseEntity<>(toDto, HttpStatus.CREATED);
  }
  
  @GetMapping
  public ResponseEntity<Page<ListEventResponseDto>> listEvents(@AuthenticationPrincipal Jwt jwt, Pageable pageable) {
	UUID userId = parseUserId(jwt);
	Page<Event> events = eventService.listEventsForOrganizer(userId, pageable);
	return ResponseEntity.ok(events.map(eventMapper::toListEventResponseDto));
  }
  
  @GetMapping(path = "/{eventId}")
  public ResponseEntity<GetEventDetailsResponseDto> getEventDetails(
	@PathVariable UUID eventId,
	@AuthenticationPrincipal Jwt jwt) {
	UUID userId = parseUserId(jwt);
	return eventService
			 .getEventForOrganizer(userId, eventId)
			 .map(eventMapper::toGetEventDetailsResponseDto)
			 .map(ResponseEntity::ok)
			 .orElse(ResponseEntity
					   .notFound()
					   .build());
  }
  

  @PutMapping(path = "/{eventId}")
  public ResponseEntity<UpdateEventResponseDto> updateEvent(
	@RequestBody @Valid UpdateEventRequestDto updateEventRequestDto,
	@AuthenticationPrincipal Jwt jwt,
	@PathVariable UUID eventId) {
	UpdateEventRequest updateEventRequest = eventMapper.fromDto(updateEventRequestDto);
	UUID userId = parseUserId(jwt);
	Event event = eventService.updateEventForOrganizer(userId, eventId, updateEventRequest);
	UpdateEventResponseDto updateEventResponseDto = eventMapper.toUpdateEventResponseDto(event);
	return ResponseEntity.ok(updateEventResponseDto);
  }
  
  @DeleteMapping(path = "/{eventId}")
  public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId, @AuthenticationPrincipal Jwt jwt) {
	UUID userId = parseUserId(jwt);
	eventService.deleteEventForOrganizer(userId, eventId);
	return ResponseEntity
			 .noContent()
			 .build();
  }
}
