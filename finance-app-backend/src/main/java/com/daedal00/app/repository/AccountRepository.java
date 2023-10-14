package com.daedal00.app.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.daedal00.app.model.Account;

public interface AccountRepository extends MongoRepository<Account, String>{
    List<Account> findByUserId(String userId);
}
