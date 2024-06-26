package com.techforb.plantsMonitor.plant;

import com.techforb.plantsMonitor.plant.dto.PlantRequest;
import com.techforb.plantsMonitor.plant.dto.PlantResponse;
import com.techforb.plantsMonitor.plant.dto.PlantUpdate;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plants")
@SecurityRequirement(name = "bearerAuth")
public class PlantController {

    private final PlantService plantService;

    @GetMapping
    public List<PlantResponse> getAllPlants() {
        return plantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantResponse> getPlantById(@PathVariable Long id) {
        return ResponseEntity.ok(plantService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PlantResponse> createPlant(@RequestBody PlantRequest plantRequest) {
        return ResponseEntity.ok(plantService.save(plantRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
        plantService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlantResponse> updatePlant(@PathVariable Long id, @RequestBody PlantUpdate plantUpdate) {
        PlantResponse updatedPlant = plantService.update(plantUpdate, id);
        return ResponseEntity.ok(updatedPlant);
    }
}
