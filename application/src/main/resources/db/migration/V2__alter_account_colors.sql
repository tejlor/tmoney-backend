UPDATE bank.account SET color = REPLACE(color, '#', '');
UPDATE bank.account SET light_color = REPLACE(light_color, '#', '');
UPDATE bank.account SET dark_color = REPLACE(dark_color, '#', '');

ALTER TABLE bank.account ALTER COLUMN color TYPE varchar(6) USING color::varchar;
ALTER TABLE bank.account ALTER COLUMN light_color TYPE varchar(6) USING light_color::varchar;
ALTER TABLE bank.account ALTER COLUMN dark_color TYPE varchar(6) USING dark_color::varchar;
