package com.iot.ai.aiot.controllers;

import com.iot.ai.aiot.models.SensorRequest;
import com.iot.ai.aiot.services.DetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Controller
public class DetectionController {
        @Autowired private DetectionService detectionService;
        private final ObjectMapper objectMapper = new ObjectMapper();
    
        @PostMapping("/upload")
        public String handleUpload(@RequestParam("file") MultipartFile file, Model model) {
                try {
                        SensorRequest request = objectMapper.readValue(file.getInputStream(), SensorRequest.class);
                        String result = detectionService.detect(request);
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(result);
                        String content = root
                                .path("choices")
                                .get(0)
                                .path("message")
                                .path("content")
                                .asText();
                        int start = content.indexOf("{"); 
                        int end = content.lastIndexOf("}") + 1;
                        String analysis = content.substring(start, end); 
                        
                        //String json = objectMapper.writeValueAsString(request);
                        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
                        model.addAttribute("request", json);
                        model.addAttribute("response", analysis);
                } catch (Exception e) {
                        model.addAttribute("response", "Error: " + e.getMessage());
                }
                return "aiot";
        } 
        
        @GetMapping("/")
        public String showIndex(Model model){
                return "aiot";
        }
}