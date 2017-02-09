DROP TABLE IF EXISTS Author_phone, Publisher_phone, Borrowed, Written_by, Author, Book, Publisher, Member, Phone;
					

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