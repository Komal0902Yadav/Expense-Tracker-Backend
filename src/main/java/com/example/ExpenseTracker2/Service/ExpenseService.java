package com.example.ExpenseTracker2.Service;

import com.example.ExpenseTracker2.Entity.Category;
import com.example.ExpenseTracker2.Entity.Expense;
import com.example.ExpenseTracker2.Entity.User;
import com.example.ExpenseTracker2.Exception.CategoryNotFoundException;
import com.example.ExpenseTracker2.Exception.ExpenseNotFoundException;
import com.example.ExpenseTracker2.Exception.UserNotFoundException;
import com.example.ExpenseTracker2.Repository.CategoryRepository;
import com.example.ExpenseTracker2.Repository.ExpenseRepository;
import com.example.ExpenseTracker2.Repository.UserRepository;
import com.example.ExpenseTracker2.dto.ExpenseRequestDTO;
import com.example.ExpenseTracker2.dto.ExpenseResponseDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository){
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public ExpenseResponseDTO createExpense(ExpenseRequestDTO dto){

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id " +
                        dto.getUserId()));

        Category category = categoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new CategoryNotFoundException("Category not found with id"
                                                             + dto.getCategoryId()));

        Expense expense = new Expense();
        expense.setTitle(dto.getTitle());
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setUser(user);
        expense.setCategory(category);

        Expense savedExpense = expenseRepository.save(expense);

        return new ExpenseResponseDTO(savedExpense.getId(), savedExpense.getTitle(),
                savedExpense.getAmount(), savedExpense.getDate(), user.getName(), category.getName());
    }

    private ExpenseResponseDTO mapToDTO(Expense expense){
        ExpenseResponseDTO dto = new ExpenseResponseDTO();
        dto.setId(expense.getId());
        dto.setTitle(expense.getTitle());
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setUserName(expense.getUser().getName());
        dto.setCategoryName(expense.getCategory().getName());
        return dto;
    }

    public List<ExpenseResponseDTO> getAllExpenses(){
        return expenseRepository.findAll()
                .stream().map(this::mapToDTO).toList();
    }

    public ExpenseResponseDTO getExpenseById(Long id){
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with id: " + id));

        return mapToDTO(expense);
    }

    public List<ExpenseResponseDTO> getExpensesByUserId(Long userId){
        return expenseRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public Expense updateExpense(Long id, Expense updatedExpense){
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with id: " + id));

        existingExpense.setTitle(updatedExpense.getTitle());
        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setCategory(updatedExpense.getCategory());
        existingExpense.setDate(updatedExpense.getDate());

        return expenseRepository.save(existingExpense);
    }

    public void deleteExpense(Long id){
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with id " +
                        id));
        expenseRepository.delete(expense);
    }

    public List<ExpenseResponseDTO> filterExpenses(
            Long userId, Long categoryId, LocalDate startDate, LocalDate endDate
    ){
        List<Expense> expenses;

        if (userId != null && categoryId != null){
                expenses = expenseRepository.findByUserIdAndCategoryId(userId, categoryId);
        } else if (userId != null) {
            expenses = expenseRepository.findByUserId(userId);
        } else if (categoryId != null) {
            expenses = expenseRepository.findByCategoryId(categoryId);
        } else if (startDate != null && endDate != null) {
            expenses = expenseRepository.findByDateBetween(startDate, endDate);
        }
        else {
            expenses = expenseRepository.findAll();
        }

        return expenses.stream()
                .map(this::mapToDTO)
                .toList();
    }

}
