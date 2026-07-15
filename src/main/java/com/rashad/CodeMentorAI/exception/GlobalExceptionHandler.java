package com.rashad.CodeMentorAI.exception;

import com.rashad.CodeMentorAI.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RepoNotFound.class)
    public ResponseEntity<ErrorResponse> repoNotFound(RepoNotFound e, WebRequest request) {
        ErrorResponse build = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .details(request.getDescription(false))
                .build();

        return new ResponseEntity<>(build, HttpStatus.NOT_FOUND);
    }
}
