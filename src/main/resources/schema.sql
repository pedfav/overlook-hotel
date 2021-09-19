CREATE TABLE IF NOT EXISTS reservations (
  id bigserial NOT NULL,
  id_user bigint NOT NULL,
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP NOT NULL,
  insert_date TIMESTAMP NOT NULL,
  update_date TIMESTAMP NOT NULL,
  EXCLUDE USING gist (tsrange(start_date, end_date) WITH &&)
);

CREATE TABLE IF NOT EXISTS users (
	id bigserial NOT NULL,
	birthday date NULL,
	email varchar(255) NULL,
	"name" varchar(255) NULL,
	CONSTRAINT email_const UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

