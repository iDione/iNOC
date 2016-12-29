CREATE TABLE clients (
  id           SERIAL PRIMARY KEY,
  name         VARCHAR(128),
  email        VARCHAR(128),
  password     VARCHAR(256),
  created_at   TIMESTAMP,
  updated_at   TIMESTAMP,
  deleted      INT DEFAULT 0
);

CREATE TABLE poc_users (
  id             SERIAL PRIMARY KEY,
  client_id      INT NOT NULL,
  first_name     VARCHAR(128),
  last_name      VARCHAR(128),
  role           VARCHAR(128),
  email_address  VARCHAR(128),
  phone_number   VARCHAR(128),
  created_at     TIMESTAMP,
  updated_at     TIMESTAMP,
  deleted        INT DEFAULT 0
);

CREATE TABLE mailing_groups (
  id             SERIAL PRIMARY KEY,
  client_id      INT NOT NULL,
  name           VARCHAR(128),
  created_at     TIMESTAMP,
  updated_at     TIMESTAMP,
  deleted        INT DEFAULT 0
);

CREATE TABLE mailing_group_poc_users (
  id                SERIAL PRIMARY KEY,
  mailing_group_id  INT NOT NULL,
  poc_user_id       INT NOT NULL,
  created_at        TIMESTAMP
);

CREATE TABLE filters (
  id                SERIAL PRIMARY KEY,
  name              VARCHAR(128),
  client_id         INT NOT NULL,
  time_interval     INT NOT NULL,
  retries           INT NOT NULL,
  mailing_group_id  INT NOT NULL,
  created_at        TIMESTAMP,
  updated_at        TIMESTAMP,
  deleted           INT DEFAULT 0
);

CREATE TABLE filter_keywords (
  id             SERIAL PRIMARY KEY,
  filter_id      INT NOT NULL,
  keyword        VARCHAR(128),
  created_at     TIMESTAMP,
  updated_at     TIMESTAMP
);

CREATE TABLE filter_poc_users (
  id             SERIAL PRIMARY KEY,
  filter_id      INT NOT NULL,
  poc_user_id    INT NOT NULL,
  created_at     TIMESTAMP,
  updated_at     TIMESTAMP,
  deleted        INT DEFAULT 0
);

CREATE TABLE issues (
  id             SERIAL PRIMARY KEY,
  email_id       INT NOT NULL,
  filter_id      INT NOT NULL,
  created_at     TIMESTAMP
);

CREATE TABLE issue_poc_users (
  id                 SERIAL PRIMARY KEY,
  issue_id           INT NOT NULL,
  poc_user_id        INT NOT NULL,
  external_call_id   VARCHAR(32),
  call_status        VARCHAR(16),
  retries            INT DEFAULT 0,
  user_response      INT DEFAULT 0,
  accepted           INT DEFAULT 0,
  created_at         TIMESTAMP,
  updated_at         TIMESTAMP
);

CREATE TABLE emails (
  id                      SERIAL PRIMARY KEY,
  external_email_id       VARCHAR(128),
  client_id               INT NOT NULL,
  created_at              TIMESTAMP
);