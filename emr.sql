
drop database if exists myemr;
create database myemr;

use myemr;

drop table if exists login;
drop table if exists doctorinfo;
drop table if exists medicinfo;

create table login (username varchar(255) not null primary key, hashvalue varchar(32) not null);

create table basicinfo (username varchar(255) not null primary key, fname varchar(255), lname varchar(255), gender varchar(10),
birth date, phone varchar(7), address varchar(255));

create table medicinfo (username varchar(255) not null primary key, bloodtype varchar(2), height int, weight int,
last_examination date);

insert into login values ("Ethan", "202cb962ac59075b964b07152d234b70");
insert into login values ("Tim", "250cf8b51c773f3f8dc8b4be867a9a02");
insert into login values ("Kate", "68053af2923e00204c3ca7c6a3150cf7");
insert into login values ("Paul", "900150983cd24fb0d6963f7d28e17f72");
insert into login values ("Zoe", "4ed9407630eb1000c0f6b63842defa7d");

insert into basicinfo values ("Ethan", "Howard", "Smith", "Male", "1988-09-27", "4138975", "25 Hill Road");
insert into basicinfo values ("Tim", "Cathy", "Green", "Male", "1988-03-24", "5789830", "3 Main Street");
insert into basicinfo values ("Kate", "Marry", "Spence", "Female", "1987-12-21", "4196278", "15 Bay Road");
insert into basicinfo values ("Paul", "Nate", "Derick", "Male", "1989-06-09", "3569723", "79 College Road");
insert into basicinfo values ("Zoe", "Alice", "Zhang", "Female", "1986-10-29", "5002830", "31 5th Avenue");

insert into medicinfo values ("Ethan", "A", 172, 140, "2014-05-17");
insert into medicinfo values ("Tim", "AB", 183, 170, "2013-12-09");
insert into medicinfo values ("Kate", "A", 169, 120, "2014-03-21");
insert into medicinfo values ("Paul", "O", 180, 165, "2014-08-30");
insert into medicinfo values ("Zoe", "B", 166, 110, "2014-02-25");






