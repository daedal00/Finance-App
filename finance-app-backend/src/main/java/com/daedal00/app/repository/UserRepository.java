package com.daedal00.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.daedal00.app.model.User;

public interface UserRepository extends MongoRepository<User, String>{
    User findByUsername(String username);
    User findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
