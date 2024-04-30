package org.clearsolutions.controller;

import lombok.RequiredArgsConstructor;
import org.clearsolutions.dto.UserRequestResponse;
import org.clearsolutions.exception.InvalidDataException;
import org.clearsolutions.model.Role;
import org.clearsolutions.service.RoleService;
import org.clearsolutions.service.UserService;
import org.clearsolutions.util.RequestValidator;
import org.clearsolutions.util.ValidationResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final RequestValidator validator;

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserRequestResponse getUserById(@PathVariable final Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<UserRequestResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@PathVariable final Long id) {
        userService.deleteUserById(id);
    }

    @PostMapping("/role/add/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addRole(@PathVariable final Long userId, @RequestBody final Role role) {
        roleService.addRoleToUser(userId, role.getId());
    }

    @DeleteMapping("/role/delete/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRole(@PathVariable final Long roleId) {
        roleService.deleteRoleById(roleId);
    }

    @GetMapping("/role/get/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Role> getUserRoles(@PathVariable final Long userId) {
        return roleService.getRoleByUserId(userId);
    }

    @GetMapping("/role/get")
    @ResponseStatus(HttpStatus.OK)
    public List<Role> getRoles() {
        return roleService.getAllExistRoles();
    }

    @PatchMapping("/update/email/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void changeUserEmail(@PathVariable final Long id, @RequestParam final String email) {
        ValidationResult validated = validator.validateEmailAddress(email);
        if (validated.isRequestValid()) {
            userService.changeUserEmail(id, email);
        } else {
            throw new InvalidDataException(validated.getErrorCause(), email);
        }
    }

    @PatchMapping("/update/phoneNumber/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void changeUserPhoneNumber(@PathVariable final Long id, @RequestParam final String phoneNumber) {
        ValidationResult validated = validator.validatePhoneNumber(phoneNumber);
        if (validated.isRequestValid()) {
            userService.changeUserPhoneNumber(id, phoneNumber);
        } else {
            throw new InvalidDataException(validated.getErrorCause(), phoneNumber);
        }
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public UserRequestResponse updateUser(@RequestBody final UserRequestResponse user) {
        ValidationResult validated = validator.validate(user);
        if (validated.isRequestValid()) {
            return userService.updateUser(user);
        }
        throw new InvalidDataException(validated.getErrorCause(), user);
    }

    @GetMapping("/get/birthDate")
    @ResponseStatus(HttpStatus.OK)
    public List<UserRequestResponse> searchByBirthDate(@RequestParam final LocalDate from,
                                        @RequestParam final LocalDate to) {
        ValidationResult validated = validator.checkDateRange(from, to);
        if (validated.isRequestValid()) {
            return userService.searchForUsersByBirthDate(from, to);
        }
        throw new InvalidDataException(validated.getErrorCause());
    }
}
