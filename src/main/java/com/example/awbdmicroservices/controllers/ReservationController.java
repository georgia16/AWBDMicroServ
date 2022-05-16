package com.example.awbdmicroservices.controllers;

import com.example.awbdmicroservices.models.Event;
import com.example.awbdmicroservices.models.Person;
import com.example.awbdmicroservices.models.Reservation;
import com.example.awbdmicroservices.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    ReservationService reservationService;


    @GetMapping(value = "/list")
    public CollectionModel<Reservation> findAll() {
        List<Reservation> reservations = reservationService.findAll();

        for(final Reservation reservation: reservations) {
            Link selfLink = linkTo(methodOn(ReservationController.class).getReservation(reservation.getId())).withSelfRel();
            reservation.add(selfLink);

            Link deleteLink = linkTo(methodOn(ReservationController.class).delete(reservation.getId())).withRel("deleteReservation");
            reservation.add(deleteLink);
        }

        Link link = linkTo(methodOn(ReservationController.class).findAll()).withSelfRel();

        CollectionModel<Reservation> result = CollectionModel.of(reservations, link);
        return result;
    }

    @GetMapping("/{id}")
    public Reservation getReservation(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    @GetMapping("/event/{event}/person/{person}")
    public List<Reservation> getReservationsByEventAndPerson(@PathVariable Long event, @PathVariable Long person) {
        List<Reservation> reservations = reservationService.listByEventAndPerson(event, person);

//        Link selfLink = linkTo(methodOn(ReservationController.class).showReservationList()).withSelfRel();

//        reservations.add(selfLink);

        return reservations;
    }

    @PostMapping("/save")
    public Reservation create(@RequestBody Reservation reservation) {
        return reservationService.save(reservation);
    }

    @DeleteMapping("/delete/{id}")
    public Reservation delete(@PathVariable Long id) {
        Reservation reservation = reservationService.deleteById(id);

        Link deleteLink = linkTo(methodOn(ReservationController.class).delete(reservation.getId())).withRel("deleteReservation");
        reservation.add(deleteLink);
        return reservation;
    }
}