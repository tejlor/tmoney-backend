package pl.telech.tmoney.commons.utils;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Extensions {

	public <T> T orElse(T obj, T other) {
		return obj != null ? obj : other;
	}
	
	public <T> T orElse(T obj, Supplier<T> func) {
		return obj != null ? obj : func.get();
	}

	public <T> List<T> list(Stream<T> stream) {
		return stream.collect(Collectors.toList());
	}
	
	public <T> Set<T> set(Stream<T> stream) {
		return stream.collect(Collectors.toSet());
	}
}
