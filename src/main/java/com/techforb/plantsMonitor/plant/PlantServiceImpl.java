package com.techforb.plantsMonitor.plant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techforb.plantsMonitor.plant.dto.PlantRequest;
import com.techforb.plantsMonitor.plant.dto.PlantResponse;
import com.techforb.plantsMonitor.sensor.Sensor;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {

    private final PlantRepository plantRepository;
    private final ObjectMapper mapper;

    @Override
    public List<PlantResponse> findAll() {
        return plantRepository.findAll().stream()
                .map(this::toPlantResponse)
                .toList();
    }

    @Override
    public PlantResponse findById(Long id) {
        Plant plant = plantRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return toPlantResponse(plant);
    }

    @Override
    public PlantResponse save(PlantRequest plantDTO) {
        Plant plant = mapper.convertValue(plantDTO, Plant.class);
        return toPlantResponse(plantRepository.save(plant));
    }

    @Override
    public void deleteById(Long id) {
        plantRepository.deleteById(id);
    }

    private PlantResponse toPlantResponse(Plant plant) {
        Long totalReadings = 0L;
        Integer totalMediumAlerts = 0;
        Integer totalRedAlerts = 0;

        for (Sensor sensor : plant.getSensors()) {
            totalReadings += sensor.getReadings();
            totalMediumAlerts += sensor.getMediumAlerts();
            totalRedAlerts += sensor.getRedAlerts();
        }

        return PlantResponse.builder()
                .name(plant.getName())
                .country(plant.getCountry())
                .totalReadings(totalReadings)
                .totalMediumAlerts(totalMediumAlerts)
                .totalRedAlerts(totalRedAlerts)
                .build();
    }

}
