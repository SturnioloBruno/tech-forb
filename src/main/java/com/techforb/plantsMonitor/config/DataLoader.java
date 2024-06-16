package com.techforb.plantsMonitor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techforb.plantsMonitor.plant.Plant;
import com.techforb.plantsMonitor.plant.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PlantRepository plantRepository;

    @Override
    public void run(String... args) throws Exception {
        if (plantRepository.count() == 0) {
            List<Plant> plants = Arrays.asList(
                new Plant(null, "Quilmes", "Argentina", null),
                new Plant(null, "Zarate", "Argentina", null),
                new Plant(null, "Sao Pablo", "Brasil", null),
                new Plant(null, "Asuncion", "Paraguay", null),
                new Plant(null, "Montevideo", "Uruguay", null)
            );
            plantRepository.saveAll(plants);
        }
    }
}
