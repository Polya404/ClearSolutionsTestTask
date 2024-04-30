package org.clearsolutions.util;

import lombok.RequiredArgsConstructor;
import org.clearsolutions.dto.UserDto;
import org.clearsolutions.exception.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RequestValidator {

    private static final String PHONE_NUMBER_PATTERN = "^([+]{1})[0-9]{8,17}$";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    @Value("${minAge}")
    private Integer minAge;

    public ValidationResult validate(final UserDto user) {
        ValidationResult validationResult = new ValidationResult(true);
        if (Objects.nonNull(user.getPhoneNumber())) {
            checkPhoneNumber(user.getPhoneNumber(), validationResult);
        }
        checkName(user, validationResult);
        checkEmail(user.getEmail(), validationResult);
        checkAge(user.getBirthDate(), validationResult);
        return validationResult;
    }

    private void checkName(UserDto user, ValidationResult validationResult) {
        if (user.getFirstName() == null || user.getLastName() == null
                || user.getFirstName().isEmpty() || user.getLastName().isEmpty()) {
            validationResult.setRequestValid(false);
            validationResult.setErrorCause(ErrorMessage.NAME_DOES_NOT_EXIST);
        }
    }

    private void checkAge(final LocalDate birthDate, final ValidationResult validationResult) {
        if (birthDate == null) {
            validationResult.setRequestValid(false);
            validationResult.setErrorCause(ErrorMessage.AGE_IS_REQUIRED);
            return;
        }
        LocalDate currentDate = LocalDate.now();
        Period ageDifference = Period.between(birthDate, currentDate);
        if (ageDifference.getYears() < minAge) {
            validationResult.setRequestValid(false);
            validationResult.setErrorCause(ErrorMessage.INVALID_AGE);
        }
    }

    public ValidationResult checkDateRange(final LocalDate from, final LocalDate to) {
        ValidationResult validationResult = new ValidationResult(true);
        if (from.isAfter(to)) {
            validationResult.setErrorCause(ErrorMessage.INVALID_RANGE_DATE);
        }
        return validationResult;
    }

    public ValidationResult validatePhoneNumber(final String phoneNumber) {
        ValidationResult validationResult = new ValidationResult(true);
        checkPhoneNumber(phoneNumber, validationResult);
        return validationResult;
    }

    public ValidationResult validateEmailAddress(final String emailAddress) {
        ValidationResult validationResult = new ValidationResult(true);
        checkEmail(emailAddress, validationResult);
        return validationResult;
    }

    private static void checkEmail(String emailAddress, ValidationResult validationResult) {
        if (emailAddress == null || !Pattern.matches(EMAIL_PATTERN, emailAddress)) {
            validationResult.setRequestValid(false);
            validationResult.setErrorCause(ErrorMessage.EMAIL_ADDRESS_NOT_VALID);
        }
    }

    private static void checkPhoneNumber(String phoneNumber, ValidationResult validationResult) {
        if (phoneNumber == null || !Pattern.matches(PHONE_NUMBER_PATTERN, phoneNumber)) {
            validationResult.setRequestValid(false);
            validationResult.setErrorCause(ErrorMessage.PHONE_NUMBER_NOT_VALID);
        }
    }
}

