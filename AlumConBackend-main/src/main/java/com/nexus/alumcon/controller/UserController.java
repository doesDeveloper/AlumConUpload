package com.nexus.alumcon.controller;

import com.nexus.alumcon.dto.user.UserBasicDTO;
import com.nexus.alumcon.dto.user.UserProfProfileDTO;
import com.nexus.alumcon.dto.user.UserProfileDTO;
import com.nexus.alumcon.entity.User;
import com.nexus.alumcon.service.UserProfileDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.nexus.alumcon.controller.AuthController.validatePassYear;

@RestController
@RequestMapping("/api/users/")
public class UserController {
    @Autowired
    private UserProfileDetailsService userProfileDetailsService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getMe(Principal principal) {
//        System.out.println(principal.getName());
        User user = userProfileDetailsService.findByUsername(principal.getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserProfileDTO userProfileDTO = new UserProfileDTO(user);
        return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
    }

    @GetMapping("/feed")
    public HttpEntity<Page<UserBasicDTO>> getFeed(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
//        // only return users that are alumni
//        Page<User> users = userProfileDetailsService.getAlumniUsers(page, size);
//        return ResponseEntity.ok(users.map(UserBasicDTO::new));
//    }
        Page<User> users = userProfileDetailsService.getAllUsers(page, size);
        return ResponseEntity.ok(users.map(UserBasicDTO::new));
    }

    @GetMapping("/alumnis")
    public HttpEntity<Page<UserBasicDTO>> getAlumnis(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size, Principal principal) {
        // only return users that are alumni
        if (principal.getName().isEmpty())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        User user = userProfileDetailsService.findByUsername(principal.getName());
        if (user.getCollegeName().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Page<User> users = userProfileDetailsService.getAlumniUsers(page, size, user.getCollegeName());
        return ResponseEntity.ok(users.map(UserBasicDTO::new));
    }

    @GetMapping("/searchalumnis")
    public HttpEntity<Page<UserBasicDTO>> searchAlumnis(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "jobTitle", required = false) String jobTitle,
//          below is for sorting and pagination
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy,
//            Authentication
            Principal principal
    ) {
        // only return users that are alumni
        if (principal.getName().isEmpty())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        User user = userProfileDetailsService.findByUsername(principal.getName());
        if (user.getCollegeName().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        List<String> allowedSortBy = List.of("firstName", "lastName", "city", "branch", "jobTitle");
        if (!allowedSortBy.contains(sortBy)) {
            sortBy = "firstName";
        }
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<User> users = userProfileDetailsService.searchAlumniUsers(firstName, lastName, city, branch, jobTitle, user.getCollegeName(), PageRequest.of(page, 10, sort));
        return ResponseEntity.ok(users.map(UserBasicDTO::new));
    }

    @GetMapping("/search")
    public HttpEntity<Page<UserBasicDTO>> searchUsers(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "jobTitle", required = false) String jobTitle,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy
    ) {
        // handle input injection people can sort by other fields
        List<String> allowedSortBy = List.of("firstName", "lastName", "city", "branch", "jobTitle", "collegeName");
        if (!allowedSortBy.contains(sortBy)) {
            sortBy = "firstName";
        }
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<User> users = userProfileDetailsService.searchUsers(firstName, lastName, city, branch, jobTitle, PageRequest.of(page, 10, sort));
        return ResponseEntity.ok(users.map(UserBasicDTO::new));
    }


    @PostMapping("/update")
    public HttpEntity<UserProfileDTO> updateProfile(@RequestBody UserProfileDTO userProfileDTO, Principal principal) {
        if (!userProfileDTO.getUsername().equals(principal.getName())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userProfileDetailsService.findByUsername(principal.getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ResponseEntity<String> body = validatePassYear(userProfileDTO.getPassOutYear());
        if (body != null) return new ResponseEntity<>(body.getStatusCode());
        user.setFirstName(userProfileDTO.getFirstName());
        user.setLastName(userProfileDTO.getLastName());
        user.setBio(userProfileDTO.getBio());
        user.setCity(userProfileDTO.getCity());
        user.setZip(userProfileDTO.getZip());
        user.setJobTitle(userProfileDTO.getJobTitle());
        user.setInterests(userProfileDTO.getInterests());
        user.setSkills(userProfileDTO.getSkills());
        user.setBranch(userProfileDTO.getBranch());
        user.setEducation(userProfileDTO.getEducation());
        user.setSchoolName(userProfileDTO.getSchoolName());
        user.setCollegeName(userProfileDTO.getCollegeName());
        user.setIsAlumni(userProfileDTO.getIsAlumni());
        user.setPassOutYear(userProfileDTO.getPassOutYear());
        user.setChats(userProfileDTO.getChats());
        return ResponseEntity.ok(new UserProfileDTO(userProfileDetailsService.saveUserDetails(user)));

    }

    @GetMapping("/profile/{username}")
    public HttpEntity<UserProfProfileDTO> viewProfile(@PathVariable("username") String username) {
        User user = userProfileDetailsService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserProfProfileDTO userProfProfileDTO = new UserProfProfileDTO(user);
        return ResponseEntity.ok(userProfProfileDTO);
    }


    @GetMapping("/me/chats")
    public HttpEntity<List<String>> getChats(Principal principal) {
        User user = userProfileDetailsService.findByUsername(principal.getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user.getChats());
    }

    @PostMapping("/me/chats")
    public HttpEntity<List<String>> addChat(Principal principal, @RequestBody String chatuser) {
        User user = userProfileDetailsService.findByUsername(principal.getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        chatuser = chatuser.trim();
        if (chatuser.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!user.getChats().contains(chatuser)) {
            User chatUser = userProfileDetailsService.findByUsername(chatuser);
            if (chatUser == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // Self user add chat
            user.getChats().add(chatuser);
            // Remote user add chat
            if (!chatUser.getChats().contains(user.getUsername()))
                chatUser.getChats().add(user.getUsername());
            userProfileDetailsService.saveUserDetails(user);
            userProfileDetailsService.saveUserDetails(chatUser);
        }
        return ResponseEntity.ok(user.getChats());
    }
}
