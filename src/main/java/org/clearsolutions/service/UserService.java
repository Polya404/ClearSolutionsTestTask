package org.clearsolutions.service;

import org.clearsolutions.dto.UserRegistrationRequest;
import org.clearsolutions.dto.UserRequestResponse;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    UserRequestResponse createUser(UserRegistrationRequest user);

    UserRequestResponse findUserByEmail(String email);

    UserRequestResponse findUserById(Long id);

    void deleteUserById(Long id);

    List<UserRequestResponse> getAllUsers();

    UserRequestResponse updateUser(UserRequestResponse user);

    List<UserRequestResponse> searchForUsersByBirthDate(LocalDate from, LocalDate to);

    void changeUserEmail(Long userId, String email);

    void changeUserPhoneNumber(Long userId, String phoneNumber);
}
