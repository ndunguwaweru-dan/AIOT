package com.iot.ai.aiot.services;

import com.iot.ai.aiot.models.SensorRequest;
import org.springframework.stereotype.Service;

@Service
public class DetectionService {
        private final OpenAIService openAIService;

        public DetectionService(OpenAIService openAIService) {
                this.openAIService = openAIService;
        }

        public String detect(SensorRequest request) {
                return openAIService.analyzeData(request);
        }
}