package com.dzenthai.budget_command.repository;

import com.dzenthai.budget_command.model.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface IncomeRepository extends JpaRepository<Income, UUID> {
}
