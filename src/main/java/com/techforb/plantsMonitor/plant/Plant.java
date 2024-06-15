package com.techforb.plantsMonitor.plant;

import com.techforb.plantsMonitor.sensor.Sensor;
import com.techforb.plantsMonitor.sensor.SensorType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Plant {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String country;
    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL)
    private List<Sensor> sensors;

    @PrePersist
    public void initializeSensors() {
        if (sensors == null) {
            sensors = new ArrayList<>();
        }

        for (SensorType type : SensorType.values()) {
            Sensor sensor = Sensor.builder()
                    .sensorType(type)
                    .plant(this)
                    .build();
            sensor.generateRandomValues();
            sensors.add(sensor);
        }
    }
}
