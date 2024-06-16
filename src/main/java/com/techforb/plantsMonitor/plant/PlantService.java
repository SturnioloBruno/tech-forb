package com.techforb.plantsMonitor.plant;

import com.techforb.plantsMonitor.plant.dto.PlantRequest;
import com.techforb.plantsMonitor.plant.dto.PlantResponse;
import com.techforb.plantsMonitor.plant.dto.PlantUpdate;

import java.util.List;
import java.util.Optional;

public interface PlantService {
    List<PlantResponse> findAll();
    PlantResponse findById(Long id);
    PlantResponse save(PlantRequest plantDTO);
    void deleteById(Long id);
    PlantResponse update(PlantUpdate plantUpdate, Long plantId);
}
