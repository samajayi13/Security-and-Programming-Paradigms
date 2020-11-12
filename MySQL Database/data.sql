CREATE TABLE userAccounts (
Firstname varchar(50) NOT NULL,
Lastname varchar(50) NOT NULL, 
Email varchar(50) NOT NULL,
Phone varchar(50) NOT NULL,
Username varchar(50) NOT NULL,
Pwd varchar(200) NOT NULL,
PRIMARY KEY (Username, Pwd)
);

INSERT INTO userAccounts VALUES
('Sara','Fletcher','sara.fletcher@myemail.co.uk','44-0191-5678901','sarafletcher','sFletch123');



