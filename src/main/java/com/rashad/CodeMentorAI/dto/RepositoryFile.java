package com.rashad.CodeMentorAI.dto;

import lombok.Data;

@Data
public class RepositoryFile {

    private String fileName;
    private String content;
    private String relativePath;
    private String extension;
    private long size;
    private long lineCount;
}
