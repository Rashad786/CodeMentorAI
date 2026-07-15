package com.rashad.CodeMentorAI.service;

import com.rashad.CodeMentorAI.dto.RepositoryFile;
import com.rashad.CodeMentorAI.dto.RepositorySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RepoParserService {

    @Value("${clone-directory}")
    private String CLONE_DIRECTORY;

    private final GitService gitService;

    private static final List<String> IGNORED_DIRECTORIES = List.of(
            ".git",
            "target",
            "build",
            "node_modules",
            ".idea"
    );

    public RepositorySummary parseRepo(String url) {

        String repoName = gitService.extractRepositoryName(url);
        System.out.println(repoName);

        Path repoPath = Paths.get(CLONE_DIRECTORY, repoName);

        System.out.println("Repo Name : " + repoName);
        System.out.println("Repo Path : " + repoPath.toAbsolutePath());
        System.out.println("Exists    : " + Files.exists(repoPath));

        if (!Files.exists(repoPath)) {
            throw new IllegalArgumentException("Repository not found");
        }

        List<RepositoryFile> files = new ArrayList<>();

        int totalFolders = 0;
        long totalLines = 0;

        try(Stream<Path> paths = Files.walk(repoPath)) {

            for(Path path: paths.toList()) {

                if(shouldIgnore(path)) {
                    continue;
                }

                if(Files.isDirectory(path)) {
                    totalFolders++;
                    continue;
                }

                RepositoryFile repositoryFile = createRepositoryFile(repoPath, path);

                totalLines += repositoryFile.getLineCount();

                files.add(repositoryFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return RepositorySummary.builder()
                .files(files)
                .totalFolders(totalFolders)
                .totalLines(totalLines)
                .totalFiles(files.size())
                .build();
    }

    private RepositoryFile createRepositoryFile(Path repoPath, Path path) throws IOException {
        RepositoryFile file = new RepositoryFile();

        file.setFileName(path.getFileName().toString());
        file.setRelativePath(repoPath.relativize(path).toString());
        file.setExtension(getExtension(path));

        String content = Files.readString(path);

        file.setContent(content);
        file.setLineCount(content.lines().count());

        return file;
    }

    private String getExtension(Path path) {

        String name = path.getFileName().toString();

        int idx = name.lastIndexOf(".");
        if (idx == -1) {
            return "";
        }

        return name.substring(idx + 1);
    }

    private boolean shouldIgnore(Path path) {

        for (Path part : path) {

            if (IGNORED_DIRECTORIES.contains(part.toString())) {
                return true;
            }
        }

        return false;
    }
}
