CREATE TABLE Accounts (
   ACCT_NUMBER CHAR (5) NOT NULL UNIQUE,
   ACCT_PIN CHAR (5) NOT NULL,
   CUST_NAME VARCHAR2 (50) NOT NULL,
   CHECKING NUMBER,
   SAVINGS NUMBER,
   PRIMARY KEY (ACCT_NUMBER, ACCT_PIN)
);

INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('03318','78159','Chris', 253.18, 1800.00);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('17325','14753','Claire', 53.00, 800.00);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('78321','85296','Ron', 17.51, 90.00);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('00845','88991','Lupa', 2530.18, 179.81);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('64037','00000','John', 420.69, 980.00);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('68700','10101','Rudy', 6083.92, 200.00);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('71167','19740','Alex', 2.27, 952.14);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('35408','44670','Dan', 5008.12, 11000.00);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('68705','10889','Abigail', .97, 1.73);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('00091','64228','Harris', 200.01, 98.11);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('71008','91375','Marques', 199.99, 1800.00);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('20248','48265','Laina', 80.01, 1173.44);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('06612','16170','Jack', 240.25, 2204.33);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('10001','10205','Anthony', 781.42, 27000.00);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('69420','00000','Cory', 4083.00, 8800.00);
INSERT INTO Accounts (ACCT_NUMBER, ACCT_PIN, CUST_NAME, CHECKING, SAVINGS)
  VALUES ('98710','34981','Clark', 79.99, 640.00);
