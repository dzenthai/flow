package com.dzenthai.budget_query.service;

import com.dzenthai.budget_query.model.entity.Income;
import com.dzenthai.budget_query.repository.IncomeRepository;
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
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeService(
            IncomeRepository incomeRepository
    ) {
        this.incomeRepository = incomeRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#id", value = "incomeById")
    public Income getIncomeById(String id) {
        log.info("IncomeService | Receiving income by id: {}", id);
        return incomeRepository.findIncomeById(id).orElseThrow(() ->
                new IllegalArgumentException("Income with id %s not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#userId", value = "incomeByUser")
    public List<Income> getAllIncomesByUserId(String userId) {
        log.info("IncomeService | Receiving all incomes by userId: {}", userId);
        return incomeRepository.findIncomeByUserId(userId)
                .orElse(Collections.emptyList());
    }

    @Transactional
    @CachePut(key = "#income.id", value = "incomeById")
    public Income saveIncome(Income income) {
        log.info("IncomeService | Creating income: {}", income);
        if (incomeRepository.existsById(income.getId())) {
            throw new IllegalArgumentException("IncomeService | Income with ID %s already exists, skipping save"
                    .formatted(income.getId()));
        }
        return incomeRepository.save(income);
    }

    @Transactional
    @CacheEvict(key = "#id", value = "incomeById")
    public void deleteIncome(String id) {
        log.info("IncomeService | Deleting income: {}", id);
        incomeRepository.deleteIncomeById(id);
    }
}
