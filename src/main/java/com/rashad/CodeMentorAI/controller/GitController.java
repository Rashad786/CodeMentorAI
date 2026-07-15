package com.rashad.CodeMentorAI.controller;

import com.rashad.CodeMentorAI.dto.CloneRepositoryRequest;
import com.rashad.CodeMentorAI.dto.CloneRepositoryResponse;
import com.rashad.CodeMentorAI.dto.RepositorySummary;
import com.rashad.CodeMentorAI.service.GitService;
import com.rashad.CodeMentorAI.service.RepoParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/git")
@RequiredArgsConstructor
public class GitController {

    private final GitService gitService;
    private final RepoParserService parserService;

    @PostMapping("/clone")
    public ResponseEntity<CloneRepositoryResponse> cloneRepository(@RequestBody CloneRepositoryRequest request) {
        return ResponseEntity.ok(gitService.cloneRepository(request.getUrl()));
    }

    @GetMapping("/summary")
    public ResponseEntity<RepositorySummary> repoSummary(@RequestBody CloneRepositoryRequest request) {
        return ResponseEntity.ok(parserService.parseRepo(request.getUrl()));
    }
}
