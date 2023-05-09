DROP TABLE IF EXISTS AutoPaymentLoans; 
DROP TABLE IF EXISTS Payment;
DROP TABLE IF EXISTS AccruedInterest;
DROP TABLE IF EXISTS OverdraftAttempt;
DROP TABLE IF EXISTS AccountTransaction;
DROP TABLE IF EXISTS Account;
DROP TABLE IF EXISTS BankCustomer;
DROP TABLE IF EXISTS Branch;


CREATE TABLE IF NOT EXISTS Branch (
  bank_name VARCHAR(20) NOT NULL,
  address VARCHAR(30) DEFAULT NULL,
  city VARCHAR(20) DEFAULT NULL,
  assets DECIMAL(12,4) DEFAULT NULL,
  CONSTRAINT PRIMARY KEY (bank_name)
);


CREATE TABLE IF NOT EXISTS BankCustomer (
  cust_name VARCHAR(20) NOT NULL,
  pin CHAR(4) NOT NULL,
  address VARCHAR(30) DEFAULT NULL,
  city VARCHAR(20) DEFAULT NULL,
  CONSTRAINT PRIMARY KEY (cust_name)
);


CREATE TABLE IF NOT EXISTS Account (
  acc_num INT(11) NOT NULL AUTO_INCREMENT,
  cust_name VARCHAR(20) DEFAULT NULL,
  bank_name VARCHAR(20) DEFAULT NULL,
  type VARCHAR(15) NOT NULL,
  balance DECIMAL(12,4) DEFAULT NULL,
  interest_rate DECIMAL(4,2) DEFAULT NULL, /* e.g., 1.5 stored for 1.5% APY, NULL if not savings account */
  CONSTRAINT PRIMARY KEY (acc_num),
  CONSTRAINT FOREIGN KEY (cust_name) REFERENCES BankCustomer(cust_name) 
    ON DELETE NO ACTION 
    ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (bank_name) REFERENCES Branch(bank_name) 
    ON DELETE NO ACTION 
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS AccountTransaction (
  transaction_id INT(11) AUTO_INCREMENT,
  acc_num INT(11) NOT NULL,
  description VARCHAR(100) NOT NULL,
  amount DECIMAL(12,4) NOT NULL, /* positive for credit, negative for debit */
  transaction_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT PRIMARY KEY (transaction_id),
  CONSTRAINT FOREIGN KEY (acc_num) REFERENCES Account(acc_num) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS OverdraftAttempt (
  transaction_id INT(11),
  balance DECIMAL(12,4) DEFAULT NULL,
  CONSTRAINT PRIMARY KEY (transaction_id),
  CONSTRAINT FOREIGN KEY (transaction_id) REFERENCES AccountTransaction (transaction_id) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS AccruedInterest (
  acc_num INT(11),
  interest DECIMAL(20,10) DEFAULT 0.0000,
  CONSTRAINT PRIMARY KEY(acc_num),
  CONSTRAINT FOREIGN KEY (acc_num) REFERENCES Account(acc_num) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE
);


-- --------------------------------------------------------


INSERT INTO Branch (bank_name, address, city, assets) VALUES
  ('CollegeAppleton', '678 college ave', 'Appleton', 10000.0000),
  ('GrandChuteAppleton', '345 sweet st', 'Appleton', 5000.0000),
  ('Menasha', '102 cool ave', 'Menasha', 9000.0000),
  ('Neenah', '789 street st', 'Neenah', 4000.0000),
  ('Oshkosh20th', '456 20th st', 'Oshkosh', 200000.0000),
  ('OshkoshMain', '123 main st', 'Oshkosh', 100000.0000);


INSERT INTO BankCustomer (cust_name, address, city, pin) VALUES
  ('Bill Jones', '456 commercial st', 'Neenah', '1234'),
  ('Fred Flintstone', 'Taylor Hall', 'Oshkosh', '5678'),
  ('Henry Ford', '789 new st', 'Fond Du Lac', '9102'),
  ('Jane Doe', '123  main st', 'Oshkosh', '3456'),
  ('John Thomas', '123  new st', 'Menasha', '7891'),
  ('Thelma Jones', '456 commercial st', 'Neenah', '2345');


INSERT INTO Account (cust_name, bank_name, type, balance, interest_rate) VALUES
  ('Jane Doe', 'OshkoshMain', 'checking', 1000.0000, NULL),
  ('Jane Doe', 'OshkoshMain', 'savings', 2000.0000, 1.5),
  ('Fred Flintstone', 'OshkoshMain', 'checking', 40000.0000, NULL),
  ('Fred Flintstone', 'Oshkosh20th', 'checking', 10000.0000, NULL),
  ('Fred Flintstone', 'Oshkosh20th', 'savings', 5000000.0000, 1.4),
  ('Bill Jones', 'Neenah', 'savings', 10000.0000, 2.0);
