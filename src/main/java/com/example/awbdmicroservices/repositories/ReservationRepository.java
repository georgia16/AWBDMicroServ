package com.example.awbdmicroservices.repositories;

import com.example.awbdmicroservices.models.Event;
import com.example.awbdmicroservices.models.Person;
import com.example.awbdmicroservices.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByEventAndPerson(Event event, Person person);
}
