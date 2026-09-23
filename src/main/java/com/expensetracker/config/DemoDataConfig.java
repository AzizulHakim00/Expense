package com.expensetracker.config;

import com.expensetracker.model.Expense;
import com.expensetracker.model.Role;
import com.expensetracker.model.User;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Configuration
public class DemoDataConfig {
    public static final String ADMIN_EMAIL = "admin@expense.local";
    public static final String ADMIN_PASSWORD = "Admin123!";
    public static final String USER_EMAIL = "user@expense.local";
    public static final String USER_PASSWORD = "User123!";

    @Bean
    public ApplicationRunner demoAccounts(
            UserRepository userRepository,
            ExpenseRepository expenseRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            upsertDemoUser(userRepository, passwordEncoder,
                    "Demo Administrator", ADMIN_EMAIL, ADMIN_PASSWORD, Role.ADMIN);

            User demoUser = upsertDemoUser(userRepository, passwordEncoder,
                    "Demo User", USER_EMAIL, USER_PASSWORD, Role.USER);

            if (expenseRepository.countByUserId(demoUser.getId()) == 0) {
                saveExpense(expenseRepository, demoUser.getId(), "Groceries", "Food", "850.00", LocalDate.now(), "Weekly grocery shopping");
                saveExpense(expenseRepository, demoUser.getId(), "Bus fare", "Transport", "120.00", LocalDate.now().minusDays(1), "Daily commute");
                saveExpense(expenseRepository, demoUser.getId(), "Internet bill", "Bills", "1200.00", LocalDate.now().minusDays(4), "Monthly internet package");
                saveExpense(expenseRepository, demoUser.getId(), "Course book", "Education", "650.00", LocalDate.now().minusDays(10), "Reference book");
            }
        };
    }

    private User upsertDemoUser(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            String fullName,
            String email,
            String plainPassword,
            Role role
    ) {
        User user = repository.findByEmailIgnoreCase(email).orElseGet(User::new);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setRole(role);
        user.setEnabled(true);
        if (user.getCreatedAt() == null) user.setCreatedAt(Instant.now());
        if (user.getPassword() == null || !passwordEncoder.matches(plainPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(plainPassword));
        }
        return repository.save(user);
    }

    private void saveExpense(
            ExpenseRepository repository,
            String userId,
            String title,
            String category,
            String amount,
            LocalDate date,
            String note
    ) {
        Expense expense = new Expense();
        expense.setUserId(userId);
        expense.setTitle(title);
        expense.setCategory(category);
        expense.setAmount(new BigDecimal(amount));
        expense.setExpenseDate(date);
        expense.setNote(note);
        expense.setCreatedAt(Instant.now());
        expense.setUpdatedAt(Instant.now());
        repository.save(expense);
    }
}
