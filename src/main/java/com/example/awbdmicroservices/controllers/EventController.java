package com.example.awbdmicroservices.controllers;

import com.example.awbdmicroservices.models.*;
import com.example.awbdmicroservices.services.EventService;
import com.example.awbdmicroservices.services.clients.DiscountServiceProxy;
import com.example.awbdmicroservices.services.clients.DurationServiceProxy;
import com.example.awbdmicroservices.services.clients.PostponeServiceProxy;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

@RestController
@Slf4j
@RequestMapping("/events")
public class EventController {
    @Autowired
    EventService eventService;

    @Autowired
    DiscountServiceProxy discountServiceProxy;

    @Autowired
    PostponeServiceProxy postponeServiceProxy;

    @Autowired
    DurationServiceProxy durationServiceProxy;

    @GetMapping("/{id}")
    @CircuitBreaker(name="eventById", fallbackMethod = "getDiscountFallback")
    @Operation(summary = "Get event", description = "Retrieve an existing event based on a given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The event was successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Wrong path"),
            @ApiResponse(responseCode = "500", description = "Error on the server")
    })
    public Event getEvent(@PathVariable @Parameter(name = "id", description = "Event id", example = "1") Long id) {

        Event event = eventService.getEventById(id);
        Discount discount = discountServiceProxy.findDiscount();
        Postpone postpone = postponeServiceProxy.findPostpone();
        Duration duration = durationServiceProxy.findDuration();

        LocalDate date = event.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();;

        log.info(discount.getInstanceId());
        event.setPrice(event.getPrice() * (100 - discount.getMonth()) / 100);
        event.setDate(Date.valueOf(date.plusDays(postpone.getDays()).plusMonths(postpone.getMonths())));
        event.setDuration(event.getDuration() + duration.getHours() + (duration.getDays()*24));

        return event;
    }

    //resilience -> behaviour when error
    //return event with no discount applied
    private Event getDiscountFallback(Long id, Throwable throwable) {
        Event event = eventService.getEventById(id);
        return event;
    }
}
