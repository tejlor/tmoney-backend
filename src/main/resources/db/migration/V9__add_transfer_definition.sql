CREATE TABLE bank.transfer_definition (
	id serial4 NOT NULL,
	source_account_id int4 NOT NULL,
	destination_account_id int4 NOT NULL,
	category_id int4 NOT NULL,
	"name" varchar(100) NOT NULL,
	description varchar(255) NOT NULL,
	CONSTRAINT transfer_definition_pkey PRIMARY KEY (id),
	CONSTRAINT transfer_definition_source_account_id_fkey FOREIGN KEY (source_account_id) REFERENCES bank.account(id),
	CONSTRAINT transfer_definition_destination_account_id_fkey FOREIGN KEY (destination_account_id) REFERENCES bank.account(id),
	CONSTRAINT transfer_definition_category_id_fkey FOREIGN KEY (category_id) REFERENCES bank.category(id)
);
