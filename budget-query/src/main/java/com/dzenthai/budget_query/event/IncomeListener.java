package com.dzenthai.budget_query.event;

import com.dzenthai.budget_query.factory.TransactionFactory;
import com.dzenthai.budget_query.model.dto.Payload;
import com.dzenthai.budget_query.model.entity.Income;
import com.dzenthai.budget_query.service.IncomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class IncomeListener {

    private final IncomeService incomeService;

    private final TransactionFactory<Income> transactionFactory;

    public IncomeListener(
            IncomeService incomeService,
            TransactionFactory<Income> transactionFactory
    ) {
        this.incomeService = incomeService;
        this.transactionFactory = transactionFactory;
    }

    @KafkaListener(
            topics = "budget-command-server.public.incomes",
            groupId = "budget-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleIncome(String message) {
        log.info("Received income: {}", message);
        try {
            Payload<Income> payload = transactionFactory.convertMessageToPayload(message);
            if (payload != null) {
                switch (payload.operation()) {
                    case CREATE -> {
                        log.info("Processing CREATE operation for income: {}", payload.data());
                        incomeService.saveIncome(payload.data());
                        log.info("Successfully processed CREATE operation for income: {}", payload.data().getId());
                    }
                    case DELETE -> {
                        log.info("Processing DELETE operation for income: {}", payload.data());
                        incomeService.deleteIncome(payload.data());
                        log.info("Successfully processed DELETE operation for income: {}", payload.data().getId());
                    }
                }
            } else {
                log.warn("Skipping invalid or non-create/delete operation");
            }
        } catch (Exception e) {
            log.error("Failed to process income message: {}", message, e);
            throw e;
        }
    }
}