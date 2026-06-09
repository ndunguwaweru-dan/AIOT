package com.iot.ai.aiot.services;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.time.Duration;

@Service
public class OpenAIService {
        @Value("${openai.api-key}") private String apiKey;
        @Value("${openai.url}") private String apiUrl;
        
        public String analyzeData(Object payload) {
                try {
                        String prompt = "Detect anomalies in this IoT dataset and return JSON: " + payload;
                        String jsonBody = """
                        {
                                "model": "gpt-4o-mini",
                                "messages": [
                                        {
                                                "role": "user",
                                                "content": "%s"
                                        }
                                ],
                                "temperature": 0.2
                        }
                        """.formatted(prompt.replace("\"", "\\\""));
                        
                        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

                        Request request = new Request.Builder()
                                .url(apiUrl)
                                .addHeader("Authorization", "Bearer " + apiKey)
                                .addHeader("Content-Type", "application/json")
                                .post(body)
                                .build();
                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(Duration.ofSeconds(30))
                                .readTimeout(Duration.ofSeconds(60))
                                .writeTimeout(Duration.ofSeconds(60))
                                .callTimeout(Duration.ofSeconds(90))
                                .build();
                        try (Response response = client.newCall(request).execute()) {
                                if (!response.isSuccessful()) {
                                        throw new RuntimeException("API error: " + response.code());
                                }
                                return response.body().string();
                        }
                } catch (IOException | RuntimeException e) {
                        throw new RuntimeException("Error calling OpenAI", e);
                }
        }
}