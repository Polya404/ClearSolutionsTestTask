package org.clearsolutions.controller;

import lombok.RequiredArgsConstructor;
import org.clearsolutions.dto.UserRegistrationRequest;
import org.clearsolutions.dto.UserRequestResponse;
import org.clearsolutions.exception.InvalidDataException;
import org.clearsolutions.service.UserService;
import org.clearsolutions.util.RequestValidator;
import org.clearsolutions.util.ValidationResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.clearsolutions.exception.ErrorMessage.EXIST_USER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/register")
public class AuthController {
    private final UserService userService;
    private final RequestValidator validator;

    @PostMapping("/save")
    public UserRequestResponse registration(@RequestBody UserRegistrationRequest user,
                                            BindingResult result) {
        ValidationResult validate = validator.validate(user);
        if (validate.isRequestValid()) {
            UserRequestResponse existingUser = userService.findUserByEmail(user.getEmail());
            if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
                result.rejectValue("email",
                        EXIST_USER);
            }
            return userService.createUser(user);
        }
        throw new InvalidDataException(validate.getErrorCause(), user);
    }
}
