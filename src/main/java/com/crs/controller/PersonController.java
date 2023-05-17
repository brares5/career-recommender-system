package com.crs.controller;

import com.crs.model.Person;
import com.crs.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Person person) {
        personService.createPerson(person);
    }

    @GetMapping
    public void classifyPerson(@RequestBody Person person) {
        personService.classifyPerson(person);
    }
}
