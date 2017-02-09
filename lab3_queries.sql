/* Queries for Setup Task 5 */

SELECT * FROM Library ORDER BY name;
SELECT * FROM Shelf ORDER BY lib_name,flr;
SELECT * FROM Stored_on ORDER BY ISBN;

SELECT title, shelf_num, lib_name
FROM Stored_on NATURAL JOIN
(SELECT ISBN, title
 FROM (SELECT ISBN FROM Stored_on WHERE lib_name = 'Main')m NATURAL JOIN 
	 (SELECT ISBN FROM Stored_on WHERE lib_name = 'South Park')sp NATURAL JOIN Book)b
ORDER BY title;

SELECT shelf_num, lib_name, SUM(total_copies)
FROM Stored_on
GROUP BY shelf_num, lib_name
ORDER BY lib_name,shelf_num;

/* Query for Setup Task 8 */
SELECT title,authors,shelf_num,lib_name 
FROM Book_view 
ORDER BY title;

/* ACTIVITY */
/* 1. Add a new book to Main Library */
UPDATE Stored_on SET total_copies = total_copies + 1 WHERE ISBN = '964201310510' AND lib_name = 'Main';
INSERT IGNORE INTO Shelf VALUES (8,'Main',2);
INSERT IGNORE INTO Book VALUES ('964201310510','Growing your own Weeds',2012,10000);
INSERT IGNORE INTO Stored_on VALUES (8,'Main','964201310510',1);

/* 2. Modify number of copies */
UPDATE Stored_on SET total_copies = 8 WHERE ISBN = '964210310907' AND lib_name = 'Main';

/* 3. Delete author Grace Slick */ 
DELETE FROM Author WHERE last_name = 'Slick' AND first_name = 'Grace';

/* 4. Add author Commander Adams */
INSERT INTO Author VALUES (305,'Adams','Commander');
INSERT INTO Phone VALUES('9705555555','o');
INSERT INTO Author_phone VALUES(305,'9705555555');

/* 5. Add a new book to South Park Library */
UPDATE Stored_on SET total_copies = total_copies + 1 WHERE ISBN = '964201310510' AND lib_name = 'South Park';
INSERT IGNORE INTO Shelf VALUES (8,'South Park',3);
INSERT IGNORE INTO Book VALUES ('964201310510','Growing your own Weeds',2012,10000);
INSERT IGNORE INTO Stored_on VALUES (8,'South Park','964201310510',1);

/* 6. Delete book Missing Tomorrow */
DELETE FROM Stored_on WHERE ISBN = (SELECT ISBN FROM Book WHERE title = 'Missing Tomorrow') AND lib_name = 'Main';

/* 7. Add 2 new copies of Eating in the Fort to South Park Library */
UPDATE Stored_on SET total_copies = total_copies + 2 WHERE ISBN = (SELECT ISBN FROM Book WHERE title = 'Eating in the Fort') AND lib_name = 'South Park';

/* 8. FAILS DUE TO REFERENTIAL INTEGRITY CONSTRAINT */

/* 9. Print contents of Audit table */
SELECT * FROM Audit;