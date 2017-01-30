ALTER TABLE poc_users DROP COLUMN role;
ALTER TABLE poc_users ADD COLUMN password varchar(50);

create table poc_user_roles (
    id             SERIAL PRIMARY KEY,
	poc_user_id       INT NOT NULL,
	role              varchar(16) not null,
	FOREIGN KEY (poc_user_id) REFERENCES poc_users(id)
);