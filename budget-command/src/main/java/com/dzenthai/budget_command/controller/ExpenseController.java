package com.dzenthai.budget_command.controller;

import com.dzenthai.budget_command.model.dto.TransactionDTO;
import com.dzenthai.budget_command.model.enums.ExpenseCategory;
import com.dzenthai.budget_command.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<?> add(
            @Valid @RequestBody TransactionDTO<ExpenseCategory> transactionDTO, @RequestHeader HttpHeaders headers) {
        return new ResponseEntity<>(expenseService.save(transactionDTO, headers.getFirst("X-User-Id")),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            expenseService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
