package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.exception.PersonException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
    public static List<Person> PERSON_DATA = new ArrayList<>(Arrays.asList(
            new Person("Mary", "Smith"),
            new Person("Brian", "Archer"),
            new Person("Collin", "Brown")
    ));

    public Person findPerson(String lastName, String firstName) {

        List<Person> personList = PERSON_DATA.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
        return personList.size() > 0 ? personList.get(0) : null;
    }


    public List<Person> findPersonsWithSurname(String lastName) {
        return PERSON_DATA.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    public boolean addPerson(String lastName, String firstName) throws PersonException {
        boolean isPersonPresent = PERSON_DATA.stream()
                .anyMatch(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName));
        if (isPersonPresent) {
            throw new PersonException("Person with firstName " + firstName + " and lastName " + lastName + " is already present. Please use PATCH command to update the existing record");
        }

        return PERSON_DATA.add(new Person(firstName, lastName));
    }
}
