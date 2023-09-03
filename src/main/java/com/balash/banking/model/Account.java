package com.balash.banking.model;

import com.balash.banking.model.constant.Currency;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ToString
public class Account {

    @NotNull
    private Long Id;
    private Bank bank;
    private Long amount;
    private User user;
    private Currency currency;
    private Date startDate;
    private Date endDate;
    private Boolean isInterestActive;
}
