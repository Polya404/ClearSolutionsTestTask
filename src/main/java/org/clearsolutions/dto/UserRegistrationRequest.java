package org.clearsolutions.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest extends UserDto {
    private String password;
}
