
package com.example.search;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter search keyword:");
        String keyword = sc.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("Keyword cannot be empty.");
            sc.close();
            return;
        }

        System.out.println(
                "Enter root directories separated by commas:");

        String input = sc.nextLine();

        List<Path> directories = Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(File::new)
                .map(File::toPath)
                .collect(Collectors.toList());

        if (directories.isEmpty()) {
            System.out.println("Please enter at least one directory.");
            sc.close();
            return;
        }

        int threadCount = Math.max(
                1, Runtime.getRuntime().availableProcessors());

        ExecutorService executor =
                Executors.newFixedThreadPool(threadCount);

        List<Future<List<Path>>> futures = new ArrayList<>();

        try {
            for (Path directory : directories) {
                futures.add(
                        executor.submit(
                                new SearchTask(directory, keyword)));
            }

            List<Path> allMatches = new ArrayList<>();

            for (Future<List<Path>> future : futures) {
                allMatches.addAll(future.get());
            }

            System.out.println("\nMatching files:");

            if (allMatches.isEmpty()) {
                System.out.println("No matching files found.");
            } else {
                for (Path path : allMatches) {
                    System.out.println(path);
                }
            }

            System.out.println(
                    "\nTotal matching files: " + allMatches.size());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Search was interrupted.");

        } catch (ExecutionException e) {
            System.out.println("Search failed: " + e.getCause());

        } finally {
            executor.shutdown();
            sc.close();
        }
    }
}
