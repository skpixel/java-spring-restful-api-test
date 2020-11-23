package uk.co.huntersix.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uk.co.huntersix.spring.rest.exception.PersonException;
import uk.co.huntersix.spring.rest.exception.UserNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.model.PersonRequest;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;

@RestController
public class PersonController {
    PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public Person person(@PathVariable(value = "lastName") String lastName,
                         @PathVariable(value = "firstName") String firstName) throws UserNotFoundException {

        Person person = personDataService.findPerson(lastName, firstName);
        if (person == null) {
            throw new UserNotFoundException("The requested person does not exist in the list");
        }
        return person;
    }

    @GetMapping("/person/{lastName}")
    public List<Person> personWithSurname(@PathVariable(value = "lastName") String lastName) throws UserNotFoundException {
        List<Person> personList = personDataService.findPersonsWithSurname(lastName);
        if (personList.isEmpty()) {
            System.out.println("There are no persons with requested surname:" + lastName);
            throw new UserNotFoundException("The requested person with surname:" + lastName + " does not exist in the list");
        } else if (personList.size() == 1)
            // any specific action to be taken if it only 1 person with surname
            System.out.println("There is 1 person with given Surname exist");
        else
            // any specific action to be taken if more than 1 person with surname
            System.out.println("Multiple people with given Surname exists");
        return personList;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/person", consumes = "application/json")
    public String addPerson(@RequestBody PersonRequest person) throws PersonException {
        boolean isAddPerson = personDataService.addPerson(person.getLastName(), person.getFirstName());

        if (!isAddPerson) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Person Add Failed");
        }

        return "Successfully Added";
    }
}