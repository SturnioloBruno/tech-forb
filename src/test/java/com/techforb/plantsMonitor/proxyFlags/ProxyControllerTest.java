package com.techforb.plantsMonitor.proxyFlags;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProxyControllerTest {

    @InjectMocks
    private ProxyController proxyController;

    @Mock
    private RestTemplate restTemplate;

    private Map<String, String> countries;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        countries = loadCountries();
    }

    private Map<String, String> loadCountries() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File("src/main/resources/countries.json"), HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading countries from JSON file");
        }
    }

    @Test
    void testLoadCountriesFromJson() {
        assertEquals(301, countries.size());
        assertEquals("Argentina", countries.get("ar"));
        assertEquals("Brasil", countries.get("br"));
    }

    @Test
    void testGetBanderaAPartirDelNombre_Argentina() {
        String countryName = "argentina";

        // Mock para simular la respuesta del restTemplate
        String expectedUrl = "https://flagcdn.com/w20/ar.png";
        when(restTemplate.getForEntity(any(String.class), any())).thenReturn(new ResponseEntity<>(expectedUrl, HttpStatus.OK));

        // Simular la solicitud GET al endpoint
        ResponseEntity<?> responseEntity = proxyController.getBanderaAPartirDelNombre(countryName);

        // Verificar que la respuesta no sea nula y tenga estado OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedUrl, responseEntity.getBody());
    }

    @Test
    void testGetBanderaAPartirDelNombre_Brasil() {
        String countryName = "brasil";

        // Mock para simular la respuesta del restTemplate
        String expectedUrl = "https://flagcdn.com/w20/br.png";
        when(restTemplate.getForEntity(any(String.class), any())).thenReturn(new ResponseEntity<>(expectedUrl, HttpStatus.OK));

        // Simular la solicitud GET al endpoint
        ResponseEntity<?> responseEntity = proxyController.getBanderaAPartirDelNombre(countryName);

        // Verificar que la respuesta no sea nula y tenga estado OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedUrl, responseEntity.getBody());
    }

}
