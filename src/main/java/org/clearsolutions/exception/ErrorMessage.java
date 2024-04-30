package org.clearsolutions.exception;


public record ErrorMessage() {
    public static final String USER_NOT_FOUND = "User [%s] not found";
    public static final String EXIST_USER = "There is already an account registered with the same email";
    public static final String EMAIL_ADDRESS_NOT_VALID = "Email address not valid";
    public static final String PHONE_NUMBER_NOT_VALID = "Phone number not valid";
    public static final String INVALID_RANGE_DATE = "The start date must be before the end date";
    public static final String INVALID_AGE = "Must be at least 18 years old";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String NAME_DOES_NOT_EXIST = "First name and last name are required";
    public static final String AGE_IS_REQUIRED = "Age is required";
}
