package com.dzenthai.budget_query.service;

import com.dzenthai.budget_query.model.entity.Income;
import com.dzenthai.budget_query.repository.IncomeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Slf4j
@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeService(
            IncomeRepository incomeRepository
    ) {
        this.incomeRepository = incomeRepository;
    }

    @Transactional(readOnly = true)
    public Income getIncomeById(String id) {
        log.info("IncomeService | Receiving income by id: {}", id);
        return incomeRepository.findIncomeById(id).orElseThrow(() ->
                new IllegalArgumentException("Income with id %s not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    public List<Income> getAllIncomesByUserId(String userId) {
        log.info("IncomeService | Receiving all incomes by userId: {}", userId);
        return incomeRepository.findIncomeByUserId(userId)
                .orElse(Collections.emptyList());
    }

    @Transactional
    public void saveIncome(Income income) {
        log.info("IncomeService | Creating income: {}", income);
        if (incomeRepository.existsById(income.getId())) {
            log.info("IncomeService | Income with ID {} already exists, skipping save", income.getId());
            return;
        }
        incomeRepository.save(income);
    }

    public void deleteIncome(Income income) {
        log.info("IncomeService | Deleting income: {}", income);
        incomeRepository.delete(income);
    }
}
