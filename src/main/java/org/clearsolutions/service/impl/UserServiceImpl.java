package org.clearsolutions.service.impl;

import lombok.RequiredArgsConstructor;
import org.clearsolutions.dto.UserRegistrationRequest;
import org.clearsolutions.dto.UserRequestResponse;
import org.clearsolutions.exception.UserNotFoundException;
import org.clearsolutions.mapper.UserRegistrationRequestMapper;
import org.clearsolutions.mapper.UserRequestResponseMapper;
import org.clearsolutions.model.Roles;
import org.clearsolutions.model.User;
import org.clearsolutions.repository.UserRepository;
import org.clearsolutions.repository.UserRoleRepository;
import org.clearsolutions.service.RoleService;
import org.clearsolutions.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.clearsolutions.exception.ErrorMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserRequestResponseMapper requestResponseMapper;
    private final UserRegistrationRequestMapper registrationRequestMapper;


    @Override
    public UserRequestResponse createUser(UserRegistrationRequest user) {
        User entity = registrationRequestMapper.toEntity(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(entity);
        setRole(entity);
        return requestResponseMapper.toDto(saved);
    }

    private void setRole(User user) {
        Long unverifiedRoleId = roleService.findRoleByName(Roles.ROLE_USER.name().toUpperCase(Locale.ROOT)).getId();
        roleService.addRoleToUser(user.getId(), unverifiedRoleId);
    }

    @Override
    public UserRequestResponse findUserByEmail(String email) {
        return requestResponseMapper.toDto(userRepository.findByEmail(email));
    }

    @Override
    public UserRequestResponse findUserById(Long id) {
        return requestResponseMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, id)));
    }

    @Override
    public void deleteUserById(Long id) {
        userRoleRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    @Override
    public List<UserRequestResponse> getAllUsers() {
        return requestResponseMapper.toDtos(userRepository.findAll());
    }

    @Override
    public UserRequestResponse updateUser(UserRequestResponse user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, user.getId()));
        BeanUtils.copyProperties(user, existingUser, "id");
        return requestResponseMapper.toDto(userRepository.save(existingUser));
    }

    @Override
    public List<UserRequestResponse> searchForUsersByBirthDate(LocalDate from, LocalDate to) {
        return requestResponseMapper.toDtos(userRepository.findUsersByBirthDateBetween(from, to));
    }

    @Override
    public void changeUserEmail(Long userId, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, userId));
        user.setEmail(email);
        userRepository.save(user);
    }

    @Override
    public void changeUserPhoneNumber(Long userId, String phoneNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, userId));
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }
}
