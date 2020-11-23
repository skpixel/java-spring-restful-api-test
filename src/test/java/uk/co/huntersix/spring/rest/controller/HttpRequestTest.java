package uk.co.huntersix.spring.rest.controller;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnPersonDetails() throws Exception {
        assertThat(
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/person/smith/mary",
                        String.class
                )
        ).contains("Mary");
    }

    @Test
    public void shouldReturnPersonNotFound() throws Exception {
        ResponseEntity<String> response = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/person/sarath/kumar", String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).contains("The requested person does not exist in the list");
    }


    @Test
    public void shouldReturnPersonDetailsForSurname() throws Exception {
        assertThat(
                this.restTemplate.getForObject("http://localhost:" + port + "/person/archer", String.class)
        ).contains("Brian");
    }

    @Test
    public void shouldReturnNotFoundPersonWithSurnameNotFound() throws Exception {
        ResponseEntity<String> response = this.restTemplate
                .getForEntity("http://localhost:" + port + "/person/kumar", String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody())
                .contains("The requested person with surname:kumar does not exist in the list");
    }

    @Test
    public void shouldReturnSuccessWhenNewPersonAdded() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject personJsonObject = new JSONObject();
        personJsonObject.put("firstName", "mary");
        personJsonObject.put("lastName", "archer");
        HttpEntity<String> request =
                new HttpEntity<String>(personJsonObject.toString(), headers);

        ResponseEntity<String> response = this.restTemplate
                .postForEntity("http://localhost:" + port + "/person", request, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        System.out.println(response.toString());
        assertThat(response.getBody()).contains("Successfully Added");
    }

    @Test
    public void shouldReturnErrorWhenAddingExistingPerson() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject personJsonObject = new JSONObject();
        personJsonObject.put("firstName", "mary");
        personJsonObject.put("lastName", "smith");
        HttpEntity<String> request =
                new HttpEntity<String>(personJsonObject.toString(), headers);

        ResponseEntity<String> response = this.restTemplate
                .postForEntity("http://localhost:" + port + "/person", request, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
        System.out.println(response.toString());
        assertThat(response.getBody())
                .contains("Person with firstName mary and lastName smith is already present. Please use PATCH command to update the existing record");
    }
}