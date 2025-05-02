package com.dzenthai.budget_query.event;

import com.dzenthai.budget_query.factory.TransactionFactory;
import com.dzenthai.budget_query.model.dto.Payload;
import com.dzenthai.budget_query.model.entity.Expense;
import com.dzenthai.budget_query.service.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ExpenseListener {

    private final ExpenseService expenseService;

    private final TransactionFactory<Expense> transactionFactory;

    public ExpenseListener(
            ExpenseService expenseService,
            TransactionFactory<Expense> transactionFactory
    ) {
        this.expenseService = expenseService;
        this.transactionFactory = transactionFactory;
    }

    @KafkaListener(
            topics = "budget-command-server.public.expenses",
            groupId = "budget-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleExpense(String message) {
        log.info("Received expense: {}", message);
        try {
            Payload<Expense> payload = transactionFactory.convertMessageToPayload(message);
            if (payload != null) {
                switch (payload.operation()) {
                    case CREATE -> {
                        log.info("Processing CREATE operation for expense: {}", payload.data());
                        expenseService.saveExpense(payload.data());
                        log.info("Successfully processed CREATE operation for expense: {}", payload.data().getId());
                    }
                    case DELETE -> {
                        log.info("Processing DELETE operation for expense: {}", payload.data());
                        expenseService.deleteExpense(payload.data());
                        log.info("Successfully processed DELETE operation for expense: {}", payload.data().getId());
                    }
                }
            } else {
                log.warn("Skipping invalid or non-create/delete operation");
            }
        } catch (Exception e) {
            log.error("Failed to process expense message: {}", message, e);
            throw e;
        }
    }
}
