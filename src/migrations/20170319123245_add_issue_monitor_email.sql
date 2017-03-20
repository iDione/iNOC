create table issue_status_history (
    id                 SERIAL PRIMARY KEY,
    issue_id           INT NOT NULL,
	status             varchar(16) not null,
	created_at         TIMESTAMP,
	FOREIGN KEY (issue_id) REFERENCES issues(id)
);

ALTER TABLE clients ADD COLUMN issue_status_email VARCHAR(128);