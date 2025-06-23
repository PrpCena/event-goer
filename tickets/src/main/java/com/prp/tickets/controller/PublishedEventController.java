package com.prp.tickets.controller;

import com.prp.tickets.domain.dto.GetPublishedEventDetailsResponseDto;
import com.prp.tickets.domain.dto.ListPublishedEventResponseDto;
import com.prp.tickets.domain.entities.Event;
import com.prp.tickets.mappers.EventMapper;
import com.prp.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/published-events")
public class PublishedEventController {
  
  private final EventService eventService;
  private final EventMapper eventMapper;
  
  @GetMapping
  public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvents(
	@RequestParam(required = false) String q,
	Pageable pageable) {
	Page<Event> events;
	if (null != q && !q
						.trim()
						.isEmpty()) {
	  events = eventService.searchPublicEvents(q, pageable);
	} else {
	  events = eventService.listPublicEvents(pageable);
	}
	return ResponseEntity.ok(events.map(eventMapper::toListPublishedEventResponseDto));
  }
  
  @GetMapping(path = "/{eventId}")
  public ResponseEntity<GetPublishedEventDetailsResponseDto> getPublishedEventDetails(
	@PathVariable UUID eventId) {
	return eventService
			 .getPublicEvent(eventId)
			 .map(eventMapper::toGetPublishedEventDetailsResponseDto)
			 .map(ResponseEntity::ok)
			 .orElse(ResponseEntity
					   .notFound()
					   .build());
  }
}
