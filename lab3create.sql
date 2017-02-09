/* Relations */

DROP TABLE IF EXISTS Author_phone, Publisher_phone, Borrowed, Written_by, Stored_on, Author, Book, Publisher, Member, Phone, Shelf, Library, Audit;

CREATE TABLE Author
(
author_id INT NOT NULL,
last_name VARCHAR(30),
first_name VARCHAR(30),
PRIMARY KEY (author_id)
);

CREATE TABLE Publisher
(
pub_id INT NOT NULL,
pub_name VARCHAR(50),
PRIMARY KEY (pub_id)
);

CREATE TABLE Book
(
ISBN VARCHAR(30) NOT NULL,
title VARCHAR(50),
yr_published INT,
pub_id INT,
FOREIGN KEY (pub_id) REFERENCES Publisher (pub_id) ON DELETE CASCADE,
PRIMARY KEY (ISBN)
);

CREATE TABLE Member
(
member_id INT NOT NULL,
last_name VARCHAR(20),
first_name VARCHAR(20),
DOB DATE,
gender CHAR(1),
PRIMARY KEY (member_id)
);

CREATE TABLE Phone
(
p_number VARCHAR(20) NOT NULL,
p_type CHAR(1) NOT NULL,
PRIMARY KEY (p_number)
);

CREATE TABLE Author_phone
(
author_id INT NOT NULL,
p_number VARCHAR(20) NOT NULL,
FOREIGN KEY (author_id) REFERENCES Author (author_id) ON DELETE CASCADE,
FOREIGN KEY (p_number) REFERENCES Phone (p_number) ON DELETE CASCADE,
PRIMARY KEY (author_id,p_number)
);

CREATE TABLE Publisher_phone
(
pub_id INT NOT NULL,
p_number VARCHAR(20) NOT NULL,
FOREIGN KEY (pub_id) REFERENCES Publisher (pub_id) ON DELETE CASCADE,
FOREIGN KEY (p_number) REFERENCES Phone (p_number) ON DELETE CASCADE,
PRIMARY KEY (pub_id,p_number)
);

CREATE TABLE Borrowed
(
ISBN VARCHAR(30) NOT NULL,
member_id INT NOT NULL,
checkout_date DATE NOT NULL,
checkin_date DATE,
FOREIGN KEY (ISBN) REFERENCES Book (ISBN) ON DELETE CASCADE,
FOREIGN KEY (member_id) REFERENCES Member (member_id) ON DELETE CASCADE,
PRIMARY KEY (ISBN,member_id,checkout_date)
);

CREATE TABLE Written_by
(
ISBN VARCHAR(20) NOT NULL,
author_id INT NOT NULL,
FOREIGN KEY (ISBN) REFERENCES Book (ISBN) ON DELETE CASCADE,
FOREIGN KEY (author_id) REFERENCES Author (author_id) ON DELETE CASCADE,
PRIMARY KEY (ISBN,author_id)
);

CREATE TABLE Library
(
name VARCHAR(30) NOT NULL,
street VARCHAR(30),
city VARCHAR(30),
state VARCHAR(30),
PRIMARY KEY (name)
);

CREATE TABLE Shelf
(
shelf_num INT NOT NULL,
lib_name VARCHAR(30) NOT NULL,
flr INT,
FOREIGN KEY(lib_name) REFERENCES Library (name) ON DELETE CASCADE,
PRIMARY KEY (shelf_num,lib_name)
);

CREATE TABLE Stored_on
(
shelf_num INT NOT NULL,
lib_name VARCHAR(30) NOT NULL,
ISBN VARCHAR(20) NOT NULL,
total_copies INT,
FOREIGN KEY (shelf_num) REFERENCES Shelf (shelf_num) ON DELETE CASCADE,
FOREIGN KEY (lib_name) REFERENCES Library (name) ON DELETE CASCADE,
FOREIGN KEY (ISBN) REFERENCES Book (ISBN) ON DELETE CASCADE,
PRIMARY KEY (shelf_num,lib_name,ISBN)
);

CREATE TABLE Audit
(
id INT NOT NULL  AUTO_INCREMENT,
table_name VARCHAR(30),
action VARCHAR(10),
date_time DATETIME,
PRIMARY KEY (id)
);

/* Triggers */

delimiter |

CREATE TRIGGER insert_author AFTER INSERT ON Author FOR EACH ROW 
		INSERT INTO Audit (table_name,action,date_time) VALUES ('Author','INSERT',NOW());

CREATE TRIGGER added_book AFTER INSERT ON Stored_on FOR EACH ROW
		INSERT INTO Audit (table_name,action,date_time) VALUES ('Stored_on','INSERT',NOW());
	
CREATE TRIGGER deleted_book AFTER DELETE ON Stored_on FOR EACH ROW
		INSERT INTO Audit (table_name,action,date_time) VALUES ('Stored_on','DELETE',NOW());
		
CREATE TRIGGER modify_book_count AFTER UPDATE ON Stored_on FOR EACH ROW
		BEGIN 
		IF NEW.total_copies != OLD.total_copies THEN
		INSERT INTO Audit (table_name,action,date_time) VALUES('Stored_on','UPDATE',NOW());
		END IF;
		END|

delimiter ;

/* Views */
CREATE OR REPLACE SQL SECURITY INVOKER VIEW Book_view AS
	SELECT title, GROUP_CONCAT(DISTINCT first_name," ",last_name) AS authors, shelf_num, flr, lib_name
	FROM (Book NATURAL JOIN Written_by NATURAL JOIN Author) NATURAL JOIN (Stored_on NATURAL JOIN Shelf)
	GROUP BY title;