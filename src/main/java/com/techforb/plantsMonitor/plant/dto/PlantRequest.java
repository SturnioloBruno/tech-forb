package com.techforb.plantsMonitor.plant.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlantRequest {
    private String name;
    private String country;
}
