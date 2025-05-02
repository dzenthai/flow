package com.dzenthai.budget_command.model.entity;

import com.dzenthai.budget_command.model.enums.IncomeCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "incomes")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private Transaction<IncomeCategory> transaction;
}
