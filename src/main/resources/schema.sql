DROP TABLE IF EXISTS ACCOUNT;  
CREATE TABLE ACCOUNT (  
customerNumber int auto_increment primary key,
customerName varchar(50) NOT NULL,
customerMobile varchar(20) NOT NULL,
customerEmail varchar(50) NOT NULL,
address1 varchar(50) NOT NULL,
address2 varchar(50),
accountType ENUM('S', 'C')
);  