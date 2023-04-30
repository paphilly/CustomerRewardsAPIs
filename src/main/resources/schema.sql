DROP TABLE IF EXISTS CustomerRewards;
DROP TABLE IF EXISTS Customers;

CREATE TABLE Customers (
  CustomerId INT PRIMARY KEY auto_increment,
  FirstName VARCHAR(100),
  LastName VARCHAR(100)
);

CREATE TABLE CustomerTransactions (
  transactionId INT PRIMARY KEY auto_increment,
  CustomerId INT,
  TransactionAmount DECIMAL(10,2),
  TransactionDate DATE,
  RewardPoints INT,
  FOREIGN KEY (CustomerId) REFERENCES Customers(CustomerId)
);