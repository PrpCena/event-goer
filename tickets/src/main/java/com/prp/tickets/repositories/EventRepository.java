package com.prp.tickets.repositories;

import com.prp.tickets.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends CrudRepository<Event, UUID> {
	Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);
	Optional<Event> findByIdAndOrganizerId(UUID id,  UUID organizerId);
}
