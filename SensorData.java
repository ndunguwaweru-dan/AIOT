package com.iot.ai.aiot.models;

import lombok.Data;

@Data
public class SensorData {
        private double temperature;
        private double humidity;
        private String timestamp;
}