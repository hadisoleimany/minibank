package com.estateguru.minibank.model;

import com.estateguru.minibank.dto.BankAccountDto;
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
public class RequestAction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.ORDINAL)
    private TransactionType transactionType;
    @Enumerated(EnumType.ORDINAL)
    private RequestStatus status;
    @OneToOne
    private BankAccount sourceAccount;
    @OneToOne
    private BankAccount destinationAccount;
    private BigDecimal amount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    private String description;
}
