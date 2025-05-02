package com.dzenthai.budget_command.mapper;

import com.dzenthai.budget_command.model.dto.TransactionDTO;
import com.dzenthai.budget_command.model.entity.Transaction;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;


@Component
public class TransactionMapper {

    public <T extends Enum<T>> Transaction<T> convertDTOToEntity(TransactionDTO<T> transactionDTO, String userId) {
        Transaction<T> transaction = new Transaction<>();
        transaction.setAmount(transactionDTO.amount());
        transaction.setCategory(transactionDTO.category());
        if (transactionDTO.createdAt() == null) {
            transaction.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        } else {
            transaction.setCreatedAt(transactionDTO.createdAt());
        }
        transaction.setNote(transactionDTO.note());
        transaction.setUserId(userId);
        return transaction;
    }
}
