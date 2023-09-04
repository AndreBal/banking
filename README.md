# Banking Application README

## General Description

This project is a Java Gradle-based servlet banking application that allows users to perform various banking operations such as transferring funds between accounts, depositing money, and withdrawing money. The application utilizes a PostgreSQL database to store user account information and transaction history.

## Instructions for Starting the Project

To get started with the project, follow these steps:

1. **Initialize the PostgreSQL Database:**

    - Ensure you have PostgreSQL installed on your system.
    - Initialize the database schema and sample data using the provided `init.sql` file. Run the following command:
      ```
      psql -d banking -a -f init.sql
      ```

2. **Build the Project:**

    - Make sure you have Gradle installed on your system.
    - Navigate to the project directory in your terminal.
    - Run the following command to build the project:
      ```
      gradle build
      ```

3. **Run the Application:**

    - After successfully building the project, start the application by running the following command:
      ```
      gradle run
      ```

4. **Access the Application:**

    - Once the application is running, you can access it in your web browser at `http://localhost:8080`.

## CRUD Operations

### Transfer

- **Endpoint:** `/transfer/{donor}/{recipient}/{amount}`
- **Method:** PUT
- **Input:**
    - http request, including path variables `donor`, `recipient`, and `amount`.

**Example Input:**

```http
http://localhost:8080/banking/transfer/1000000000/1000000001/0.03
```
- **Output:**
    - Text containing operation receipt.

**Example Output:**

```text
----------------------------------------------
|              Банковский Чек                |
| Чек:                                   238 |
| 2023-09-04                        04:52:01 |
| Тип транзакции:                    Перевод |
| Банк отправителя:               Dummy-Bank |
| Банк получателя:                     Banko |
| Счёт отправителя:               1000000000 |
| Счёт получателя:                1000000001 |
| Сумма:                            0.03 BYN |
----------------------------------------------
```
### Deposit

- **Endpoint:** `/deposit/{recipient}/{amount}`
- **Method:** PUT
- **Input:**
   - http request, including path variables `recipient`, and `amount`.

**Example Input:**

```http
http://localhost:8080/banking/deposit/1000000001/5.07
```
- **Output:**
   - Text containing operation receipt.

**Example Output:**

```text
----------------------------------------------
|              Банковский Чек                |
| Чек:                                   237 |
| 2023-09-04                        04:51:59 |
| Тип транзакции:                 Пополнение |
| Банк зачисления:                     Banko |
| Счёт зачисления:                1000000001 |
| Сумма:                            5.07 BYN |
----------------------------------------------
```
### Withdraw

- **Endpoint:** `/withdraw/{donor}/{amount}`
- **Method:** PUT
- **Input:**
   - http request, including path variables `donor`, and `amount`.

**Example Input:**

```http
http://localhost:8080/banking/withdraw/1000000001/5.07
```
- **Output:**
   - Text containing operation receipt.

**Example Output:**

```text
----------------------------------------------
|              Банковский Чек                |
| Чек:                                   231 |
| 2023-09-04                        04:25:33 |
| Тип транзакции:                     Снятие |
| Банк снятия:                         Banko |
| Счёт снятия:                    1000000001 |
| Сумма:                            5.07 BYN |
----------------------------------------------
```