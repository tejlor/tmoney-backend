CREATE SCHEMA adm;
ALTER SCHEMA adm OWNER TO tmoney;

CREATE SCHEMA auth;
ALTER SCHEMA auth OWNER TO tmoney;

CREATE SCHEMA bank;
ALTER SCHEMA bank OWNER TO tmoney;

CREATE SCHEMA public;
ALTER SCHEMA public OWNER TO postgres;
COMMENT ON SCHEMA public IS 'standard public schema';

CREATE PROCEDURE bank.updatebalances()
    LANGUAGE plpgsql
    AS $$  
DECLARE   
	idx int;
	sums NUMERIC(10,2)[20] := '{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}';
	sum numeric(10,2) := 0;
	rec record;
    curs CURSOR FOR SELECT id, account_id, amount FROM bank.entry ORDER BY date ASC, id ASC;
BEGIN
	OPEN curs;
	LOOP
		FETCH curs INTO rec;
		EXIT WHEN NOT FOUND;
		idx := rec.account_id;
		sum := sum + rec.amount; 
		sums[idx] := sums[idx] + rec.amount; 
		UPDATE bank.entry SET balance = sums[idx], balance_overall = sum WHERE id = rec.id;
	END LOOP;
	CLOSE curs;
END
$$;
ALTER PROCEDURE bank.updatebalances() OWNER TO tmoney;


CREATE TABLE adm.setting (
	id serial4 NOT NULL,
	"name" varchar(32) NOT NULL,
	value varchar(255) NULL,
	CONSTRAINT setting_pkey PRIMARY KEY (id)
);

CREATE TABLE adm."user" (
	id serial4 NOT NULL,
	first_name varchar(32) NOT NULL,
	last_name varchar(32) NOT NULL,
	email varchar(64) NOT NULL,
	"password" bpchar(40) NOT NULL,
	created_time timestamp NOT NULL,
	created_by_id int4 NOT NULL,
	modified_time timestamp NULL,
	modified_by_id int4 NULL,
	CONSTRAINT user_email_key UNIQUE (email),
	CONSTRAINT user_pkey PRIMARY KEY (id),
	CONSTRAINT user_created_by_id_fkey FOREIGN KEY (created_by_id) REFERENCES adm."user"(id),
	CONSTRAINT user_modified_by_id_fkey FOREIGN KEY (modified_by_id) REFERENCES adm."user"(id)
);

CREATE TABLE auth.access_token (
	token_id varchar(255) NULL,
	"token" bytea NULL,
	authentication_id varchar(255) NOT NULL,
	user_name varchar(255) NULL,
	client_id varchar(255) NULL,
	authentication bytea NULL,
	refresh_token varchar(255) NULL,
	CONSTRAINT access_token_pkey PRIMARY KEY (authentication_id)
);

CREATE TABLE auth.refresh_token (
	token_id varchar(255) NULL,
	"token" bytea NULL,
	authentication bytea NULL
);

CREATE TABLE bank.account (
	id serial4 NOT NULL,
	code varchar(10) NULL,
	"name" varchar(100) NULL,
	active bool NOT NULL,
	color varchar(7) NULL,
	order_no varchar(3) NULL,
	icon varchar(50) NULL,
	light_color varchar(7) NULL,
	dark_color varchar(7) NULL,
	CONSTRAINT account_pkey PRIMARY KEY (id)
);

CREATE TABLE bank.category (
	id serial4 NOT NULL,
	"name" varchar(100) NULL,
	account int4 NOT NULL,
	report bool NOT NULL,
	default_name varchar(100) NULL,
	default_amount numeric(10, 2) NULL,
	default_description varchar(255) NULL,
	CONSTRAINT category_pkey PRIMARY KEY (id)
);

CREATE TABLE bank.entry (
	id serial4 NOT NULL,
	account_id int4 NOT NULL,
	"date" timestamp NOT NULL,
	category_id int4 NOT NULL,
	"name" varchar(100) NULL,
	amount numeric(10, 2) NULL,
	description varchar(255) NULL,
	balance numeric(10, 2) NULL,
	balance_overall numeric(10, 2) NULL,
	CONSTRAINT entry_pkey PRIMARY KEY (id),
	CONSTRAINT entry_account_id_fkey FOREIGN KEY (account_id) REFERENCES bank.account(id),
	CONSTRAINT entry_category_id_fkey FOREIGN KEY (category_id) REFERENCES bank.category(id)
);
