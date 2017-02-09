SELECT * FROM Book 
ORDER BY ISBN;

SELECT * FROM Member
ORDER BY last_name, first_name;

SELECT * FROM Author
ORDER BY last_name, first_name;

SELECT * FROM Publisher
ORDER BY pub_name;

SELECT * FROM Phone
ORDER BY p_number;

SELECT * FROM Author_phone;

SELECT * FROM Publisher_phone;

SELECT * FROM Borrowed;

SELECT * FROM Written_by;

SELECT first_name, last_name
FROM Member
WHERE LEFT(last_name,1) = 'B';

SELECT ISBN, title, yr_published
FROM Book NATURAL JOIN Publisher 
WHERE pub_name = 'Coyote Publishing'
ORDER BY title;

SELECT first_name, last_name, member_id, ISBN, title, yr_published
FROM Member NATURAL JOIN Borrowed NATURAL JOIN Book
WHERE checkin_date = '9999-01-01';

SELECT first_name, last_name, author_id, title
FROM Author NATURAL JOIN Written_by NATURAL JOIN Book;

SELECT first_name, last_name, p_number
FROM Author_phone NATURAL JOIN Author
WHERE p_number IN (SELECT p_number FROM Author_phone GROUP BY p_number HAVING count(*) > 1);
