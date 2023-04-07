package pl.telech.tmoney.bank.logic.tag;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class EntryTagCalculator {

	private static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");
	
	final Map<String, Function<LocalDate, String>> map = new HashMap<>();
	
	
	public EntryTagCalculator() {
		map.put("miesiac", 		this::currentMonth);
		map.put("miesiac-1", 	this::previousMonth);
		map.put("kwartal", 		this::currentQuarter);
		map.put("kwartal-1", 	this::previousQuarter);
		map.put("rok", 			this::currentYear);
		map.put("rok-1", 		this::previousYear);
	}
	
	public String replaceTagsWithValues(String text, LocalDate date) {
		if (StringUtils.isBlank(text)) {
			return null;
		}
		
		for (var entry : map.entrySet()) {
			String tag = String.format("[%s]", entry.getKey());
			text = text.replace(tag, entry.getValue().apply(date));
		}
		return text;
	}
	
	private String currentMonth(LocalDate date) {
		return monthFormatter.format(date);
	}
	
	private String previousMonth(LocalDate date) {
		return monthFormatter.format(date.minusMonths(1));
	}
	
	private String currentQuarter(LocalDate date) {
		return formatQuarter(date.get(IsoFields.QUARTER_OF_YEAR));
	}
	
	private String previousQuarter(LocalDate date) {
		return formatQuarter(date.minusMonths(3).get(IsoFields.QUARTER_OF_YEAR));
	}
	
	private String currentYear(LocalDate date) {
		return String.valueOf(date.getYear());
	}
	
	private String previousYear(LocalDate date) {
		return String.valueOf(date.getYear() - 1);
	}
	
	private String formatQuarter(int quarter) {
		switch(quarter) {
			case 1: return "I";
			case 2: return "II";
			case 3: return "III";
			case 4: return "IV";
			default: return "";
		}
	}
}
