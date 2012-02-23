-- You can use this file to load seed data into the database using SQL statements

insert into Country (id, name, code) values (1, 'United Kingdom', 'GB')
insert into Country (id, name, code) values (2, 'United States of America', 'US')
insert into Country (id, name, code) values (3, 'Canada', 'CA')

insert into Member (id, name, email, phoneNumber, address1, address2, city, postalCode, country_id) values (1, 'John Smith', 'john.smith@gov.uk', '02011234567', '10 Downing Street', 'Westminster', 'London', 'SW1A 2AA', 1) 