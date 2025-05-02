package com.dzenthai.budget_query.service;

import com.dzenthai.budget_query.model.entity.Expense;
import com.dzenthai.budget_query.repository.ExpenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Slf4j
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(
            ExpenseRepository expenseRepository
    ) {
        this.expenseRepository = expenseRepository;
    }

    @Transactional(readOnly = true)
    public Expense getExpenseById(String id) {
        log.info("ExpenseService | Receiving expense by id: {}", id);
        return expenseRepository.findExpenseById(id).orElseThrow(() ->
                new IllegalArgumentException("Expense with id %s not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    public List<Expense> getAllExpensesByUserId(String userId) {
        log.info("ExpenseService | Receiving all expenses by userId: {}", userId);
        return expenseRepository.findExpenseByUserId(userId)
                .orElse(Collections.emptyList());
    }

    @Transactional
    public void saveExpense(Expense expense) {
        log.info("ExpenseService | Creating expense: {}", expense);
        if (expenseRepository.existsById(expense.getId())) {
            log.info("ExpenseService | Expense with ID {} already exists, skipping save", expense.getId());
            return;
        }
        expenseRepository.save(expense);
    }

    public void deleteExpense(Expense expense) {
        log.info("ExpenseService | Deleting expense: {}", expense);
        expenseRepository.delete(expense);
    }
}
