package com.balash.banking.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

    private long Id;
    private String username;
    private String email;

}