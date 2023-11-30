package pl.telech.tmoney.bank.logic.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.experimental.ExtensionMethod;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.utils.TExtensions;
import pl.telech.tmoney.commons.utils.TUtils;

@Component
@ExtensionMethod(TExtensions.class)
public class TransactionsFileParser {
	
	private static final int DATE_IDX = 1;
	private static final int NAME_IDX = 2;
	private static final int AMOUNT_IDX = 5; 
	
	public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	

	public List<Entry> parseTransactionsFile(String fileContent) {	
		return Arrays.stream(fileContent.split("\n"))
			.skip(1) // header
			.map(this::parseLine)
			.sorted(Comparator.comparing(Entry::getDate))
			.list();
	}
	
	private Entry parseLine(String line) {
		String[] data = line.split("\\|");
		
		var entry = new Entry();
		entry.setAmount(getAmount(data));
		entry.setDate(getDate(data));
		entry.setName(getName(data));
		entry.setExternalId(getExternalId(data));
		return entry;
	}
	
	private LocalDate getDate(String[] line) {
		String dateStr = line[DATE_IDX];
		try {
			return LocalDate.parse(dateStr, dateFormat);
		}
		catch(DateTimeParseException e) {
			throw new TMoneyException("Niepoprawny format daty - " + dateStr, e);
		}
	}
	
	private String getName(String[] line) {
		String name = line[NAME_IDX].strip();
		if (name.length() > 100) {
			name = name.substring(0, 100);
		}
		return name;
	}
	
	private BigDecimal getAmount(String[] line) {
		String amountStr = line[AMOUNT_IDX].replace(',', '.');
		try {
			return new BigDecimal(amountStr); 
		}
		catch (NumberFormatException e) {
			throw new TMoneyException("Niepoprawny format kwoty - " + amountStr, e);
		}
	}
	
	private String getExternalId(String[] line) {
		return TUtils.md5(line[DATE_IDX] + line[NAME_IDX] + line[AMOUNT_IDX]);
	}
}
