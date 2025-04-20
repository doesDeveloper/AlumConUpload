package com.nexus.alumcon.controller;

import com.nexus.alumcon.dto.AuthRequestDTO;
import com.nexus.alumcon.dto.user.RegisterUserDTO;
import com.nexus.alumcon.entity.User;
import com.nexus.alumcon.service.UserProfileDetailsService;
import com.nexus.alumcon.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
//    private final AuthenticationManager authenticationManager;
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//    private final PasswordEncoder passwordEncoder;
//
//    public AuthController(AuthenticationManager authenticationManager,
//                          UserRepository userRepository,
//                          JwtUtil jwtUtil,
//                          PasswordEncoder passwordEncoder) {
//        this.authenticationManager = authenticationManager;
//        this.userRepository = userRepository;
//        this.jwtUtil = jwtUtil;
//        this.passwordEncoder = passwordEncoder;
//    }


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserProfileDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static ResponseEntity<String> inputValidation(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty())
            return ResponseEntity.badRequest().body("Username is required");
        if (user.getPassword() == null || user.getPassword().isEmpty())
            return ResponseEntity.badRequest().body("Password is required");
        if (user.getFirstName() == null || user.getFirstName().isEmpty())
            return ResponseEntity.badRequest().body("First name is required");
        if (user.getLastName() == null || user.getLastName().isEmpty())
            return ResponseEntity.badRequest().body("Last name is required");
        if (user.getEmail() == null || user.getEmail().isEmpty())
            return ResponseEntity.badRequest().body("Email is required");
        if (user.getPhone() == null || user.getPhone().isEmpty())
            return ResponseEntity.badRequest().body("Phone is required");
        if (user.getAddress() == null || user.getAddress().isEmpty())
            return ResponseEntity.badRequest().body("Address is required");
        if (user.getCity() == null || user.getCity().isEmpty())
            return ResponseEntity.badRequest().body("City is required");
        if (user.getZip() == null || user.getZip().isEmpty())
            return ResponseEntity.badRequest().body("Zip is required");
        if (user.getEducation() == null || user.getEducation().isEmpty())
            return ResponseEntity.badRequest().body("Education is required");
        if (user.getSchoolName() == null || user.getSchoolName().isEmpty())
            return ResponseEntity.badRequest().body("School name is required");
        if (user.getCollegeName() == null || user.getCollegeName().isEmpty())
            return ResponseEntity.badRequest().body("College name is required");
        if (user.getSkills() == null || user.getSkills().isEmpty())
            return ResponseEntity.badRequest().body("Skills are required");
        if (user.getInterests() == null || user.getInterests().isEmpty())
            return ResponseEntity.badRequest().body("Interests are required");
        if (user.getBranch() == null || user.getBranch().isEmpty())
            return ResponseEntity.badRequest().body("Branch is required");
        if (user.getJobTitle() == null || user.getJobTitle().isEmpty())
            return ResponseEntity.badRequest().body("Job title is required");
        return null;
    }

    public static ResponseEntity<String> validatePassYear(String passOutYear) {
        try {
            int i = Integer.parseInt(passOutYear);
            if (i < 1950 || i > 2040)
                return ResponseEntity.badRequest().body("Pass out year is not a valid year");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Pass out year is not a valid integer");
        }
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserDTO userDTO) {

//        ResponseEntity<String> validation = inputValidation(user);
//        if (validation != null) return validation;

        // TODO: check if user already exists
        // alphanumerc user name
        if (!userDTO.getUsername().matches("^[a-zA-Z0-9]+$"))
            return ResponseEntity.badRequest().body("Username should only contain alphanumeric characters");
        if (userDetailsService.existsByUsername(userDTO.getUsername()))
            return ResponseEntity.badRequest().body("Username already exists");
        if (userDetailsService.existsByEmail(userDTO.getEmail()))
            return ResponseEntity.badRequest().body("Email already exists");
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        //check if passedYear is int parsable
        ResponseEntity<String> body = validatePassYear(userDTO.getPassOutYear());
        if (body != null) return body;

        userDTO.setChats(new ArrayList<>());
        User user = userDetailsService.saveUserDetails(userDTO);
        if (user == null)
            return ResponseEntity.badRequest().body("Failed to register user");
        return ResponseEntity.ok().body("Successfully registered! " + user.getUsername());
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequestDTO authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new Exception("Incorrect username or password", e);
        }
        return jwtUtil.generateToken(authRequest.getUsername());
    }
}