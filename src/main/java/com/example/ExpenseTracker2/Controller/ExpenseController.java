package com.example.ExpenseTracker2.Controller;

import com.example.ExpenseTracker2.Entity.Expense;
import com.example.ExpenseTracker2.Service.ExpenseService;
import com.example.ExpenseTracker2.dto.ExpenseRequestDTO;
import com.example.ExpenseTracker2.dto.ExpenseResponseDTO;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @PostMapping
    private ExpenseResponseDTO createExpense(@Valid @RequestBody ExpenseRequestDTO requestDTO){
        return expenseService.createExpense(requestDTO);
    }

    @GetMapping
    public List<ExpenseResponseDTO> getAllExpense(){
        return expenseService.getAllExpenses();
    }

    @GetMapping("/{id}")
    public ExpenseResponseDTO getExpenseById(@PathVariable Long id){
        return expenseService.getExpenseById(id);
    }

    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id, @RequestBody Expense expense){
        return expenseService.updateExpense(id, expense);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id){
         expenseService.deleteExpense(id);
    }

    @GetMapping("/filter")
    public List<ExpenseResponseDTO> filterExpenses(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
            ){
        return expenseService.filterExpenses(userId, categoryId, startDate, endDate);
    }
}
