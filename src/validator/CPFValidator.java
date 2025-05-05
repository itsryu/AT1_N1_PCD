package validator;

import java.util.stream.IntStream;

public class CPFValidator implements CPFValidationStrategy {
    private static final int[] PESO_DV1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESO_DV2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    @Override
    public boolean validate(String cpf) {
        if (cpf == null) return false;

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || hasAllSameDigits(cpf)) {
            return false;
        }

        return validateDigit(cpf, 9, PESO_DV1) &&
                validateDigit(cpf, 10, PESO_DV2);
    }

    private boolean hasAllSameDigits(String cpf) {
        return cpf.chars().allMatch(c -> c == cpf.charAt(0));
    }

    private boolean validateDigit(String cpf, int digitPosition, int[] weights) {
        int sum = IntStream.range(0, weights.length)
                .map(i -> (cpf.charAt(i) - '0') * weights[i])
                .sum();

        int digit = (sum * 10) % 11;
        digit = digit == 10 ? 0 : digit;

        return digit == (cpf.charAt(digitPosition) - '0');
    }
}
