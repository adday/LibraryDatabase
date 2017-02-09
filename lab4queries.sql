SELECT * FROM Borrowed;


SELECT first_name, last_name, member_id, title
FROM Member NATURAL JOIN Borrowed NATURAL JOIN Book
WHERE checkin_date = '9999-01-01';