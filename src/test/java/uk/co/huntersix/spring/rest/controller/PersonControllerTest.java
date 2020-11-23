package uk.co.huntersix.spring.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.co.huntersix.spring.rest.exception.PersonException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.model.PersonRequest;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    public static String asJsonString(final PersonRequest obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(new Person("Mary", "Smith"));
        this.mockMvc.perform(get("/person/smith/mary"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName").value("Mary"))
                .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void shouldReturnErrorFromServiceWhenNotFound() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(null);
        this.mockMvc.perform(get("/person/smith/mary"))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(jsonPath("status").value("NOT_FOUND"))
                .andExpect(jsonPath("message").value("The requested person does not exist in the list"));
    }

    // Tests for exercise  : 3
    @Test
    public void shouldReturnPersonsWithSurnameFromService() throws Exception {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Mary", "Smith"));
        personList.add(new Person("John", "Smith"));
        personList.add(new Person("Brian", "Archer"));
        when(personDataService.findPersonsWithSurname(any())).thenReturn(personList);

        this.mockMvc.perform(get("/person/smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andReturn();
    }

    @Test
    public void shouldReturnOnePersonWithSurnameFromService() throws Exception {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Brian", "Archer"));
        when(personDataService.findPersonsWithSurname(any())).thenReturn(personList);

        this.mockMvc.perform(get("/person/Archer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Archer"))
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();
    }

    @Test
    public void shouldReturnNotFoundPersonWithSurnameIsNotfound() throws Exception {
        List<Person> personList = new ArrayList<>();
        when(personDataService.findPersonsWithSurname(any())).thenReturn(personList);

        this.mockMvc.perform(get("/person/archer"))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(jsonPath("status").value("NOT_FOUND"))
                .andExpect(jsonPath("message")
                        .value("The requested person with surname:archer does not exist in the list"));

    }

    // Add Person
    @Test
    public void testAddPersonSuccessful() throws Exception {
        when(personDataService.addPerson(any(), any())).thenReturn(true);
        PersonRequest person = new PersonRequest("mary", "smith");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(person)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    //Add Person - Person already exists
    @Test
    public void testAddExistingPersonIsNotSuccessful() throws Exception {

        PersonException usEx = new PersonException("Person with firstName john and lastName smith is already present. Please use PATCH command to update the existing record");
        when(personDataService.addPerson(any(), any())).thenThrow(usEx);
        PersonRequest person = new PersonRequest("mary", "smith");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(person)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("message")
                        .value("Person with firstName john and lastName smith is already present. Please use PATCH command to update the existing record"));
    }

    @Test
    public void testAddPersonNotSuccessful() throws Exception {
        when(personDataService.addPerson(any(), any())).thenReturn(false);
        PersonRequest person = new PersonRequest("mary", "smith");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(person)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

    }
}