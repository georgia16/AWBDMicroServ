package com.example.awbdmicroservices.services;

import com.example.awbdmicroservices.exceptions.EventNotFoundException;
import com.example.awbdmicroservices.exceptions.ReservationNotFoundException;
import com.example.awbdmicroservices.models.Event;
import com.example.awbdmicroservices.models.Reservation;
import com.example.awbdmicroservices.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public List<Event> findAll() {
        List<Event> events = new LinkedList<>();
        eventRepository.findAll().iterator().forEachRemaining(events::add);
        return events;
    }

    public Event getEventById(Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (!optionalEvent.isPresent())
            throw new EventNotFoundException("Event not found");

        return optionalEvent.get();
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public Event deleteById(Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);

        if (!optionalEvent.isPresent()) {
            throw new EventNotFoundException("Event not found");
        }
        eventRepository.delete(optionalEvent.get());
        return optionalEvent.get();
    }
}
