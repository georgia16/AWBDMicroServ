package com.example.awbdmicroservices.controllers;

import com.example.awbdmicroservices.models.Person;
import com.example.awbdmicroservices.models.Reservation;
import com.example.awbdmicroservices.services.PersonService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/all")
    @Operation(summary = "Get persons", description = "Get all persons from the DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of persons was successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Wrong path"),
            @ApiResponse(responseCode = "500", description = "Error on the server")
    })
    public CollectionModel<Person> findAll() {
        List<Person> persons = personService.findAll();

        for(final Person person: persons) {
            Link selfLink = linkTo(methodOn(PersonController.class).getPerson(person.getId())).withSelfRel();
            person.add(selfLink);

            Link deleteLink = linkTo(methodOn(PersonController.class).delete(person.getId())).withRel("deletePerson");
            person.add(deleteLink);
        }

        Link link = linkTo(methodOn(PersonController.class).findAll()).withSelfRel();

        CollectionModel<Person> result = CollectionModel.of(persons, link);
        return result;
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete person", description = "Delete an existing person by specifying its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The person was successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Wrong path - id doesn't exist"),
            @ApiResponse(responseCode = "500", description = "Error on the server")
    })
    public Person delete(@PathVariable @Parameter(name = "id", description = "Person id", example = "1") Long id) {
        Person person = personService.deleteById(id);

        Link deleteLink = linkTo(methodOn(PersonController.class).delete(person.getId())).withRel("deletePerson");
        person.add(deleteLink);
        return person;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get person", description = "Retrieve a person based on a given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The person was successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Wrong path - id doesn't exist"),
            @ApiResponse(responseCode = "500", description = "Error on the server")
    })
    public Person getPerson(@PathVariable @Parameter(name = "id", description = "Person id", example = "1") Long id) {
        return personService.getPersonById(id);
    }

    @PostMapping("/save")
    @Operation(summary = "Post person", description = "Create a new person based on the info received in the request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The person was successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Wrong path"),
            @ApiResponse(responseCode = "500", description = "Error on the server")
    })
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        Person createdPerson = personService.save(person);
        return ResponseEntity.created(URI.create("/agency" + createdPerson.getId())).body(createdPerson);
    }
}
