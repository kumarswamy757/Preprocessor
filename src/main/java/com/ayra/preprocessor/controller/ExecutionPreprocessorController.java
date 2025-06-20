package com.ayra.preprocessor.controller;

import com.ayra.preprocessor.dto.request.ScenarioExecutionRequestDTO;
import com.ayra.preprocessor.dto.response.ScenarioTestScriptsResponseDTO;
import com.ayra.preprocessor.service.ExecutionPreprocessorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preprocessor")
public class ExecutionPreprocessorController {

    @Autowired
    private ExecutionPreprocessorService executionPreprocessorService;

    @PostMapping("/process-scenario")
    public ResponseEntity<String> getTestScript(@RequestBody ScenarioExecutionRequestDTO scenarioExecutionRequestDTO) {
        String executionFolderPath = executionPreprocessorService.processExecutionRequest(scenarioExecutionRequestDTO);
        if (executionFolderPath == null) {
            return ResponseEntity.status(400).body("No Steps to execute in the scenario");
        } else {
            executionPreprocessorService.publishFolderPathToKafka(executionFolderPath);
            System.out.println("Execution folder path: " + executionFolderPath);
            return ResponseEntity.ok("Execution request processed successfully");
        }
    }
}
