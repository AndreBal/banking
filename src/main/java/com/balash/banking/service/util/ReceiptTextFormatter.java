package com.balash.banking.service.util;

import com.balash.banking.model.Transaction;

import java.text.SimpleDateFormat;

public class ReceiptTextFormatter {

    private Utils utils = Utils.getInstance();
    private final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");

    private static final ReceiptTextFormatter INSTANCE = new ReceiptTextFormatter();

    private ReceiptTextFormatter(){}

    public static ReceiptTextFormatter getInstance(){
        return INSTANCE;
    }

    public String TransactionReceipt(Transaction transaction){
        String dateAsString = DATE_FORMATTER.format(transaction.getTransactionDate());
        String timeAsString = TIME_FORMATTER.format(transaction.getTransactionDate());
        String moneyString = utils.convertToDollarsAndCents(transaction.getAmount());
        StringBuilder receipt = new StringBuilder();
        receipt.append("----------------------------------------------\n");
        receipt.append("|              Банковский Чек                |\n");
        receipt.append(String.format("| Чек: %37s |\n", transaction.getId()));
        receipt.append(String.format("| %s %31s |\n",dateAsString,timeAsString));
        receipt.append(String.format("| Тип транзакции: %26s |\n", transaction.getTransactionType().getRussianTranslation()));
        switch (transaction.getTransactionType()) {
            case TRANSFER:
                receipt.append(String.format("| Банк отправителя: %24s |\n", transaction.getDonorAccount().getBank().getName()));
                receipt.append(String.format("| Банк получателя: %25s |\n", transaction.getRecipientAccount().getBank().getName()));
                receipt.append(String.format("| Счёт отправителя: %24s |\n", transaction.getDonorAccount().getId()));
                receipt.append(String.format("| Счёт получателя: %25s |\n", transaction.getRecipientAccount().getId()));
                receipt.append(String.format("| Сумма: %35s |\n", moneyString+" "+transaction.getDonorAccount().getCurrency()));
                break;
            case DEPOSIT:
                receipt.append(String.format("| Банк зачисления: %25s |\n", transaction.getRecipientAccount().getBank().getName()));
                receipt.append(String.format("| Счёт зачисления: %25s |\n", transaction.getRecipientAccount().getId()));
                receipt.append(String.format("| Сумма: %35s |\n", moneyString+" "+transaction.getRecipientAccount().getCurrency()));
                break;
            case WITHDRAWAL:
                receipt.append(String.format("| Банк снятия: %29s |\n", transaction.getDonorAccount().getBank().getName()));
                receipt.append(String.format("| Счёт снятия: %29s |\n", transaction.getDonorAccount().getId()));
                receipt.append(String.format("| Сумма: %35s |\n", moneyString+" "+transaction.getDonorAccount().getCurrency()));
                break;
        }

        receipt.append("----------------------------------------------\n");
        return receipt.toString();
    }

}
