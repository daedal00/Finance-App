package com.daedal00.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.daedal00.app.api.dto.UserDTO;
import com.daedal00.app.model.PlaidData;
import com.daedal00.app.model.User;
import com.daedal00.app.repository.PlaidDataRepository;
import com.daedal00.app.repository.TransactionRepository;
import com.daedal00.app.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaidDataRepository plaidDataRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        return convertToDTO(user);
    }

    public UserDTO saveUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    
    public void deleteUser(String userId) {
        plaidDataRepository.deleteByUserId(userId);
    
        transactionRepository.deleteByUserId(userId);
    
        userRepository.deleteById(userId);
    }
    

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User linkUserWithPlaidData(String userId, String accessToken) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    
        PlaidData plaidData = new PlaidData();
        plaidData.setUserId(user.getId());
        plaidData.setAccessToken(accessToken);
        plaidDataRepository.save(plaidData);
    
        return user;
    }
    
    public UserDTO getUserWithPlaidData(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        PlaidData plaidData = plaidDataRepository.findByUserId(userId);
        return convertToUserDTO(user, plaidData);
    }

    public UserDTO updateUser(String id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    
        if (userDTO.getUsername() != null) {
            existingUser.setUsername(userDTO.getUsername());
        }
    
        if (userDTO.getEmail() != null) {
            existingUser.setEmail(userDTO.getEmail());
        }
    
        if (userDTO.getFirstName() != null) {
            existingUser.setFirstName(userDTO.getFirstName());
        }
    
        if (userDTO.getLastName() != null) {
            existingUser.setLastName(userDTO.getLastName());
        }
    
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(userDTO.getPassword());
        }
    
        User updatedUser = userRepository.save(existingUser);
    
        return convertToDTO(updatedUser);
    }
    
    private UserDTO convertToUserDTO(User user, PlaidData plaidData) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        if (plaidData != null) {
            userDTO.setAccessToken(plaidData.getAccessToken());
            userDTO.setItemId(plaidData.getItemId());
        }
        return userDTO;
    }
}
