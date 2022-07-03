CREATE TABLE Accounts (
	ACCOUNT_ID CHAR (5) NOT NULL UNIQUE,
	ACCOUNT_PIN CHAR (5) NOT NULL,
	CUST_NAME VARCHAR2 (50) NOT NULL,
	PRIMARY KEY (ACCOUNT_ID, ACCOUNT_PIN)
);

CREATE TABLE Checking (
	ACCOUNT_ID CHAR (5) NOT NULL,
	CHECKING NUMBER,
	PRIMARY KEY (ACCOUNT_ID),
	FOREIGN KEY (ACCOUNT_ID) REFERENCES Accounts (ACCOUNT_ID)
);

CREATE TABLE Savings (
	ACCOUNT_ID CHAR (5) NOT NULL,
	SAVINGS NUMBER,
	PRIMARY KEY (ACCOUNT_ID),
	FOREIGN KEY (ACCOUNT_ID) REFERENCES Accounts (ACCOUNT_ID)
);

INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('03318','78159','Chris');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('17325','14753','Claire');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('78321','85296','Ron');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('00845','88991','Lupa');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('64037','00000','John');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('68700','10101','Rudy');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('71167','19740','Alex');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('35408','44670','Dan');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('68705','10889','Abigail');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('00091','64228','Harris');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('71008','91375','Marques');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('20248','48265','Laina');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('06612','16170','Jack');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('10001','10205','Anthony');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('69420','00000','Cory');
INSERT INTO Accounts (ACCOUNT_ID, ACCOUNT_PIN, CUST_NAME)
  VALUES ('98710','34981','Clark');
  
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('03318', 253.18);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('17325', 53.00);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('78321', 17.51);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('00845', 2530.18);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('64037', 420.69);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('68700', 6083.92);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('71167', 2.27);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('35408', 5008.12);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('68705', .97);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('00091', 200.01);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('71008', 199.99);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('20248', 80.01);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('06612', 240.25);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('10001', 781.42);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('69420', 4083.00);
INSERT INTO Checking (ACCOUNT_ID, CHECKING)
  VALUES ('98710', 79.99);

INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('03318', 1800.00);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('17325', 800.00);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('78321', 90.00);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('00845', 179.81);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('64037', 980.00);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('68700', 200.00);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('71167', 952.14);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('35408', 11000.00);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('68705', 1.73);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('00091', 98.11);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('71008', 1800.00);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('20248', 1173.44);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('06612', 2204.33);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('10001', 27000.00);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('69420', 8800.00);
INSERT INTO Savings (ACCOUNT_ID, SAVINGS)
  VALUES ('98710', 640.00);