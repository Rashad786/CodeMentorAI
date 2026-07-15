package com.rashad.CodeMentorAI.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CloneRepositoryResponse {

    private String repoName;
    private String path;
    private String msg;
    private boolean success;
}
