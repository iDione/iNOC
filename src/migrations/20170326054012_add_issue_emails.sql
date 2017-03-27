create table issue_emails (
    id                 SERIAL PRIMARY KEY,
    issue_id           INT NOT NULL,
	email_id           INT NOT NULL,
	created_at         TIMESTAMP,
	FOREIGN KEY (issue_id) REFERENCES issues(id),
	FOREIGN KEY (email_id) REFERENCES emails(id)
);