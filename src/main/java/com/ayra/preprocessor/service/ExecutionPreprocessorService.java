package com.ayra.preprocessor.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ayra.preprocessor.api.RestApi;
import com.ayra.preprocessor.dto.request.ScenarioExecutionRequestDTO;
import com.ayra.preprocessor.dto.response.ScenarioTestScriptsResponseDTO;
import com.ayra.preprocessor.dto.response.ScenarioTestScriptsResponseDTO.TestScripts;
import com.ayra.preprocessor.dto.response.ScenarioTestScriptsResponseDTO.TestScripts.TestSteps;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ExecutionPreprocessorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionPreprocessorService.class);
    @Value("${filesystem.path}")
    private String fileSystemPath;

    @Autowired
    RestApi restApi;

    public String processExecutionRequest(
            ScenarioExecutionRequestDTO scenarioExecutionRequestDTO) {
        System.out.println("Start of Processing execution request...");
        System.out.println("Scenario ID: " + scenarioExecutionRequestDTO.getScenarioId());
        ResponseEntity<ScenarioTestScriptsResponseDTO> scenarioDetailsResponse = restApi
                .getScenarioExecutionRequestDetails(scenarioExecutionRequestDTO.getScenarioId());

        if (scenarioDetailsResponse.getStatusCode().is2xxSuccessful()) {
            System.out.println("Scenario Details: " + scenarioDetailsResponse.getBody());
            ScenarioTestScriptsResponseDTO scenarioDetails = scenarioDetailsResponse.getBody();
            if (scenarioDetails != null) {
                UUID runId = UUID.randomUUID();
                int projectId = scenarioDetails.getProjectId();
                long scenarioId = scenarioDetails.getScenarioId();
                String scenarioName = scenarioDetails.getScenarioName();
                String executionFolderPath = fileSystemPath + projectId + "/" + scenarioId + "/" + runId;
                List<TestScripts> testScripts = scenarioDetails.getTestScripts();
                testScripts.forEach(eachTestScript -> {
                    long testScriptId = eachTestScript.getTestScriptId();
                    String testScriptName = eachTestScript.getTestScriptName();
                    int testScriptOrder = eachTestScript.getTestScriptOrder();
                    List<TestSteps> testSteps = eachTestScript.getTestSteps();

                    if (!Files.exists(Paths.get(executionFolderPath))) {
                        try {
                            System.out.println("Creating directory" + testScriptName);
                            Files.createDirectories(Paths.get(executionFolderPath));
                        } catch (Exception e) {
                            System.out.println("Failed to create project directory: " + e.getMessage());
                        }
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        String testStepsJson = objectMapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(testSteps);
                        File file = new File(
                                executionFolderPath + "/" + testScriptOrder + "_" + testScriptId + ".json");
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Files.write(Paths.get(file.getAbsolutePath()), testStepsJson.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                serializationRDBean(null, projectId)
                return executionFolderPath;
            } else {
                System.out.println("No scenario details found for the given ID.");
                return null;
            }
        } else {
            System.out.println("Failed to fetch scenario details. Status code: " + scenarioDetailsResponse.getStatusCode());
            return null;
        }
    }

    private String serializationRDBean(ScenarioExecutionRequestDTO rdBean, int testRunId) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        LOGGER.trace("#TEST_RUN_ID : " + rdBean.getTestExecutionRunId() + "# OBJECT SERILIZED ");
        try {
            // serialize and send the RunDefinitionBean
            String filePath = Util.getFileName(testRunId) + testRunId + ".obj";
            fos = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(rdBean);
            fos.close();
            oos.close();
            return "Success";
        } catch (Exception e) {
            LOGGER.error("Error=#TEST_RUN_ID : " + rdBean.getTestExecutionRunId() + "# OBJECT SERILIZED ", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOGGER.error("Error=#TEST_RUN_ID : " + rdBean.getTestExecutionRunId() + "# EXCEPTION WHILE CLOSING FILEOUTPUTSTREAM ", e);
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    LOGGER.error("Error=#TEST_RUN_ID : " + rdBean.getTestExecutionRunId() + "# EXCEPTION WHILE CLSOING OBJECTOUTPUTSTREAM ", e);
                }
            }
        }
        return "Failure";
    }  

    public boolean publishFolderPathToKafka(String folderPath) {
        return restApi.publishFolderPathToKafka(folderPath);
    }
}
