package com.iot.ai.aiot.models;

import lombok.Data;
import java.util.List;

@Data
public class SensorRequest {
        private String deviceId;
        private List<SensorData> data;
}
