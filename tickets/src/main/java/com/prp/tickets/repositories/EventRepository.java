package com.prp.tickets.repositories;

import com.prp.tickets.domain.entities.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EventRepository extends CrudRepository<Event, UUID> {

}
