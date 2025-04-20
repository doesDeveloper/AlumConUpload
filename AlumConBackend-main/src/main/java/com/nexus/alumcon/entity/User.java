package com.nexus.alumcon.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String bio;
    private String email;
    private String username;
    private String password;
    private String role; // Security field
    private String phone;
    private String address; // address lines
    private String city;
    private String zip;
    private String education; // what do you mean by this???
    private String schoolName;
    private String collegeName;
    private String skills;
    private String interests;
    private String branch;
    private String jobTitle;
    //    private String jobName;
    @Builder.Default
    private Boolean isAlumni = false;
    private String passOutYear;
    @Builder.Default
    private List<String> chats = new ArrayList<>();


}
