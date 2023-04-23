package pl.telech.tmoney.commons.utils;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TExtensions {

	public static <T> T orElse(T obj, T other) {
		return obj != null ? obj : other;
	}
	
	public static <T> T orElse(T obj, Supplier<T> func) {
		return obj != null ? obj : func.get();
	}

	public static <T> List<T> list(Stream<T> stream) {
		return stream.collect(Collectors.toList());
	}
	
	public static <T> Set<T> set(Stream<T> stream) {
		return stream.collect(Collectors.toSet());
	}
}
