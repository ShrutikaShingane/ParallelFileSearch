
package com.example.search;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SearchTask implements Callable<List<Path>> {

    private final Path rootDirectory;
    private final String keyword;

    public SearchTask(Path rootDirectory, String keyword) {
        this.rootDirectory = rootDirectory;
        this.keyword = keyword.toLowerCase();
    }

    @Override
    public List<Path> call() throws IOException {

        List<Path> matches = new ArrayList<>();

        if (!Files.isDirectory(rootDirectory)) {
            System.out.println("Invalid directory: " + rootDirectory);
            return matches;
        }

        Files.walkFileTree(rootDirectory,
                new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(
                    Path file, BasicFileAttributes attrs) {

                if (file.getFileName().toString()
                        .toLowerCase()
                        .contains(keyword)) {
                    matches.add(file);
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(
                    Path file, IOException exc) {

                System.out.println(
                        "Skipping inaccessible path: " + file);

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(
                    Path dir, IOException exc) {

                if (exc != null) {
                    System.out.println(
                            "Could not fully scan: " + dir);
                }

                return FileVisitResult.CONTINUE;
            }
        });

        return matches;
    }
}
