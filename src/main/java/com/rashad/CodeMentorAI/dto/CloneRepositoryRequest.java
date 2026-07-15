package com.rashad.CodeMentorAI.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CloneRepositoryRequest {

    @NotBlank(message = "Repository URL cannot be empty")
    private String url;
}
