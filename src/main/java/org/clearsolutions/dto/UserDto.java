package org.clearsolutions.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserDto {

    protected String firstName;

    protected String lastName;

    protected String email;

    protected LocalDate birthDate;

    protected String address;

    protected String phoneNumber;
}
