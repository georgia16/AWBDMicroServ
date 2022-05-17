package com.example.awbdmicroservices.services;

import com.example.awbdmicroservices.exceptions.PersonNotFoundException;
import com.example.awbdmicroservices.exceptions.ReservationNotFoundException;
import com.example.awbdmicroservices.models.Person;
import com.example.awbdmicroservices.models.Reservation;
import com.example.awbdmicroservices.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    public List<Person> findAll() {
        List<Person> persons = new LinkedList<>();
        personRepository.findAll().iterator().forEachRemaining(persons::add);
        return persons;
    }

    public Person getPersonById(Long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (!optionalPerson.isPresent())
            throw new PersonNotFoundException("Person not found");

        return optionalPerson.get();
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public Person deleteById(Long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);

        if (!optionalPerson.isPresent()) {
            throw new PersonNotFoundException("Person not found");
        }
        personRepository.delete(optionalPerson.get());
        return optionalPerson.get();
    }
}
