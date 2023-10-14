package com.daedal00.app.repository;

import com.daedal00.app.model.PlaidData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlaidDataRepository extends MongoRepository<PlaidData, String> {
    PlaidData findByUserId(String userId);
    void deleteByUserId(String userId);
}
