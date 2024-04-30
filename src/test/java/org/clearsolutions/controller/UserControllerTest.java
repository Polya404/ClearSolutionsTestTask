package org.clearsolutions.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.clearsolutions.dto.UserRequestResponse;
import org.clearsolutions.model.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.clearsolutions.service.RoleService;
import org.clearsolutions.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class UserControllerTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    private final String PATH = "/api/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    private final Long id = 1L;

    @BeforeAll
    static void startContainer() {
        postgreSQLContainer.start();
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }

    @AfterAll
    static void stopContainer() {
        postgreSQLContainer.stop();
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void getUserById() {
        when(userService.findUserById(id)).thenReturn(createUserRequestResponse());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/get/{id}", id))
                .andExpect(status().isOk())
                .andReturn();

        UserRequestResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), UserRequestResponse.class);
        assertEquals(id, response.getId());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void getAllUsers() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/get"))
                .andExpect(status().isOk())
                .andReturn();
        List<UserRequestResponse> responses = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(responses);
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void deleteUserById() {
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/delete/{id}", id))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteUserById(id);
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void deleteUserByIdWithoutRole() {
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/delete/{id}", id))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void addRole() {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/role/add/{userId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createListOfRoles().get(1))))
                .andExpect(status().isOk());

        verify(roleService, times(1)).addRoleToUser(id, createListOfRoles().get(1).getId());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void addRoleWithoutRole() {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/role/add/{userId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createListOfRoles().get(1))))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void deleteRole() {
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/role/delete/{roleId}", id))
                .andExpect(status().isOk());

        verify(roleService, times(1)).deleteRoleById(id);
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void deleteRoleWithoutRole() {
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/role/delete/{roleId}", id))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void getUserRoles() {
        List<Role> roles = createListOfRoles();
        when(roleService.getRoleByUserId(id)).thenReturn(roles);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/role/get/{userId}", id))
                .andExpect(status().isOk())
                .andReturn();

        List<Role> responseRoles = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(roles.size(), responseRoles.size());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void getUserRolesWithoutRole() {
        List<Role> roles = createListOfRoles();
        when(roleService.getRoleByUserId(id)).thenReturn(roles);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/role/get/{userId}", id))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void getRoles() {
        List<Role> roles = createListOfRoles();
        when(roleService.getAllExistRoles()).thenReturn(roles);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/role/get"))
                .andExpect(status().isOk())
                .andReturn();

        List<Role> responseRoles = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(roles.size(), responseRoles.size());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void getRolesWithoutRole() {
        List<Role> roles = createListOfRoles();
        when(roleService.getAllExistRoles()).thenReturn(roles);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/role/get"))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void changeUserEmail() {
        String newEmail = "new_email@email.com";
        mockMvc.perform(MockMvcRequestBuilders.patch(PATH + "/update/email/{id}", id)
                        .param("email", newEmail))
                .andExpect(status().isOk());

        verify(userService, times(1)).changeUserEmail(id, newEmail);
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void changeUserPhoneNumber() {
        String newPhoneNumber = "+380663786533";
        mockMvc.perform(MockMvcRequestBuilders.patch(PATH + "/update/phoneNumber/{id}", id)
                        .param("phoneNumber", newPhoneNumber))
                .andExpect(status().isOk());

        verify(userService, times(1)).changeUserPhoneNumber(id, newPhoneNumber);

    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void updateUser() {
        UserRequestResponse updateUserRequest = createUserRequestResponse();
        updateUserRequest.setFirstName("updatedFirstName");
        updateUserRequest.setLastName("updatedLastName");

        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void searchByBirthDate() {
        UserRequestResponse response1 = createUserRequestResponse();
        UserRequestResponse response2 = createUserRequestResponse();
        UserRequestResponse response3 = createUserRequestResponse();
        UserRequestResponse response4 = createUserRequestResponse();
        setParam(response1, response2, response3, response4);
        LocalDate from = LocalDate.parse("1999-01-01");
        LocalDate to = LocalDate.parse("1999-04-01");

        List<UserRequestResponse> users = Arrays.asList(response2, response3);

        when(userService.searchForUsersByBirthDate(from, to)).thenReturn(users);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/get/birthDate")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(status().isOk())
                .andReturn();

        List<UserRequestResponse> responseUsers = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(users.size(), responseUsers.size());
    }

    private List<Role> createListOfRoles() {
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("USER");
        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ADMIN");
        return Arrays.asList(role1, role2);
    }

    private static void setParam(UserRequestResponse response1, UserRequestResponse response2,
                                 UserRequestResponse response3, UserRequestResponse response4) {
        response1.setBirthDate(LocalDate.parse("1998-01-01"));
        response2.setBirthDate(LocalDate.parse("1999-01-01"));
        response3.setBirthDate(LocalDate.parse("1999-02-01"));
        response4.setBirthDate(LocalDate.parse("1999-05-01"));
    }


    private UserRequestResponse createUserRequestResponse() {
        UserRequestResponse userRequestResponse = new UserRequestResponse();
        userRequestResponse.setId(1L);
        userRequestResponse.setEmail("email@email.com");
        userRequestResponse.setFirstName("firstName");
        userRequestResponse.setLastName("lastName");
        userRequestResponse.setAddress("address");
        userRequestResponse.setPhoneNumber("+380663786522");
        userRequestResponse.setBirthDate(LocalDate.parse("1999-01-01"));
        return userRequestResponse;
    }
}