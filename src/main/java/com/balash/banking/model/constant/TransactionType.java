package com.balash.banking.model.constant;

public enum TransactionType {


    DEPOSIT("Пополнение"),
    WITHDRAWAL("Снятие"),
    TRANSFER("Перевод");

    private final String russianTranslation;

    TransactionType(String russianTranslation) {
        this.russianTranslation = russianTranslation;
    }

    public String getRussianTranslation() {
        return russianTranslation;
    }
}
