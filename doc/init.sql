-- *****************************************************************************
-- ADMINISTRATION
-- *****************************************************************************
CREATE SCHEMA adm;

-- Setting
CREATE TABLE adm.setting (
    id serial NOT NULL PRIMARY KEY,
    name varchar(32) NOT NULL,
    value varchar(255)
);
ALTER TABLE adm.setting OWNER to tmoney;

-- User
CREATE TABLE adm.user (
    id serial NOT NULL PRIMARY KEY,
    first_name varchar(32) NOT NULL,
    last_name varchar(32) NOT NULL,
    email varchar(64) NOT NULL UNIQUE,
    password char(40) NOT NULL,
    created_time timestamp without time zone NOT NULL,
    created_by_id integer NOT NULL REFERENCES adm.user(id),
    modified_time timestamp without time zone,
    modified_by_id integer REFERENCES adm.user(id)
);
ALTER TABLE adm.user OWNER to tmoney;

-- *****************************************************************************
-- AUTHENTICATION
-- *****************************************************************************
CREATE SCHEMA auth;

CREATE TABLE auth.access_token ( 
	token_id varchar(255),
	token bytea,
	authentication_id varchar(255) PRIMARY KEY,
	user_name varchar(255),
	client_id varchar(255),
	authentication bytea,
	refresh_token varchar(255)
);
ALTER TABLE auth.access_token OWNER to tmoney;

CREATE TABLE auth.refresh_token ( 
	token_id varchar(255),
	token bytea,
	authentication bytea 
);
ALTER TABLE auth.refresh_token OWNER to tmoney;

-- *****************************************************************************
-- PUBLIC
-- *****************************************************************************
CREATE TABLE account (
	id serial NOT NULL PRIMARY KEY,
	code varchar(10) NOT NULL,
	name varchar(100) NOT NULL,
	active boolean NOT NULL,
	color varchar(100),
	order_no integer
);
ALTER TABLE account OWNER to tmoney;

CREATE TABLE category (
	id serial NOT NULL PRIMARY KEY,
	name varchar(100) NOT NULL,
	account integer NOT NULL,
	report boolean NOT NULL,
	default_name varchar(100),
	default_amount decimal(10,2),
	default_description varchar(255)
);
ALTER TABLE category OWNER to tmoney;

CREATE TABLE entry (
	id serial NOT NULL PRIMARY KEY,
	account_id integer NOT NULL REFERENCES account(id),
	date timestamp WITHOUT time ZONE NOT NULL,
	category_id integer NOT NULL REFERENCES category(id),
	name varchar(100) NOT NULL,
	amount decimal(10,2) NOT NULL,
	description varchar(255),
	balance decimal(10,2) NOT NULL,
	balance_overall decimal(10,2) NOT NULL
);
ALTER TABLE entry OWNER to tmoney;

-- *****************************************************************************
-- INSERT DEFAULT DATA
-- *****************************************************************************

INSERT INTO adm.user(first_name, last_name, email, password, created_time, created_by_id)
    VALUES ('Krzysztof', 'Telech', 'tejlor@wp.pl','7c4a8d09ca3762af61e59520943dc26494f8941b', now(), 1);
    