package com.rashad.CodeMentorAI.controller;

import com.rashad.CodeMentorAI.dto.CloneRepositoryRequest;
import com.rashad.CodeMentorAI.dto.CloneRepositoryResponse;
import com.rashad.CodeMentorAI.service.GitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/git")
@RequiredArgsConstructor
public class GitController {

    private final GitService gitService;

    @PostMapping("/clone")
    public ResponseEntity<CloneRepositoryResponse> cloneRepository(@RequestBody CloneRepositoryRequest request) {
        return ResponseEntity.ok(gitService.cloneRepository(request.getUrl()));
    }
}
