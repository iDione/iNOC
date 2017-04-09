ALTER TABLE issue_emails ADD COLUMN status varchar(16);
ALTER TABLE issue_emails ADD COLUMN server_code varchar(16);

ALTER TABLE emails ADD COLUMN subject varchar(256);
ALTER TABLE emails ADD COLUMN body text;