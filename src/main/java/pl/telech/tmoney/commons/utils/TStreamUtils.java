package pl.telech.tmoney.commons.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;

@UtilityClass
@ExtensionMethod(Extensions.class)
public class TStreamUtils {

	public <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
		return mapToList(collection, mapper);
	}
	

	public <T, R> List<R> mapToList(Collection<T> collection, Function<T, R> mapper) {
		return collection.stream()
				.map(mapper)
				.list();
	}
	
	public <T, R> Set<R> mapToSet(Collection<T> collection, Function<T, R> mapper) {
		return collection.stream()
				.map(mapper)
				.collect(Collectors.toSet());
	}
	
	public <T extends Comparable<T>> List<T> sort(List<T> list) {
		Collections.sort(list);
		return list;
	}
}
