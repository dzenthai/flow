package com.dzenthai.budget_command.service;

import com.dzenthai.budget_command.mapper.ExpenseMapper;
import com.dzenthai.budget_command.model.dto.TransactionDTO;
import com.dzenthai.budget_command.model.entity.Expense;
import com.dzenthai.budget_command.model.enums.ExpenseCategory;
import com.dzenthai.budget_command.repository.ExpenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Slf4j
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final ExpenseMapper expenseMapper;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            ExpenseMapper expenseMapper
    ) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
    }

    @Transactional
    public Expense save(TransactionDTO<ExpenseCategory> transactionDTO, String userId) {
        log.info("ExpenseService | Saving expense, userId: {}", userId);
        var expense = expenseMapper.convertDTOtoEntity(transactionDTO, userId);
        return expenseRepository.save(expense);
    }

    public void delete(UUID id) {
        log.info("ExpenseService | Deleting expense, id: {}", id);
        var expense = expenseRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Expense %s not found".formatted(id)));
        expenseRepository.delete(expense);
    }
}
