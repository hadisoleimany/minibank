package com.estateguru.minibank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String accountNumber;
    private BigDecimal currentBalance;
    @ManyToOne
    private Customer customer;
    @OneToOne(fetch = FetchType.EAGER)
    private Currency currency;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY)
    private  List<Transaction> transactions;
}
