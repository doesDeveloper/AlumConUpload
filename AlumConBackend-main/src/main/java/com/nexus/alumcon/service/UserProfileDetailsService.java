package com.nexus.alumcon.service;

import com.nexus.alumcon.dto.user.RegisterUserDTO;
import com.nexus.alumcon.entity.User;
import com.nexus.alumcon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class UserProfileDetailsService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User saveUserDetails(RegisterUserDTO user) {
        User newUser = User.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .bio(user.getBio())
                .phone(user.getPhone())
                .address(user.getAddress())
                .city(user.getCity())
                .zip(user.getZip())
                .education(user.getEducation())
                .schoolName(user.getSchoolName())
                .collegeName(user.getCollegeName())
                .skills(user.getSkills())
                .interests(user.getInterests())
                .branch(user.getBranch())
                .jobTitle(user.getJobTitle())
                .isAlumni(user.getIsAlumni())
                .passOutYear(user.getPassOutYear())
                .chats(user.getChats())
                .role("USER")
                .build();
        return userRepository.save(newUser);
    }


    public User saveUserDetails(User user) {
        return userRepository.save(user);
    }

    public Page<User> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    public Page<User> searchUsers(String firstName, String lastName, String city, String branch, String jobTitle, Pageable pageable) {
        User userProbe = new User();
        userProbe.setFirstName(firstName);
        userProbe.setLastName(lastName);
        userProbe.setCity(city);
        userProbe.setBranch(branch);
        userProbe.setJobTitle(jobTitle);
//        userProbe.setIsAlumni(false);
        System.out.println(userProbe);
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("id", "password", "role", "isAlumni", "chats");;
        return userRepository.findAll(Example.of(userProbe, matcher), pageable);
    }

    public Page<User> searchAlumniUsers(String firstName, String lastName, String city, String branch, String jobTitle, String collegeName, Pageable pageable) {
        User userProbe = new User();
        userProbe.setFirstName(firstName);
        userProbe.setLastName(lastName);
        userProbe.setCity(city);
        userProbe.setBranch(branch);
        userProbe.setJobTitle(jobTitle);
        userProbe.setCollegeName(collegeName);
        userProbe.setIsAlumni(true);
        System.out.println(userProbe);
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("id", "password", "role", "chats");;
        return userRepository.findAll(Example.of(userProbe, matcher), pageable);
    }

    public Page<User> getAlumniUsers(int page, int size, String collegeName) {
        System.out.println(collegeName);
        User userProbe = new User();
        userProbe.setIsAlumni(true);
        userProbe.setCollegeName(collegeName);
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        System.out.println(userProbe.toString());
        return userRepository.findAll(Example.of(userProbe, matcher), PageRequest.of(page, size));
    }
}