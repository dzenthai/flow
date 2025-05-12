package com.dzenthai.budget_query.repository;

import com.dzenthai.budget_query.model.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {

    @Query("SELECT e FROM Expense e WHERE e.id =: id")
    Optional<Expense> findExpenseById(String id);

    @Query("SELECT e FROM Expense e WHERE e.userId =: userId")
    Optional<List<Expense>> findExpenseByUserId(String userId);

    void deleteExpenseById(String id);
}
