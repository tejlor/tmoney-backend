package pl.telech.tmoney.bank.logic.transaction;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

import lombok.experimental.UtilityClass;
import pl.telech.tmoney.bank.model.entity.Entry;

@UtilityClass
public class CategoryResolver {

	private static final int PETROL = 8;
	private static final int CAR = 66;
	private static final int INTERNET = 64;
	private static final int GADGETS = 41;

	private static final int SHOPPING_FOOD = 71;
	private static final int SHOPPING_OTHER = 17;
	
	private static final int IKE = 67;
	private static final int IKZE = 68;
	
	private static final int WORK_INCOME = 54;
	private static final int US_TAXES = 53;
	private static final int ZUS_TAXES = 52;
	
	private static final List<Pair<String, Integer>> patterns = List.of(
			Pair.of("IKE", IKE),
			Pair.of("IKZE", IKZE),
			Pair.of("Lidl|Hert", SHOPPING_FOOD),
			Pair.of("Shell|Orlen", PETROL),
			Pair.of("Spotify|Allegro|Blik", GADGETS),
			Pair.of("Faktura", WORK_INCOME),
			Pair.of("Bricomarche", SHOPPING_OTHER),
			Pair.of("T\\-Mobile", INTERNET),
			Pair.of("VAT7K|PIT\\-5", US_TAXES),
			Pair.of("składki", ZUS_TAXES),
			Pair.of("RL/LS", CAR),
			Pair.of("KARTĄ", SHOPPING_FOOD)
	);
	
	public static Optional<Integer> resolve(Entry entry) {
		for (var pair : patterns) {
			Pattern p = Pattern.compile(pair.getKey(), Pattern.CASE_INSENSITIVE);
			if (p.matcher(entry.getName()).find()) {
				return Optional.of(pair.getValue());
			}
		}
		return Optional.empty();
	}
}
