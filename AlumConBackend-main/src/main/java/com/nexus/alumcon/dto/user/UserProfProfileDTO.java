package com.nexus.alumcon.dto.user;

import com.nexus.alumcon.entity.User;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfProfileDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String city;
    private String education; // what do you mean by this???
    private String schoolName;
    private String collegeName;
    private String skills;
    private String interests;
    private String branch;
    private String jobTitle;
    private Boolean isAlumni;
    private String passOutYear;

    public UserProfProfileDTO(User user) {
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setBio(user.getBio());
        setCity(user.getCity());
        setEducation(user.getEducation());
        setSchoolName(user.getSchoolName());
        setCollegeName(user.getCollegeName());
        setSkills(user.getSkills());
        setInterests(user.getInterests());
        setBranch(user.getBranch());
        setJobTitle(user.getJobTitle());
        setUsername(user.getUsername());
        setIsAlumni(user.getIsAlumni());
        setPassOutYear(user.getPassOutYear());
    }
}
