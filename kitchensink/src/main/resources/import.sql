-- You can use this file to load seed data into the database using SQL statements

insert into Country (id, name, code) values (0, 'United Kingdom', 'GB')
insert into Country (id, name, code) values (1, 'United States of America', 'US')
insert into Country (id, name, code) values (2, 'Canada', 'CA')

insert into Member (id, name, email, phoneNumber, address1, address2, city, postalCode, country_id) values (0, 'John Smith', 'john.smith@gov.uk', '02011234567', '10 Downing Street', 'Westminster', 'London', 'TOP SECRET', 0) 