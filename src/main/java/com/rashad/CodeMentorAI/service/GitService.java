package com.rashad.CodeMentorAI.service;

import com.rashad.CodeMentorAI.dto.CloneRepositoryResponse;
import com.rashad.CodeMentorAI.exception.RepoNotFound;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class GitService {

    @Value("${CLONE_DIRECTORY}")
    private String CLONE_DIRECTORY;

    public CloneRepositoryResponse cloneRepository(String url) {

        if (!isUrlValid(url)) {
            throw new IllegalArgumentException("Invalid GitHub repository URL.");
        }

        String repoName = extractRepositoryName(url);

        if (repositoryExists(repoName)) {
            log.info("Repository already exists.");
            return CloneRepositoryResponse.builder()
                    .repoName(repoName)
                    .msg("Repository already exists.")
                    .success(false)
                    .build();
        }

        File directory = Paths.get(CLONE_DIRECTORY, repoName).toFile();

        try {

            Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(directory)
                    .call();

            log.info("Repository {} cloned successfully.", repoName);

            return CloneRepositoryResponse.builder()
                    .repoName(repoName)
                    .msg("Repository cloned successfully.")
                    .path(directory.getAbsolutePath())
                    .success(true)
                    .build();

        } catch (TransportException e) {

            log.error("Repository not found: {}", url, e);
            throw new RepoNotFound("Repository not found or access denied.");

        } catch (GitAPIException e) {

            log.error("Git clone failed.", e);
            throw new RuntimeException("Unable to clone repository.");

        }
    }

    public String extractRepositoryName(String url) {
        // Example:
        // https://github.com/spring-projects/spring-boot.git

        String repoName = url.substring(url.lastIndexOf("/") + 1);

        if (repoName.endsWith(".git")) {
            repoName = repoName.substring(0, repoName.length() - 4);
        }

        return repoName;
    }


    public boolean repositoryExists(String repoName) {
        Path repoPath = Paths.get(CLONE_DIRECTORY, repoName);

        return repoPath.toFile().exists();
    }

    public boolean isUrlValid(String url) {

        if (url == null || url.isBlank()) {
            return false;
        }

        return url.matches("^https://github\\.com/[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+(\\.git)?/?$");
    }
}
