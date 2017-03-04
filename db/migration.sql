create user 'test'@'localhost' identified by 'test';
create database bookStore default charset utf8;
grant all privileges on `bookStore`.* to `test`@`localhost`;

use `bookStore`;
create table `book` (
`bookId` integer auto_increment,
`title` text not null,
`siteId` varchar(16) not null,
`fileName` text,
`thumbnail` blob,
`md5` varchar(32),
primary key(`bookId`)
) engine=InnoDB;
create index book_siteId on `book`(`siteId`);
create index book_md5 on `book`(`md5`);
