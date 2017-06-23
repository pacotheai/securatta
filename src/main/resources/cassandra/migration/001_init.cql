CREATE TABLE user (
  name text,
  username text PRIMARY KEY
);

CREATE TABLE user_credentials (
  username text PRIMARY KEY,
  password text
);
