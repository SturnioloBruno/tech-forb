package com.techforb.plantsMonitor.plant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techforb.plantsMonitor.plant.dto.PlantRequest;
import com.techforb.plantsMonitor.plant.dto.PlantResponse;
import com.techforb.plantsMonitor.plant.dto.PlantUpdate;
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

    @Override
    public PlantResponse update(PlantUpdate plantUpdate, Long plantId) {
        Plant plantToUpdate = plantRepository.findById(plantId).orElseThrow(EntityNotFoundException::new);
        PlantResponse plantToUpdate2 = toPlantResponse(plantToUpdate);
        Integer sensorsDisabled = plantUpdate.getSensorsDisabled();


        Long readingsDifference = plantUpdate.getTotalReadings() - plantToUpdate2.getTotalReadings();
        Integer mediumAlertsDifference = plantUpdate.getMediumAlerts() - plantToUpdate2.getTotalMediumAlerts();
        Integer redAlertsDifference = plantUpdate.getRedAlerts() - plantToUpdate2.getTotalRedAlerts();

        List<Sensor> toUpdateSensors = plantToUpdate.getSensors();

        // Calcular el número de sensores actualmente deshabilitados
        long currentDisabledSensors = toUpdateSensors.stream()
                .filter(sensor -> !sensor.getIsEnabled())
                .count();
        long sensorsDifference = sensorsDisabled - currentDisabledSensors;

        if (sensorsDifference > 0) {
            // Deshabilitar sensores adicionales si es necesario
            toUpdateSensors.stream()
                    .filter(Sensor::getIsEnabled)
                    .limit(sensorsDifference)
                    .forEach(sensor -> sensor.setIsEnabled(false));
        } else if (sensorsDifference < 0) {
            // Habilitar sensores adicionales si es necesario
            toUpdateSensors.stream()
                    .filter(sensor -> !sensor.getIsEnabled())
                    .limit(-sensorsDifference)
                    .forEach(sensor -> sensor.setIsEnabled(true));
        }

        // Encontrar un sensor habilitado para actualizar, si no hay, tomar el primero
        Sensor sensorToUpdate = toUpdateSensors.stream()
                .filter(Sensor::getIsEnabled)
                .findFirst()
                .orElse(toUpdateSensors.get(0));

        // actualizo las medidas en algun sensor
        sensorToUpdate.setReadings((int) (sensorToUpdate.getReadings() + readingsDifference));
        sensorToUpdate.setMediumAlerts(sensorToUpdate.getMediumAlerts() + mediumAlertsDifference);
        sensorToUpdate.setRedAlerts(sensorToUpdate.getRedAlerts() + redAlertsDifference);

        // actualizo la planta
        plantToUpdate.setSensors(toUpdateSensors);
        plantToUpdate.setName(plantUpdate.getName());
        plantToUpdate.setCountry(plantUpdate.getCountry());

        // actualizo la base
        plantRepository.save(plantToUpdate);

        // actualizo la respuesta
        plantToUpdate2 = toPlantResponse(plantToUpdate);
        return plantToUpdate2;
    }

    private PlantResponse toPlantResponse(Plant plant) {
        Long totalReadings = 0L;
        Integer totalMediumAlerts = 0;
        Integer totalRedAlerts = 0;
        Integer sensorsDisabled = 0;

        for (Sensor sensor : plant.getSensors()) {
            totalReadings += sensor.getReadings();
            totalMediumAlerts += sensor.getMediumAlerts();
            totalRedAlerts += sensor.getRedAlerts();
            if (!sensor.getIsEnabled()) {
                sensorsDisabled++;
            }
        }

        return PlantResponse.builder()
                .id(plant.getId())
                .name(plant.getName())
                .country(plant.getCountry())
                .totalReadings(totalReadings)
                .totalMediumAlerts(totalMediumAlerts)
                .totalRedAlerts(totalRedAlerts)
                .sensorsDisabled(sensorsDisabled)
                .build();
    }

}
