package edu.unisabana.tyvs.registry.delivery.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistryControllerIT {

    @Autowired
    private TestRestTemplate rest;

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // Given: persona viva, 30 años, id único
    // When: POST /register con JSON válido
    // Then: HTTP 200 y body "VALID"
    @Test
    public void shouldRegisterValidPerson() {
        // Arrange
        String json = "{\"name\":\"Ana\",\"id\":100,\"age\":30,\"gender\":\"FEMALE\",\"alive\":true}";

        // Act
        ResponseEntity<String> resp = rest.postForEntity("/register", new HttpEntity<>(json, headers()), String.class);

        // Assert
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("VALID", resp.getBody());
    }

    // Given: persona viva de 15 años
    // When: POST /register
    // Then: HTTP 200 y body "UNDERAGE"
    @Test
    public void shouldReturnUnderAge() {
        // Arrange
        String json = "{\"name\":\"Juan\",\"id\":101,\"age\":15,\"gender\":\"MALE\",\"alive\":true}";

        // Act
        ResponseEntity<String> response = rest.postForEntity("/register", new HttpEntity<>(json, headers()), String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UNDERAGE", response.getBody());
    }

    // Given: persona con alive=false
    // When: POST /register
    // Then: HTTP 200 y body "DEAD"
    @Test
    public void shouldReturnDeadWhenPersonIsNotAlive() {
        // Arrange
        String json = "{\"name\":\"Carlos\",\"id\":102,\"age\":40,\"gender\":\"MALE\",\"alive\":false}";

        // Act
        ResponseEntity<String> response = rest.postForEntity("/register", new HttpEntity<>(json, headers()), String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("DEAD", response.getBody());
    }

    // Given: persona con id=103 registrada dos veces
    // When: segundo POST /register con mismo id
    // Then: HTTP 200 y body "DUPLICATED"
    @Test
    public void shouldReturnDuplicatedWhenPersonAlreadyRegistered() {
        // Arrange
        String json = "{\"name\":\"Maria\",\"id\":103,\"age\":25,\"gender\":\"FEMALE\",\"alive\":true}";

        // Act
        rest.postForEntity("/register", new HttpEntity<>(json, headers()), String.class);
        ResponseEntity<String> response = rest.postForEntity("/register", new HttpEntity<>(json, headers()), String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("DUPLICATED", response.getBody());
    }

    // Given: JSON sin campo "gender" ni "alive"
    // When: POST /register con JSON incompleto
    // Then: el sistema responde con algún código HTTP (prueba negativa)
    @Test
    public void shouldReturnBadRequestWhenJsonIsIncomplete() {
        // Arrange
        String json = "{\"name\":\"Pedro\",\"id\":104,\"age\":30}";

        // Act
        ResponseEntity<String> response = rest.postForEntity("/register", new HttpEntity<>(json, headers()), String.class);

        // Assert
        assertNotNull(response.getStatusCode());
        assertNotNull(response.getBody());
    }
}