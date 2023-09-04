CREATE DATABASE bankingdb;
\c bankingdb;

CREATE TABLE IF NOT EXISTS "user"
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    username text COLLATE pg_catalog."default",
    email text COLLATE pg_catalog."default",
    CONSTRAINT user_pkey PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS "transaction"
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    amount integer,
    transaction_type text COLLATE pg_catalog."default",
    date timestamp without time zone,
    donor_acc_id integer,
    recipient_acc_id integer,
    CONSTRAINT transaction_pkey PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS bank
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name text COLLATE pg_catalog."default",
    CONSTRAINT bank_pkey PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS account
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1000000000 MINVALUE 1000000000 MAXVALUE 9999999999 CACHE 1 ),
    bank_id integer,
    amount integer,
    user_id integer,
    currency text COLLATE pg_catalog."default",
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    is_interest_active boolean,
    CONSTRAINT account_pkey PRIMARY KEY (id)
);


INSERT INTO "bank" (name)
VALUES
  ('Clever-Bank'),
  ('Dummy-Bank'),
  ('Banko'),
  ('Another-Bank'),
  ('LalalaBank'),
  ('MiskatonicBank');
INSERT INTO "user" (username, email)
VALUES
  ('smithJohn', 'smithJohn@gmail.com'),
  ('doeJane', 'doeJane@gmail.com'),
  ('brownMike', 'brownMike@gmail.com'),
  ('wilsonSarah', 'wilsonSarah@gmail.com'),
  ('leeDavid', 'leeDavid@gmail.com'),
  ('chenLinda', 'chenLinda@gmail.com'),
  ('martinezCarlos', 'martinezCarlos@gmail.com'),
  ('lopezMaria', 'lopezMaria@gmail.com'),
  ('hallRobert', 'hallRobert@gmail.com'),
  ('nguyenJennifer', 'nguyenJennifer@gmail.com'),
  ('jonesWilliam', 'jonesWilliam@gmail.com'),
  ('whiteEmily', 'whiteEmily@gmail.com'),
  ('smithChris', 'smithChris@gmail.com'),
  ('wilsonLaura', 'wilsonLaura@gmail.com'),
  ('martinDaniel', 'martinDaniel@gmail.com'),
  ('kimSusan', 'kimSusan@gmail.com'),
  ('millerJames', 'millerJames@gmail.com'),
  ('jacksonSophia', 'jacksonSophia@gmail.com'),
  ('brownKevin', 'brownKevin@gmail.com'),
  ('nguyenHelen', 'nguyenHelen@gmail.com'),
  ('wilsonMatthew', 'wilsonMatthew@gmail.com'),
  ('leeAnna', 'leeAnna@gmail.com'),
  ('smithRobert', 'smithRobert@gmail.com'),
  ('chenSophia', 'chenSophia@gmail.com'),
  ('johnsonMichael', 'johnsonMichael@gmail.com');
  INSERT INTO "account" (bank_id, amount, user_id, currency, start_date, end_date)
VALUES
  (4, 5000, 8, 'USD', '2023-02-15 08:30:00', '2026-02-15 08:30:00'),
  (1, 2500, 12, 'EUR', '2023-03-20 12:15:00', '2026-03-20 12:15:00'),
  (3, 750, 5, 'GBP', '2023-04-10 16:45:00', '2026-04-10 16:45:00'),
  (2, 3000, 9, 'CAD', '2023-05-05 14:20:00', '2026-05-05 14:20:00'),
  (5, 12000, 7, 'AUD', '2023-06-30 10:10:00', '2026-06-30 10:10:00'),
  (1, 600, 2, 'JPY', '2023-07-25 19:05:00', '2026-07-25 19:05:00'),
  (4, 9000, 11, 'CHF', '2023-08-08 11:50:00', '2026-08-08 11:50:00'),
  (3, 1500, 6, 'SEK', '2023-09-12 07:30:00', '2026-09-12 07:30:00'),
  (2, 700, 10, 'NOK', '2023-10-03 23:55:00', '2026-10-03 23:55:00'),
  (5, 18000, 4, 'DKK', '2023-11-28 03:40:00', '2026-11-28 03:40:00'),
  (3, 4200, 1, 'SGD', '2023-12-22 21:25:00', '2026-12-22 21:25:00'),
  (4, 820, 3, 'HKD', '2024-01-18 18:00:00', '2027-01-18 18:00:00'),
  (1, 3600, 5, 'INR', '2024-02-14 22:45:00', '2027-02-14 22:45:00'),
  (2, 920, 8, 'CNY', '2024-03-10 15:35:00', '2027-03-10 15:35:00'),
  (5, 13200, 12, 'KRW', '2024-04-05 09:20:00', '2027-04-05 09:20:00'),
  (3, 6100, 7, 'MXN', '2024-05-01 04:05:00', '2027-05-01 04:05:00'),
  (4, 2700, 11, 'BRL', '2024-06-25 13:50:00', '2027-06-25 13:50:00'),
  (1, 420, 9, 'ARS', '2024-07-20 17:15:00', '2027-07-20 17:15:00'),
  (2, 9800, 6, 'ZAR', '2024-08-15 20:40:00', '2027-08-15 20:40:00'),
  (5, 5300, 2, 'RUB', '2024-09-10 11:00:00', '2027-09-10 11:00:00'),
  (1, 220, 10, 'TRY', '2024-10-05 06:30:00', '2027-10-05 06:30:00'),
  (3, 770, 4, 'PLN', '2024-11-29 19:55:00', '2027-11-29 19:55:00'),
  (4, 6400, 8, 'HUF', '2024-12-24 22:10:00', '2027-12-24 22:10:00'),
  (2, 15000, 3, 'THB', '2025-01-20 14:45:00', '2028-01-20 14:45:00'),
  (1, 2800, 12, 'MYR', '2025-02-14 09:30:00', '2028-02-14 09:30:00'),
  (3, 890, 5, 'SGD', '2025-03-10 03:15:00', '2028-03-10 03:15:00'),
  (2, 4000, 9, 'HKD', '2025-04-05 18:00:00', '2028-04-05 18:00:00'),
  (5, 11000, 7, 'INR', '2025-05-30 11:45:00', '2028-05-30 11:45:00'),
  (1, 550, 2, 'JPY', '2025-06-25 06:30:00', '2028-06-25 06:30:00'),
  (4, 8500, 11, 'CNY', '2025-07-20 01:15:00', '2028-07-20 01:15:00'),
  (3, 1200, 6, 'KRW', '2025-08-14 16:00:00', '2028-08-14 16:00:00'),
  (2, 680, 10, 'MXN', '2025-09-09 09:45:00', '2028-09-09 09:45:00'),
  (5, 14500, 4, 'BRL', '2025-10-04 04:30:00', '2028-10-04 04:30:00'),
  (3, 2600, 1, 'ARS', '2025-11-28 19:15:00', '2028-11-28 19:15:00'),
  (4, 740, 3, 'ZAR', '2025-12-23 13:00:00', '2028-12-23 13:00:00'),
  (1, 7600, 5, 'RUB', '2026-01-17 07:45:00', '2029-01-17 07:45:00'),
  (2, 380, 8, 'TRY', '2026-02-11 02:30:00', '2029-02-11 02:30:00'),
  (5, 12500, 12, 'PLN', '2026-03-07 21:15:00', '2029-03-07 21:15:00'),
  (3, 4200, 7, 'HUF', '2026-04-01 16:00:00', '2029-04-01 16:00:00'),
  (4, 640, 11, 'THB', '2026-04-26 10:45:00', '2029-04-26 10:45:00'),
  (1, 3100, 9, 'MYR', '2026-05-21 05:30:00', '2029-05-21 05:30:00'),
  (2, 760, 6, 'SGD', '2026-06-15 00:15:00', '2029-06-15 00:15:00');