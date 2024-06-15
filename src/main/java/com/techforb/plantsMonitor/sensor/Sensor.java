package com.techforb.plantsMonitor.sensor;

import com.techforb.plantsMonitor.plant.Plant;
import jakarta.persistence.*;
import lombok.*;

import java.util.Random;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sensor {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private SensorType sensorType;
    // quiero que se generen aleatorio
    private Integer readings;
    // quiero que sea aleatorio, pero que la suma de este
    private Integer mediumAlerts;
    // mas este, no sea mayor que readings, pero los tres aleatorios
    private Integer redAlerts;
    private Boolean isEnabled = true;
    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    public void generateRandomValues() {
        Random random = new Random();

        this.mediumAlerts = random.nextInt(40);
        this.redAlerts = random.nextInt(5);
        this.readings = random.nextInt(500) + mediumAlerts + redAlerts;
    }
}
