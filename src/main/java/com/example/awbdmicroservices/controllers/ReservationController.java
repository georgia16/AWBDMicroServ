package com.example.awbdmicroservices.controllers;

import com.example.awbdmicroservices.models.Discount;
import com.example.awbdmicroservices.models.Event;
import com.example.awbdmicroservices.models.Person;
import com.example.awbdmicroservices.models.Reservation;
import com.example.awbdmicroservices.services.ReservationService;
import com.example.awbdmicroservices.services.clients.DiscountServiceProxy;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    ReservationService reservationService;

    @Autowired
    DiscountServiceProxy discountServiceProxy;

    @GetMapping(value = "/list")
    @Operation(summary = "Get reservations", description = "Get all reservations from the DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of reservations was successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Wrong path"),
            @ApiResponse(responseCode = "500", description = "Error on the server")
    })
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
    @Operation(summary = "Get reservation", description = "Retrieve a reservation based on a given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The reservation was successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Wrong path - id doesn't exist"),
            @ApiResponse(responseCode = "500", description = "Error on the server")
    })
    public Reservation getReservation(@PathVariable @Parameter(name = "id", description = "Reservation id", example = "1") Long id) {
        return reservationService.getReservationById(id);
    }

    @GetMapping("/event/{event}/person/{person}")
    @Operation(summary = "Get reservation by event and person", description = "Retrieve a reservation based on two given ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The reservation was successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Wrong path - one of the ids doesn't exist"),
            @ApiResponse(responseCode = "500", description = "Error on the server")
    })
    public CollectionModel<Reservation> getReservationsByEventAndPerson(@PathVariable
                                                                            @Parameter(name = "id", description = "Event id", example = "1") Long event,
                                                                        @PathVariable
                                                                        @Parameter(name = "id", description = "Person id", example = "1") Long person) {
        List<Reservation> reservations = reservationService.listByEventAndPerson(event, person);

        for(final Reservation reservation: reservations) {
            Link selfLink = linkTo(methodOn(ReservationController.class).getReservation(reservation.getId())).withSelfRel();
            reservation.add(selfLink);

            Link deleteLink = linkTo(methodOn(ReservationController.class).delete(reservation.getId())).withRel("deleteReservation");
            reservation.add(deleteLink);
        }

        Link link = linkTo(methodOn(ReservationController.class).getReservationsByEventAndPerson(event, person)).withSelfRel();
        CollectionModel<Reservation> result = CollectionModel.of(reservations, link);

        return result;
    }

    @PostMapping("/save")
    @Operation(summary = "Post reservation", description = "Create a new reservation based on the info received in the request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The reservation was successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Wrong path"),
            @ApiResponse(responseCode = "500", description = "Error on the server")
    })
    public Reservation create(@RequestBody Reservation reservation) {
        return reservationService.save(reservation);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete reservation", description = "Delete an existing reservation by specifying its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The reservation was successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Wrong path - id doesn't exist"),
            @ApiResponse(responseCode = "500", description = "Error on the server")
    })
    public Reservation delete(@PathVariable @Parameter(name = "id", description = "Reservation id", example = "1") Long id) {
        Reservation reservation = reservationService.deleteById(id);

        Link deleteLink = linkTo(methodOn(ReservationController.class).delete(reservation.getId())).withRel("deleteReservation");
        reservation.add(deleteLink);
        return reservation;
    }
}
