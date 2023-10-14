package com.daedal00.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.daedal00.app.model.Transaction;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByUserId(String userId);

    void deleteByUserId(String userId);
}
