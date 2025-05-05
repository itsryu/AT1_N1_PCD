package processing;

import validator.CPFValidationStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FileProcessorFactory {
    private static final int BUFFER_SIZE = 8192;

    public static Runnable createProcessor(List<Path> files, AtomicInteger validCount, AtomicInteger invalidCount, CPFValidationStrategy validator) {
        return () -> files.parallelStream().forEach(file -> processFile(file, validCount, invalidCount, validator));
    }

    private static void processFile(Path file, AtomicInteger validCount, AtomicInteger invalidCount, CPFValidationStrategy validator) {
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(cpf -> {
                        if (validator.validate(cpf)) {
                            validCount.incrementAndGet();
                        } else {
                            invalidCount.incrementAndGet();
                        }
                    });
        } catch (IOException e) {
            System.err.printf("Erro processando arquivo %s: %s%n", file, e.getMessage());
        }
    }
}