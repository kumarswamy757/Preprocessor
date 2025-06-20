package com.ayra.preprocessor.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ayra.preprocessor.dto.response.ScenarioTestScriptsResponseDTO;

@Service
public class RestApi {
    @Value("${rest.api.url}")
    private String apiUrl;

    @Value("${kafka.api.url}")
    private String kafkaApiUrl;

    public ResponseEntity<ScenarioTestScriptsResponseDTO> getScenarioExecutionRequestDetails(long scenarioId) {
        String url = apiUrl + "scenario/" + scenarioId;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url, ScenarioTestScriptsResponseDTO.class);
    }

    public boolean publishFolderPathToKafka(String folderPath) {
        System.out.println("Publishing folder path to Kafka: " + folderPath);

        String url = kafkaApiUrl + "/publish?message=" + folderPath;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, folderPath, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Successfully published folder path to Kafka: " + folderPath);
            return true;
        } else {
            System.out.println("Failed to publish folder path to Kafka: " + response.getStatusCode());
            return false;
        }
    }
}
