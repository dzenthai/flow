package com.dzenthai.budget_query.controller;

import com.dzenthai.budget_query.service.ExpenseService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExpense(@PathVariable String id) {
        return new ResponseEntity<>(expenseService.getExpenseById(id),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllIExpenseByUserId(@RequestHeader HttpHeaders headers) {
        var userId = headers.getFirst("X-User-Id");
        return new ResponseEntity<>(expenseService.getAllExpensesByUserId(userId),
                HttpStatus.OK);
    }
}
