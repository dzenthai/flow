package com.dzenthai.budget_query.service;

import com.dzenthai.budget_query.model.entity.Expense;
import com.dzenthai.budget_query.repository.ExpenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(key = "#id", value = "expenseById")
    public Expense getExpenseById(String id) {
        log.info("ExpenseService | Receiving expense by id: {}", id);
        return expenseRepository.findExpenseById(id).orElseThrow(() ->
                new IllegalArgumentException("Expense with id %s not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#userId", value = "expensesByUser")
    public List<Expense> getAllExpensesByUserId(String userId) {
        log.info("ExpenseService | Receiving all expenses by userId: {}", userId);
        return expenseRepository.findExpenseByUserId(userId)
                .orElse(Collections.emptyList());
    }

    @Transactional
    @CachePut(key = "#expense.id", value = "expenseById")
    public Expense saveExpense(Expense expense) {
        log.info("ExpenseService | Creating expense: {}", expense);
        if (expenseRepository.existsById(expense.getId())) {
            throw new IllegalArgumentException("ExpenseService | Expense with ID %s already exists, skipping save"
                    .formatted(expense.getId()));
        }
        return expenseRepository.save(expense);
    }

    @Transactional
    @CacheEvict(key = "#id", value = "expenseById")
    public void deleteExpense(String id) {
        log.info("ExpenseService | Deleting expense: {}", id);
        expenseRepository.deleteExpenseById(id);
    }
}
