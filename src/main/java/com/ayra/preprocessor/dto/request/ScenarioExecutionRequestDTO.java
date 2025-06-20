package com.ayra.preprocessor.dto.request;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScenarioExecutionRequestDTO {
    private int projectId;
    private long scenarioId;
    private String scenarioName;
    private int createdBy;
    private Date createdDate;
    private int updatedBy;
    private Date updatedDate;
    private List<TestScripts> testScripts;

    @Data
    @Builder
    public static class TestScripts {
        private int testScriptId;
        private String testScriptName;
        private int testScriptOrder;
    }
}
