package com.balash.banking.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class Bank {

    @NotNull
    private Long Id;
    private String name;
}
