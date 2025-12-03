package com.DullPointers.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Report {

    private String saveReport(String report) {
        // 1. Format date to remove illegal characters (like ':')
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);

        // 2. Add a file extension (e.g., .txt)
        String fileName = "Report-" + timestamp + ".csv";

        try {
            // 3. Write the content to the file
            // usage: Path, Content, Options (Create if not exists, Truncate if exists)
            Files.writeString(Path.of(fileName), report,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Saved successfully: " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to save report: " + e.getMessage());
            e.printStackTrace();
            return "Failed to Generate Report";
        }
        return fileName;
    }

    protected abstract String createReport();

    public final String generateReport() {
        return saveReport(createReport());
    }
}
