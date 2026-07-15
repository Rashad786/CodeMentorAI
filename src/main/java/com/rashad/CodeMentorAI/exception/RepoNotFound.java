package com.rashad.CodeMentorAI.exception;

public class RepoNotFound extends RuntimeException{
    public RepoNotFound(String message) {
        super(message);
    }
}
