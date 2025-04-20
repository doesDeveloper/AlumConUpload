package com.nexus.alumcon.dto.user;

import com.nexus.alumcon.entity.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String bio;
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
    private Boolean isAlumni;
    private String passOutYear;
    private List<String> chats;
    public UserProfileDTO(User user) {
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setBio(user.getBio());
        setEmail(user.getEmail());
        setUsername(user.getUsername());
        setPhone(user.getPhone());
        setAddress(user.getAddress());
        setCity(user.getCity());
        setZip(user.getZip());
        setEducation(user.getEducation());
        setSchoolName(user.getSchoolName());
        setCollegeName(user.getCollegeName());
        setSkills(user.getSkills());
        setInterests(user.getInterests());
        setBranch(user.getBranch());
        setJobTitle(user.getJobTitle());
        setIsAlumni(user.getIsAlumni());
        setPassOutYear(user.getPassOutYear());
        setChats(user.getChats());
    }


}
