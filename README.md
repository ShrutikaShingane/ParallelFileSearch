
# Parallel File Search Utility

## Description
A Java application that searches for files concurrently in multiple directories using ExecutorService.

## Features
- Searches multiple directories in parallel.
- Uses Callable and Future to manage search tasks.
- Displays matching file paths.
- Skips inaccessible files and folders.
- Handles invalid directory paths.

## Technologies Used
- Java
- Eclipse IDE
- Apache Maven

## Concepts Used
- Multithreading
- ExecutorService
- Callable and Future
- Java NIO
- Exception handling

## How to Run
1. Import the project into Eclipse as an Existing Maven Project.
2. Open Main.java.
3. Select Run As → Java Application.
4. Enter a keyword and directory paths.
5. Check the Console for matching files.

## Build
Run `mvn clean package` to build the project.
