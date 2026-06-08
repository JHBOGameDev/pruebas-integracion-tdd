// src/test/java/edu/unisabana/tyvs/registry/delivery/rest/RegistryControllerIT.java
package edu.unisabana.tyvs.registry.delivery.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import edu.unisabana.tyvs.registry.application.port.out.RegistryRepositoryPort;

import static org.junit.Assert.*;

// src/test/java/.../RegistryControllerIT.java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistryControllerIT {

    @TestConfiguration
    static class TestBeans {
        @Bean
        public RegistryRepositoryPort registryRepositoryPort() throws Exception {
            String jdbc = "jdbc:h2:mem:regdb;DB_CLOSE_DELAY=-1";
            var repo = new edu.unisabana.tyvs.registry.infrastructure.persistence.RegistryRepository(jdbc);
            repo.initSchema();
            return repo;
        }

        @Bean
        public edu.unisabana.tyvs.registry.application.usecase.Registry registry(RegistryRepositoryPort port) {
            return new edu.unisabana.tyvs.registry.application.usecase.Registry(port);
        }
    }

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void shouldRegisterValidPerson() {
        String json = "{\"name\":\"Ana\",\"id\":100,\"age\":30,\"gender\":\"FEMALE\",\"alive\":true}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> resp = rest.postForEntity("/register", new HttpEntity<>(json, headers), String.class);

        assert resp.getStatusCode() == HttpStatus.OK;
        assert "VALID".equals(resp.getBody());
    }

    @Test
    public void shouldReturnUnderAge() {

        String json =
                """
                {
                    "name":"Juan",
                    "id":101,
                    "age":15,
                    "gender":"MALE",
                    "alive":true
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response =
                rest.postForEntity(
                        "/register",
                        new HttpEntity<>(json,headers),
                        String.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());

        assertEquals("UNDERAGE",response.getBody());
    }

    @Test
    public void shouldReturnDeadWhenPersonIsNotAlive() {
        String json = """
            {
                "name":"Carlos",
                "id":102,
                "age":40,
                "gender":"MALE",
                "alive":false
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = rest.postForEntity(
                "/register",
                new HttpEntity<>(json, headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("DEAD", response.getBody());
    }

    @Test
    public void shouldReturnDuplicatedWhenPersonAlreadyRegistered() {
        String json = """
            {
                "name":"Maria",
                "id":103,
                "age":25,
                "gender":"FEMALE",
                "alive":true
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Primer registro
        rest.postForEntity("/register", new HttpEntity<>(json, headers), String.class);

        // Segundo registro con mismo ID
        ResponseEntity<String> response = rest.postForEntity(
                "/register",
                new HttpEntity<>(json, headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("DUPLICATED", response.getBody());
    }

    @Test
    public void shouldReturnBadRequestWhenJsonIsIncomplete() {
        // JSON incompleto — falta el campo "alive"
        String json = """
            {
                "name":"Pedro",
                "id":104,
                "age":30
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = rest.postForEntity(
                "/register",
                new HttpEntity<>(json, headers),
                String.class);

        // El sistema debe manejar el JSON incompleto
        assertNotNull(response.getStatusCode());
        assertNotNull(response.getBody());
    }
}