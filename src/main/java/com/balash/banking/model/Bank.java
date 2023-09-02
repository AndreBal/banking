package com.balash.banking.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class Bank {

    @NotNull
    private long Id;
    private String name;
}
