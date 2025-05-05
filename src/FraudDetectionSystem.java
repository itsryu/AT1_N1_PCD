import management.ExecutionManager;
import validator.CPFValidationStrategy;
import validator.CPFValidator;

import java.io.IOException;
import java.nio.file.Path;

public class FraudDetectionSystem {
    private static final Path INPUT_DIR = Path.of("data");
    private static final String OUTPUT_PREFIX = "v";

    public static void main(String[] args) {
        CPFValidationStrategy validator = new CPFValidator();
        ExecutionManager manager = new ExecutionManager(INPUT_DIR, OUTPUT_PREFIX, validator);

        int[] threadConfigs = {1, 2, 3, 5, 6, 10, 15, 30};

        for (int threads : threadConfigs) {
            try {
                System.out.printf("Executando com %d threads...%n", threads);
                manager.executeWithThreads(threads);
                System.out.println("Concluído com sucesso");
            } catch (IOException e) {
                System.err.printf("Falha na execução com %d threads: %s%n", threads, e.getMessage());
            }
        }
    }
}