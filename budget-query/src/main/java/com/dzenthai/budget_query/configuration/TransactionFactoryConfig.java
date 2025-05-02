package com.dzenthai.budget_query.configuration;

import com.dzenthai.budget_query.factory.TransactionFactory;
import com.dzenthai.budget_query.model.entity.Expense;
import com.dzenthai.budget_query.model.entity.Income;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TransactionFactoryConfig {

    @Bean
    public TransactionFactory<Income> incomeTransactionFactory() {
        return new TransactionFactory<>(Income.class);
    }

    @Bean
    public TransactionFactory<Expense> expenseTransactionFactory() {
        return new TransactionFactory<>(Expense.class);
    }
}
