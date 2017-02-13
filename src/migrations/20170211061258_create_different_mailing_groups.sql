ALTER TABLE filters RENAME COLUMN mailing_group_id to assigned_mailing_group_id;
ALTER TABLE filters ADD COLUMN unassigned_mailing_group_id int;
ALTER TABLE filters ADD COLUMN resolved_mailing_group_id int;