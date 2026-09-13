package com.livrotech.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.isBlank()) {
            return true;
        }

        if (!cpf.matches("\\d{11}") || cpf.chars().distinct().count() == 1) {
            return false;
        }

        int firstDigit = calculateDigit(cpf.substring(0, 9), 10);
        int secondDigit = calculateDigit(cpf.substring(0, 10), 11);

        return firstDigit == Character.digit(cpf.charAt(9), 10)
                && secondDigit == Character.digit(cpf.charAt(10), 10);
    }

    private int calculateDigit(String value, int initialWeight) {
        int sum = 0;
        for (int index = 0; index < value.length(); index++) {
            sum += Character.digit(value.charAt(index), 10) * (initialWeight - index);
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
