package com.ayra.preprocessor.dto.response;

import java.sql.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ScenarioTestScriptsResponseDTO {
    private int projectId;
    private long scenarioId;
    private String scenarioName;
    private int createdBy;
    private Date createdDate;
    private int updatedBy;
    private Date updatedDate;
    private byte status;
    private List<TestScripts> testScripts;

    @Data
    @Builder
    public static class TestScripts {
        private long testScriptId;
        private String testScriptName;
        private int testScriptOrder;
        private byte status;
        private int createdBy;
        private Date createdDate;
        private int updatedBy;
        private Date updatedDate;
        private List<TestSteps> testSteps;

        @Data
        @Builder
        public static class TestSteps {
            private long testScriptId;
            private long testStepId;
            private String command;
            private String target;
            private String value;
            private byte status;
            private int createdBy;
            private Date createdDate;
            private int updatedBy;
            private Date updatedDate;
            private int stepNumber;
        }
    }

}
