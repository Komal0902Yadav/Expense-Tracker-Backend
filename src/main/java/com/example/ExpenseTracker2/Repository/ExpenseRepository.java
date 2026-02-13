package com.example.ExpenseTracker2.Repository;

import com.example.ExpenseTracker2.Entity.Expense;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);
    List<Expense> findByCategoryId(Long categoryId);
    List<Expense> findByUserIdAndCategoryId(Long userId, Long categoryId);
    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
