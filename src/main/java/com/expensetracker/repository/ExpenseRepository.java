package com.expensetracker.repository;

import com.expensetracker.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByUserIdOrderByExpenseDateDescCreatedAtDesc(String userId);
    List<Expense> findByUserIdAndExpenseDateBetweenOrderByExpenseDateDescCreatedAtDesc(
<<<<<<< HEAD
            String userId,
            LocalDate start,
            LocalDate end
=======
            String userId, LocalDate start, LocalDate end
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8
    );
    List<Expense> findAllByOrderByExpenseDateDescCreatedAtDesc();
    long countByUserId(String userId);
}
