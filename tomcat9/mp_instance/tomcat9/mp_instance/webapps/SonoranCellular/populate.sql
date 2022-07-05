INSERT INTO Member
	VALUES('nick@gmail.com','Nick','I am a student in Tuscon. I have a pet.','Tuscon','22-APR-21','n','y',NULL);
INSERT INTO Member
	VALUES('jayden@gmail.com','Jayden','I am a student in Tuscon. I have a pet.','Tuscon','22-APR-21','n','y',NULL);
INSERT INTO Member
	VALUES('james@gmail.com','James','I am a student in Tuscon. I have a pet.','Tuscon','22-APR-21','y','n','I am an adopter.');
INSERT INTO Member
	VALUES('ben@gmail.com','Ben','I am a student in Tuscon. I have a pet.','Tuscon','22-APR-21','y','n','I am an adopter.');

INSERT INTO Pet
	VALUES('jayden@gmail.com','Cindy','22-APR-2020','y','Cat',NULL,NULL);
INSERT INTO Pet
	VALUES('nick@gmail.com','Cindy','22-APR-2020','y','Cat',NULL,NULL);
INSERT INTO Pet
	VALUES('jayden@gmail.com','David','30-APR-2019','n','Dog',1,NULL);
INSERT INTO Pet
	VALUES('jayden@gmail.com','Rita','10-JAN-2018','y','Rabbit',NULL,'n');
INSERT INTO Pet
	VALUES('ben@gmail.com','Tom','10-JAN-2018','y','Rabbit',NULL,'n');

INSERT INTO Favorite
	VALUES('nick@gmail.com','jayden@gmail.com','Cindy','22-APR-2020');
INSERT INTO Favorite
	VALUES('nick@gmail.com','jayden@gmail.com','David','30-APR-2019');
INSERT INTO Favorite
	VALUES('nick@gmail.com','jayden@gmail.com','Rita','10-JAN-2018');
INSERT INTO Favorite
	VALUES('james@gmail.com','jayden@gmail.com','Rita','10-JAN-2018');

INSERT INTO ChatMessage
	VALUES(17,NULL,'How are you today?','22-APR-21','nick@gmail.com','ben@gmail.com');
INSERT INTO ChatMessage
	VALUES(19,NULL,'Good morning!','22-APR-21','nick@gmail.com','james@gmail.com');
INSERT INTO ChatMessage
	VALUES(20,17,'Fine. Do you want to send your pet?','22-APR-21','ben@gmail.com','nick@gmail.com');
INSERT INTO ChatMessage
	VALUES(21,20,'Yes. Do you like it?','22-APR-21','nick@gmail.com','ben@gmail.com');
