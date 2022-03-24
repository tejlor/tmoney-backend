package pl.telech.tmoney.utils;

import static lombok.AccessLevel.PRIVATE;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.test.util.ReflectionTestUtils;

@FieldDefaults(level = PRIVATE)
public class BeanFieldSetter {
	Map<Key, OriginValue> originValueMap = new HashMap<>();

	public void restoreAllOriginValues() {
		originValueMap.values().forEach(OriginValue::restore);
		originValueMap.clear();
	}

	public void setObjectField(Object targetObject, String fieldName, Object value) {
		var key = new Key(targetObject.hashCode(), fieldName);
		restoreOriginValueIfExists(key);
		
		Object origin = ReflectionTestUtils.getField(targetObject, fieldName);
		originValueMap.put(key, new OriginValue(targetObject, fieldName, origin));
		
		ReflectionTestUtils.setField(targetObject, fieldName, value);
	}

	private void restoreOriginValueIfExists(Key key) {
		var originValue = originValueMap.get(key);
		if (originValue != null) {
			originValue.restore();
			originValueMap.remove(key);
		}
	}

	@AllArgsConstructor
	@FieldDefaults(level = PRIVATE)
	private class OriginValue {
		Object targetObject;
		String fieldName;
		Object value;

		public void restore() {
			ReflectionTestUtils.setField(targetObject, fieldName, value);
		}
	}

	@AllArgsConstructor
	@EqualsAndHashCode
	@FieldDefaults(level = PRIVATE)
	private class Key {
		int objectHashCode;
		String fieldName;
	}
}
