create table users (
	username varchar(50) not null primary key,
	password varchar(50) not null,
	enabled boolean not null,
	poc_user_id        INT NOT NULL,
	FOREIGN KEY (poc_user_id) REFERENCES poc_users(id)
);

create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	FOREIGN KEY (username) REFERENCES users(username)
);