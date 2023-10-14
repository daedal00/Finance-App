package com.daedal00.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.daedal00.app.api.dto.UserDTO;
import com.daedal00.app.model.PlaidData;
import com.daedal00.app.model.User;
import com.daedal00.app.repository.PlaidDataRepository;
import com.daedal00.app.repository.UserRepository;
import com.daedal00.app.service.UserService;


import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PlaidDataRepository plaidDataRepository;

    @GetMapping("/check-plaid-link")
    public ResponseEntity<?> checkPlaidLink(@RequestParam String userId) {
        PlaidData plaidData = plaidDataRepository.findByUserId(userId);
        boolean isPlaidLinked = plaidData != null && plaidData.getAccessToken() != null;
        return ResponseEntity.ok().body(new PlaidLinkStatusResponse(isPlaidLinked));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic")) {
            String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            final String[] values = credentials.split(":", 2);

            String username = values[0];
            String password = values[1];

            final User userFromDb = userService.findByUsername(username);

            if (userFromDb == null || !passwordEncoder.matches(password, userFromDb.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
            }

            if (userFromDb != null && passwordEncoder.matches(password, userFromDb.getPassword())) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(userFromDb.getId());
                userDTO.setUsername(userFromDb.getUsername());
                return ResponseEntity.ok().body(userDTO);
            }

            UserDTO userDTO = new UserDTO();
            userDTO.setId(userFromDb.getId());
            userDTO.setUsername(userFromDb.getUsername());

            return ResponseEntity.ok().body(userDTO);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header.");
    }


    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        User existingUserByUsername = userRepository.findByUsername(userDTO.getUsername());
        User existingUserByEmail = userRepository.findByEmail(userDTO.getEmail());
        if (existingUserByUsername != null || existingUserByEmail != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or Email already exists");
        }

        userDTO.setPassword(userDTO.getPassword());

        UserDTO savedUserDTO = userService.saveUser(userDTO);
        return ResponseEntity.ok(savedUserDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUser(id, userDTO);
        if (updatedUserDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUserDTO);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }


    public static class PlaidLinkStatusResponse {
        private final boolean isPlaidLinked;

        public PlaidLinkStatusResponse(boolean isPlaidLinked) {
            this.isPlaidLinked = isPlaidLinked;
        }

        public boolean getIsPlaidLinked() {
            return isPlaidLinked;
        }
    }
}
