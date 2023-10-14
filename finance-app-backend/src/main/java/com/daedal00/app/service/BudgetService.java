package com.daedal00.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daedal00.app.model.Budget;
import com.daedal00.app.repository.BudgetRepository;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public Budget setBudgetForCategory(String userId, String category, Double amount) {
        Budget budget = budgetRepository.findByUserIdAndCategory(userId, category);
        if (budget == null) {
            budget = new Budget();
            budget.setUserId(userId);
            budget.setCategory(category);
        }
        budget.setAmount(amount);
        return budgetRepository.save(budget);
    }

    public List<Budget> getBudgetsForUser(String userId) {
        return budgetRepository.findByUserId(userId);
    }
}
