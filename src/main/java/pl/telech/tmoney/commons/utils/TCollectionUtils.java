package pl.telech.tmoney.commons.utils;

import java.util.Collection;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TCollectionUtils {

	public <T> T first(Collection<T> collection) {
		if (collection == null || collection.size() == 0) {
			return null;
		}
		return collection.iterator().next();
	}

}
