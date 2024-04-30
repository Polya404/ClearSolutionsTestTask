package org.clearsolutions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.clearsolutions.dto.UserRegistrationRequest;
import org.clearsolutions.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    private final String PATH = "/api/v1/register/save";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @SneakyThrows
    void registrationSuccess() {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createUserRegistrationRequest())))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void registrationAgeLess18() {
        UserRegistrationRequest request = createUserRegistrationRequest();
        request.setBirthDate(LocalDate.parse("2012-01-01"));
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SneakyThrows
    void registrationInvalidPhoneNumber() {
        UserRegistrationRequest request = createUserRegistrationRequest();
        request.setPhoneNumber("0000000000");
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SneakyThrows
    void registrationInvalidEmail() {
        UserRegistrationRequest request = createUserRegistrationRequest();
        request.setEmail("test.test");
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SneakyThrows
    void registrationEmptyEmail() {
        UserRegistrationRequest request = createUserRegistrationRequest();
        request.setEmail("");
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SneakyThrows
    void registrationEmptyName() {
        UserRegistrationRequest request = createUserRegistrationRequest();
        request.setFirstName("");
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                request)))
                .andExpect(status().is4xxClientError());

        request.setFirstName("test");
        request.setLastName("");
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SneakyThrows
    void registrationEmptyBirthDate() {
        UserRegistrationRequest request = createUserRegistrationRequest();
        request.setBirthDate(null);
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SneakyThrows
    void registrationEmptyPhoneNumber() {
        UserRegistrationRequest request = createUserRegistrationRequest();
        request.setPhoneNumber(null);
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                request)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void registrationEmptyAddress() {
        UserRegistrationRequest request = createUserRegistrationRequest();
        request.setAddress(null);
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                request)))
                .andExpect(status().is2xxSuccessful());
    }



    private UserRegistrationRequest createUserRegistrationRequest() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setFirstName("firstName");
        userRegistrationRequest.setLastName("lastName");
        userRegistrationRequest.setPassword("password");
        userRegistrationRequest.setEmail("email@email.com");
        userRegistrationRequest.setPhoneNumber("+380663786522");
        userRegistrationRequest.setAddress("address");
        userRegistrationRequest.setBirthDate(LocalDate.parse("1999-01-01"));
        return userRegistrationRequest;
    }
}