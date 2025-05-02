package com.dzenthai.budget_query.controller;

import com.dzenthai.budget_query.service.IncomeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIncome(@PathVariable String id) {
        return new ResponseEntity<>(incomeService.getIncomeById(id),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllIncomesByUserId(@RequestHeader HttpHeaders headers) {
        var userId = headers.getFirst("X-User-Id");
        return new ResponseEntity<>(incomeService.getAllIncomesByUserId(userId),
                HttpStatus.OK);
    }
}
