package com.daedal00.app.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Collector;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daedal00.app.api.dto.TransactionDTO;
import com.daedal00.app.model.Transaction;
import com.daedal00.app.repository.TransactionRepository;

import lombok.Data;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(String id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            return null;
        }
        return convertToDTO(transaction);
    }

    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = convertToEntity(transactionDTO);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDTO(savedTransaction);
    }

    public void deleteTransaction(String id) {
        transactionRepository.deleteById(id);
    }

    public Map<String, Map<String, CategoryTransactions>> getTransactionTotalsByCategory(String userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory,
                        Collectors.groupingBy(transaction -> {
                            LocalDate date = convertToLocalDate(transaction.getDate());
                            return date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                        }, Collector.of(CategoryTransactions::new,
                                (categoryTransactions, transaction) -> {
                                    categoryTransactions.setMonth(convertToLocalDate(transaction.getDate()).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                                    categoryTransactions.getTransactions().add(transaction);
                                    categoryTransactions.setTotalAmount(categoryTransactions.getTotalAmount() + transaction.getAmount());
                                },
                                (categoryTransactions1, categoryTransactions2) -> {
                                    categoryTransactions1.getTransactions().addAll(categoryTransactions2.getTransactions());
                                    categoryTransactions1.setTotalAmount(categoryTransactions1.getTotalAmount() + categoryTransactions2.getTotalAmount());
                                    return categoryTransactions1;
                                }))));
    }

    private LocalDate convertToLocalDate(java.util.Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Transaction convertToEntity(TransactionDTO transactionDTO) {
        return modelMapper.map(transactionDTO, Transaction.class);
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    @Data
    public static class CategoryTransactions {
        private String month;
        private Double totalAmount = 0.0;
        private List<Transaction> transactions = new ArrayList<>();
    }
}