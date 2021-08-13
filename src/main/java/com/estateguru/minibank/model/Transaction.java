package com.estateguru.minibank.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @ManyToOne
    private BankAccount account;
    private BigDecimal transactionAmount;
    @Enumerated(EnumType.ORDINAL)
    private TransactionType transactionType;
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
}
