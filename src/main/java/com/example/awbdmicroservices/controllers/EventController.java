package com.example.awbdmicroservices.controllers;

import com.example.awbdmicroservices.models.Discount;
import com.example.awbdmicroservices.models.Event;
import com.example.awbdmicroservices.models.Reservation;
import com.example.awbdmicroservices.services.EventService;
import com.example.awbdmicroservices.services.clients.DiscountServiceProxy;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/events")
public class EventController {
    @Autowired
    EventService eventService;

    @Autowired
    DiscountServiceProxy discountServiceProxy;

    @GetMapping("/{id}")
    @CircuitBreaker(name="eventById")
    public Event getEvent(@PathVariable Long id) {

        Event event = eventService.getEventById(id);
        Discount discount = discountServiceProxy.findDiscount();

        log.info(discount.getInstanceId());
        event.setPrice(event.getPrice() * (100 - discount.getMonth()) / 100);

        return event;
    }
}
