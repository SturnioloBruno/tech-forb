package com.techforb.plantsMonitor.probando;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/proxy")
public class ProxyController {

    @Autowired
    private RestTemplate restTemplate;

    private final Map<String, String> countries;

    public ProxyController() {
        countries = loadCountries();
    }

    private Map<String, String> loadCountries() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File("src/main/resources/countries.json"), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading countries from JSON file");
        }
    }

    @GetMapping("/codes")
    public ResponseEntity<?> getCodes() {
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/bandera/{codigoPais}")
    public ResponseEntity<?> getBanderaAPartirDelCodigo(@PathVariable String codigoPais) {
        // Construir la URL completa de la bandera
        String flagApiUrl = "https://flagcdn.com";
        String url = String.format("%s/w20/%s.png", flagApiUrl, codigoPais.toLowerCase());
        return ResponseEntity.ok(url);
    }

    @GetMapping("/banderaAPartirDelNombre/{nombrePais}")
    public ResponseEntity<?> getBanderaAPartirDelNombre(@PathVariable String nombrePais) {
        String codigo = obtenerCodigoPaisAPartirDelNombre(nombrePais.toLowerCase());
        if (codigo != null) {
            return getBanderaAPartirDelCodigo(codigo);
        }else
            return null;
    }

    private String obtenerCodigoPaisAPartirDelNombre(String nombrePais) {
        for (Map.Entry<String, String> entry : countries.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(nombrePais)){
                return entry.getKey();
            }
        }
        return null;
    }

}