package com.prp.tickets.controller;


import com.prp.tickets.domain.CreateEventRequest;
import com.prp.tickets.domain.dto.CreateEventRequestDto;
import com.prp.tickets.domain.dto.CreateEventResponseDto;
import com.prp.tickets.domain.dto.ListEventResponseDto;
import com.prp.tickets.domain.entities.Event;
import com.prp.tickets.mappers.EventMapper;
import com.prp.tickets.services.EventService;
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
  
  private UUID parseUserId(Jwt jwt) {
	return UUID.fromString(jwt.getSubject());
  }
}
