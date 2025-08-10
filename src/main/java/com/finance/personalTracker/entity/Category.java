package com.finance.personalTracker.entity;

import com.finance.personalTracker.enums.CategoryType;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String Id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryType type;
}
