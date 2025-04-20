package com.nexus.alumcon.dto.user;

import com.nexus.alumcon.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String collegeName;
    private String city;
    private String bio;
    private Boolean isAlumni;
    private String passOutYear;

    public UserBasicDTO(User user) {
        setUsername(user.getUsername());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setJobTitle(user.getJobTitle());
        setCollegeName(user.getCollegeName());
        setCity(user.getCity());
        setBio(user.getBio());
        setIsAlumni(user.getIsAlumni());
        setPassOutYear(user.getPassOutYear());
    }
}
