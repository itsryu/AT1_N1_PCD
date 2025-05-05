package validator;

@FunctionalInterface
public interface CPFValidationStrategy {
    boolean validate(String cpf);

    default CPFValidationStrategy and(CPFValidationStrategy other) {
        return cpf -> this.validate(cpf) && other.validate(cpf);
    }
}
