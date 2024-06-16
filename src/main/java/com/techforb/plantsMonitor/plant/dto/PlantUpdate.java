package com.techforb.plantsMonitor.plant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlantUpdate {
    private String name;
    private String country;
    private Long totalReadings;
    private Integer sensorsDisabled;
    private Integer mediumAlerts;
    private Integer redAlerts;
}
