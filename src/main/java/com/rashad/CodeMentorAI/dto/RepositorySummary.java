package com.rashad.CodeMentorAI.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RepositorySummary {

    private int totalFiles;

    private int totalFolders;

    private long totalLines;

    private List<RepositoryFile> files;
}
