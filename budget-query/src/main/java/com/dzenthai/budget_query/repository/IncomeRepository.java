package com.dzenthai.budget_query.repository;

import com.dzenthai.budget_query.model.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IncomeRepository extends JpaRepository<Income, String> {

    @Query("SELECT i FROM Income i WHERE i.id = :id")
    Optional<Income> findIncomeById(String id);

    @Query("SELECT i FROM Income i WHERE i.userId = :userId")
    Optional<List<Income>> findIncomeByUserId(String userId);

    void deleteIncomeById(String id);
}
