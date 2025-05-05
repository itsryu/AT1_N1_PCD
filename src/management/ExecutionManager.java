package management;

import processing.FileProcessorFactory;
import validator.CPFValidationStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ExecutionManager {
    private final Path inputDir;
    private final String outputPrefix;
    private final CPFValidationStrategy validator;

    public ExecutionManager(Path inputDir, String outputPrefix, CPFValidationStrategy validator) {
        this.inputDir = Objects.requireNonNull(inputDir);
        this.outputPrefix = Objects.requireNonNull(outputPrefix);
        this.validator = Objects.requireNonNull(validator);
    }

    public void executeWithThreads(int threadCount) throws IOException {
        List<Path> files = getInputFiles();
        if (files.isEmpty()) {
            System.out.println("Nenhum arquivo encontrado para processamento");
            return;
        }

        AtomicInteger validCount = new AtomicInteger();
        AtomicInteger invalidCount = new AtomicInteger();

        List<List<Path>> partitions = partitionFiles(files, threadCount);
        List<Runnable> processors = createProcessors(partitions, validCount, invalidCount);

        long startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        processors.forEach(executor::submit);
        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Execução interrompida");
            return;
        }

        long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        saveResults(threadCount, validCount.get(), invalidCount.get(), duration);
    }

    private List<Path> getInputFiles() throws IOException {
        try (Stream<Path> stream = Files.list(inputDir)) {
            return stream.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    private List<List<Path>> partitionFiles(List<Path> files, int partitions) {
        int size = files.size();
        int chunkSize = (size + partitions - 1) / partitions;

        return IntStream.range(0, partitions)
                .mapToObj(i -> files.subList(i * chunkSize, Math.min(size, (i + 1) * chunkSize)))
                .collect(Collectors.toList());
    }

    private List<Runnable> createProcessors(List<List<Path>> partitions,
                                            AtomicInteger validCount, AtomicInteger invalidCount) {
        return partitions.stream()
                .map(files -> FileProcessorFactory.createProcessor(files, validCount, invalidCount, validator))
                .collect(Collectors.toList());
    }

    private void saveResults(int threadCount, int valid, int invalid, long duration) throws IOException {
        String filename = String.format("%s%d_threads.txt", outputPrefix, threadCount);
        String content = String.format(
                "Threads: %d%nTempo: %d ms%nVálidos: %d%nInválidos: %d%n",
                threadCount, duration, valid, invalid);

        Files.writeString(Path.of("output", filename), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
