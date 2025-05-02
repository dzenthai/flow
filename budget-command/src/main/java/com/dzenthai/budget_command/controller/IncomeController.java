package com.dzenthai.budget_command.controller;

import com.dzenthai.budget_command.model.dto.TransactionDTO;
import com.dzenthai.budget_command.model.enums.IncomeCategory;
import com.dzenthai.budget_command.service.IncomeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(
            @RequestBody TransactionDTO<IncomeCategory> transactionDTO, @RequestHeader HttpHeaders headers) {
        return new ResponseEntity<>(incomeService.save(transactionDTO, headers.getFirst("X-User-Id")),
                HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            incomeService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
