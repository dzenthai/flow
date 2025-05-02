package com.dzenthai.budget_command.service;

import com.dzenthai.budget_command.mapper.IncomeMapper;
import com.dzenthai.budget_command.model.dto.TransactionDTO;
import com.dzenthai.budget_command.model.entity.Income;
import com.dzenthai.budget_command.model.enums.IncomeCategory;
import com.dzenthai.budget_command.repository.IncomeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Slf4j
@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;

    private final IncomeMapper incomeMapper;

    public IncomeService(
            IncomeRepository incomeRepository,
            IncomeMapper incomeMapper
    ) {
        this.incomeRepository = incomeRepository;
        this.incomeMapper = incomeMapper;
    }

    @Transactional
    public Income save(TransactionDTO<IncomeCategory> transactionDTO, String userId) {
        log.info("IncomeService | Saving income, userId: {}", userId);
        var income = incomeMapper.convertDTOtoEntity(transactionDTO, userId);
        return incomeRepository.save(income);
    }

    public void delete(UUID id) {
        log.info("IncomeService | Deleting income, id: {}", id);
        var income = incomeRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Income %s not found".formatted(id)));
        incomeRepository.delete(income);
    }
}
