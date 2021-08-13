package com.estateguru.minibank.dto;
import com.estateguru.minibank.model.RequestStatus;
import com.estateguru.minibank.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestActionDto {
    private TransactionType transactionType;
    private RequestStatus status;
    private BankAccountDto sourceAccount;
    private BankAccountDto destinationAccount;
    private BigDecimal amount;
    private Date createDate;
    private Date updateDate;
    private String description;
}
