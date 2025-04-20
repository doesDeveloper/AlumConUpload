package com.nexus.alumcon.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RegisterUserDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String bio;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    private String phone;
    private String address;
    private String city;
    private String zip;
    private String education;
    private String schoolName;
    private String collegeName;
    private String skills;
    private String interests;
    private String branch;
    private String jobTitle;
    private Boolean isAlumni;
    private String passOutYear;
    private List<String> chats;
}
