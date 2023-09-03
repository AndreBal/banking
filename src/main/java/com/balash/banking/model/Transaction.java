package com.balash.banking.model;

import com.balash.banking.model.constant.TransactionType;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Transaction {

    private Long Id;
    private Long amount;
    private TransactionType transactionType;
    private Date transactionDate;
    private Account donorAccount;
    private Account recipientAccount;
}

