
package com.example.search;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

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

        System.out.println("Enter root directories separated by commas:");
        String input = sc.nextLine();

        List<Path> directories = Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(java.io.File::new)
                .map(java.io.File::toPath)
                .collect(java.util.stream.Collectors.toList());
        
        if (directories.isEmpty()) {
            System.out.println("Please enter at least one directory.");
            sc.close();
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());

        List<Future<List<Path>>> futures = new ArrayList<>();

        try {
            for (Path directory : directories) {
                futures.add(executor.submit(
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
                allMatches.forEach(System.out::println);
                System.out.println("\nTotal matches: " + allMatches.size());
            }

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
