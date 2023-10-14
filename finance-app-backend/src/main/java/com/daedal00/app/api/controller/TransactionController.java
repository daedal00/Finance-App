package com.daedal00.app.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daedal00.app.api.dto.TransactionDTO;
import com.daedal00.app.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable String id) {
        TransactionDTO transactionDTO = transactionService.getTransactionById(id);
        if (transactionDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactionDTO);
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        TransactionDTO savedTransactionDTO = transactionService.saveTransaction(transactionDTO);
        return ResponseEntity.ok(savedTransactionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/totals")
    public ResponseEntity<Map<String, Map<String, TransactionService.CategoryTransactions>>> getTransactionTotalsByCategory(@RequestParam String userId) {
        Map<String, Map<String, TransactionService.CategoryTransactions>> totals = transactionService.getTransactionTotalsByCategory(userId);
        if (totals == null || totals.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(totals);
    }

}
