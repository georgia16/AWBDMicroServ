package com.example.awbdmicroservices.services;

import com.example.awbdmicroservices.exceptions.EventNotFoundException;
import com.example.awbdmicroservices.exceptions.PersonNotFoundException;
import com.example.awbdmicroservices.exceptions.ReservationNotFoundException;
import com.example.awbdmicroservices.models.Event;
import com.example.awbdmicroservices.models.Person;
import com.example.awbdmicroservices.models.Reservation;
import com.example.awbdmicroservices.repositories.EventRepository;
import com.example.awbdmicroservices.repositories.PersonRepository;
import com.example.awbdmicroservices.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    PersonRepository personRepository;

    public List<Reservation> findAll() {
        List<Reservation> reservations = new LinkedList<>();
        reservationRepository.findAll().iterator().forEachRemaining(reservations::add);
        return reservations;
    }

    public Reservation getReservationById(Long id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (!optionalReservation.isPresent())
            throw new ReservationNotFoundException("Reservation not found");

        return optionalReservation.get();
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation deleteById(Long id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if (!optionalReservation.isPresent()) {
            throw new ReservationNotFoundException("Reservation not found");
        }
        reservationRepository.delete(optionalReservation.get());
        return optionalReservation.get();
    }

    public List<Reservation> listByEventAndPerson(Long event, Long person) {
        Optional<Event> optionalEvent = eventRepository.findById(event);
        Optional<Person> optionalPerson = personRepository.findById(person);

        if(!optionalEvent.isPresent()) {
            throw new EventNotFoundException("Event not found");
        }

        if(!optionalPerson.isPresent()) {
            throw new PersonNotFoundException("Person not found");
        }

        List<Reservation> reservations = reservationRepository.findAll().stream().filter(res -> res.getEvent().getId().equals(event)).filter(res -> res.getPerson().getId().equals(person)).collect(Collectors.toList());

        return reservations;
    }
}
