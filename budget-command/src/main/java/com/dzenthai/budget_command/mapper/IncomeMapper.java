package com.dzenthai.budget_command.mapper;

import com.dzenthai.budget_command.model.dto.TransactionDTO;
import com.dzenthai.budget_command.model.entity.Income;
import com.dzenthai.budget_command.model.enums.IncomeCategory;
import org.springframework.stereotype.Component;


@Component
public class IncomeMapper {

    private final TransactionMapper transactionMapper;

    public IncomeMapper(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }

    public Income convertDTOtoEntity(TransactionDTO<IncomeCategory> transactionDTO, String userId) {
        return Income.builder()
                .transaction(transactionMapper.convertDTOToEntity(transactionDTO, userId))
                .build();
    }
}
