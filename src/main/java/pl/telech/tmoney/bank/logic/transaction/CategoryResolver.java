package pl.telech.tmoney.bank.logic.transaction;

import java.util.List;
import java.util.Optional;

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
			Pair.of("LIDL|HERT", SHOPPING_FOOD),
			Pair.of("SHELL|ORLEN", PETROL),
			Pair.of("SPOTIFY|ALLEGRO", GADGETS),
			Pair.of("PEOPLEVIBE", WORK_INCOME),
			Pair.of("Bricomarche", SHOPPING_OTHER),
			Pair.of("T-MOBILE", INTERNET),
			Pair.of("VAT7K|PIT-5", US_TAXES),
			Pair.of("składki", ZUS_TAXES),
			Pair.of("RL/LS", CAR)
	);
	
	public static Optional<Integer> resolve(Entry entry) {
		for (var pair : patterns) {
			if (entry.getName().matches(pair.getKey())) {
				return Optional.of(pair.getValue());
			}
		}
		return Optional.empty();
	}
}
