package com.prp.tickets.controller;

import com.prp.tickets.domain.dto.ListPublishedEventResponseDto;
import com.prp.tickets.domain.entities.Event;
import com.prp.tickets.mappers.EventMapper;
import com.prp.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  
}
