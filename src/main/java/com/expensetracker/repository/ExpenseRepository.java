package com.expensetracker.repository;

import com.expensetracker.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByUserIdOrderByExpenseDateDescCreatedAtDesc(String userId);
    List<Expense> findByUserIdAndExpenseDateBetweenOrderByExpenseDateDescCreatedAtDesc(
            String userId,
            LocalDate start,
            LocalDate end
    );
    List<Expense> findAllByOrderByExpenseDateDescCreatedAtDesc();
    long countByUserId(String userId);
}
