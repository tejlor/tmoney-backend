CREATE TABLE bank.category_to_account (
	category_id int4 NOT NULL,
	account_id int4 NOT NULL,
	CONSTRAINT category_to_account_category_id_fkey FOREIGN KEY (category_id) REFERENCES bank.category(id),
	CONSTRAINT category_to_account_account_id_fkey FOREIGN KEY (account_id) REFERENCES bank.account(id)
);

ALTER TABLE bank.category DROP COLUMN account;
