DROP TABLE IF EXISTS dog;

CREATE TABLE dog
(
    id     LONG AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(250) NOT NULL,
    breed  VARCHAR(250) NOT NULL,
    origin VARCHAR(250)
);

INSERT INTO dog (id, name, breed, origin)
VALUES (1, 'MAX', 'Basque Shepherd Dog', 'Korea');
INSERT INTO dog (id, name, breed, origin)
VALUES (2, 'FAX', 'Combai', 'Japan');
INSERT INTO dog (id, name, breed, origin)
VALUES (3, 'PINEAPPLE', 'German Wirehaired Pointer', 'Germany');
INSERT INTO dog (id, name, breed, origin)
VALUES (4, 'MANGO', 'Kerry Beagle', 'India');
INSERT INTO dog (id, name, breed, origin)
VALUES (5, 'ROOKIE', 'Old English Terrier', 'England');
