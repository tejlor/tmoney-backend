CREATE OR REPLACE PROCEDURE bank.updatebalances()
    LANGUAGE plpgsql
    AS $$  
DECLARE   
	idx int;
	sums NUMERIC(10,2)[20] := '{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}';
	sum numeric(10,2) := 0;
	rec record;
    curs CURSOR FOR SELECT e.id, e.account_id, e.amount, c.report FROM bank.entry e INNER JOIN bank.category c ON e.category_id = c.id ORDER BY date ASC, id ASC;
BEGIN
	OPEN curs;
	LOOP
		FETCH curs INTO rec;
		EXIT WHEN NOT FOUND;
		idx := rec.account_id;
		IF rec.report THEN 
			sum := sum + rec.amount; 
		END IF;
		sums[idx] := sums[idx] + rec.amount; 
		UPDATE bank.entry SET balance = sums[idx], balance_overall = sum WHERE id = rec.id;
	END LOOP;
	CLOSE curs;
END
$$;
