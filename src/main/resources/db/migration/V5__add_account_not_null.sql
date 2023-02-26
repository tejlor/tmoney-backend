ALTER TABLE bank.account ADD COLUMN logo text NULL;

ALTER TABLE bank.account ALTER COLUMN code SET NOT NULL;
ALTER TABLE bank.account ALTER COLUMN "name" SET NOT NULL;

CREATE UNIQUE INDEX account_code_idx ON bank.account (code);
CREATE UNIQUE INDEX account_name_idx ON bank.account ("name");
