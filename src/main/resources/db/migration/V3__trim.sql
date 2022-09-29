UPDATE bank.account SET code = trim(code); 
UPDATE bank.account SET name = trim(name); 
UPDATE bank.entry SET name = trim(name); 
UPDATE bank.entry SET description = trim(description); 
UPDATE bank.entry SET "name" = regexp_replace(name, '\r\n', ' '); 
UPDATE bank.entry SET "name" = regexp_replace(name, '\r|\n', ' '); 
