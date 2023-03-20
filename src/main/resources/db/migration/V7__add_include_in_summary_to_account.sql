ALTER TABLE bank.account ADD COLUMN balancing_category_id int4 NULL;
ALTER TABLE bank.account ADD CONSTRAINT account_balancing_category_id_fkey FOREIGN KEY (balancing_category_id) REFERENCES bank.category(id);
