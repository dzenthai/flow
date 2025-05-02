package com.dzenthai.budget_command.model.entity;

import com.dzenthai.budget_command.model.enums.ExpenseCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private Transaction<ExpenseCategory> transaction;
}
