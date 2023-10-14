package com.daedal00.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.daedal00.app.model.Budget;
import java.util.List;

public interface BudgetRepository extends MongoRepository<Budget, String> {

    Budget findByUserIdAndCategory(String userId, String category);
    
    List<Budget> findByUserId(String userId);
}
