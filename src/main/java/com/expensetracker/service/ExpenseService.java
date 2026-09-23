package com.expensetracker.service;

import com.expensetracker.dto.ExpenseRequest;
import com.expensetracker.model.Expense;
import com.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class ExpenseService {
<<<<<<< HEAD

=======
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> allForUser(String userId) {
        return expenseRepository.findByUserIdOrderByExpenseDateDescCreatedAtDesc(userId);
    }

    public List<Expense> weeklyForUser(String userId, LocalDate referenceDate) {
        LocalDate start = referenceDate.with(DayOfWeek.MONDAY);
        LocalDate end = start.plusDays(6);
<<<<<<< HEAD
        return expenseRepository.findByUserIdAndExpenseDateBetweenOrderByExpenseDateDescCreatedAtDesc(
                userId, start, end
        );
=======
        return expenseRepository.findByUserIdAndExpenseDateBetweenOrderByExpenseDateDescCreatedAtDesc(userId, start, end);
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8
    }

    public List<Expense> monthlyForUser(String userId, LocalDate referenceDate) {
        LocalDate start = referenceDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = referenceDate.with(TemporalAdjusters.lastDayOfMonth());
<<<<<<< HEAD
        return expenseRepository.findByUserIdAndExpenseDateBetweenOrderByExpenseDateDescCreatedAtDesc(
                userId, start, end
        );
    }

    public List<Expense> forPeriod(String userId, String period, LocalDate referenceDate) {
        if ("week".equalsIgnoreCase(period)) {
            return weeklyForUser(userId, referenceDate);
        }
        if ("month".equalsIgnoreCase(period)) {
            return monthlyForUser(userId, referenceDate);
        }
=======
        return expenseRepository.findByUserIdAndExpenseDateBetweenOrderByExpenseDateDescCreatedAtDesc(userId, start, end);
    }

    public List<Expense> forPeriod(String userId, String period, LocalDate referenceDate) {
        if ("week".equalsIgnoreCase(period)) return weeklyForUser(userId, referenceDate);
        if ("month".equalsIgnoreCase(period)) return monthlyForUser(userId, referenceDate);
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8
        return allForUser(userId);
    }

    public Expense getOwned(String userId, String expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        if (!userId.equals(expense.getUserId())) {
            throw new IllegalArgumentException("Expense not found");
        }
        return expense;
    }

    public Expense create(String userId, ExpenseRequest request) {
        Expense expense = new Expense();
        expense.setUserId(userId);
        expense.setCreatedAt(Instant.now());
        applyRequest(expense, request);
        return expenseRepository.save(expense);
    }

    public Expense update(String userId, String expenseId, ExpenseRequest request) {
        Expense expense = getOwned(userId, expenseId);
        applyRequest(expense, request);
        return expenseRepository.save(expense);
    }

    public void deleteOwned(String userId, String expenseId) {
<<<<<<< HEAD
        Expense expense = getOwned(userId, expenseId);
        expenseRepository.delete(expense);
=======
        expenseRepository.delete(getOwned(userId, expenseId));
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8
    }

    public List<Expense> allForAdmin() {
        return expenseRepository.findAllByOrderByExpenseDateDescCreatedAtDesc();
    }

    public void deleteAsAdmin(String expenseId) {
        if (!expenseRepository.existsById(expenseId)) {
            throw new IllegalArgumentException("Expense not found");
        }
        expenseRepository.deleteById(expenseId);
    }

<<<<<<< HEAD
    public long countAll() {
        return expenseRepository.count();
    }

=======
>>>>>>> ddc854d9aa2bb888f13a42a657cf261d844435f8
    public BigDecimal total(List<Expense> expenses) {
        return expenses.stream()
                .map(Expense::getAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void applyRequest(Expense expense, ExpenseRequest request) {
        expense.setTitle(request.getTitle().trim());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory().trim());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setNote(request.getNote() == null ? "" : request.getNote().trim());
        expense.setUpdatedAt(Instant.now());
    }
}
