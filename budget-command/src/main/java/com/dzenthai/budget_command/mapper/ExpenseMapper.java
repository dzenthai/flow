package com.dzenthai.budget_command.mapper;

import com.dzenthai.budget_command.model.dto.TransactionDTO;
import com.dzenthai.budget_command.model.entity.Expense;
import com.dzenthai.budget_command.model.enums.ExpenseCategory;
import org.springframework.stereotype.Component;


@Component
public class ExpenseMapper {

    private final TransactionMapper transactionMapper;

    public ExpenseMapper(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }

    public Expense convertDTOtoEntity(TransactionDTO<ExpenseCategory> transactionDTO, String userId) {
        return Expense.builder()
                .transaction(transactionMapper.convertDTOToEntity(transactionDTO, userId))
                .build();
    }
}
