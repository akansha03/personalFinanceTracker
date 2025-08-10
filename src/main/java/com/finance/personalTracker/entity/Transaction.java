package com.finance.personalTracker.entity;

import com.finance.personalTracker.enums.Account;
import com.finance.personalTracker.enums.CategoryType;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {

    @Id
    public UUID Id;

    private LocalDateTime date;

    private Double amount;

    private CategoryType type;

    @ManyToOne
    private Category categoryName;

    @Enumerated(EnumType.STRING)
    private Account account;

    private String note;

    private String image_url;

    private String description;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
