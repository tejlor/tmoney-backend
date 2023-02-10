package pl.telech.tmoney.commons.asserts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.SoftAssertions;

import pl.telech.tmoney.commons.model.dto.AbstractDto;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

public abstract class EntityAssert<E extends AbstractEntity, T extends AbstractDto> {
	
	protected final T result;
	protected E entity;
	protected T dto;
	
	private Map<String, Pair<Function<E, Object>, Function<T, Object>>> conditions;
	private Set<String> createSkipFields;
	private Set<String> updateSkipFields;
	
	
	protected EntityAssert(T result) {
		this.result = result;
		
		conditions = new HashMap<>();
		conditions.put("id", Pair.of(AbstractEntity::getId, AbstractDto::getId));
		
		createSkipFields = new HashSet<>();
		createSkipFields.add("id");
		
		updateSkipFields = new HashSet<>();
		updateSkipFields.add("id");
	}
	
	protected void addCondition(String field, Pair<Function<E, Object>, Function<T, Object>> values) {
		conditions.put(field, values);
	}
	
	protected void addCreateSkipFields(String... fields) {
		for (String field : fields) {
			createSkipFields.add(field);
		}	
	}
	
	protected void addUpdateSkipFields(String... fields) {
		for (String field : fields) {
			updateSkipFields.add(field);
		}
	}
	
	public EntityAssert<E,T> isMappedFrom(E entity) {
		this.entity = entity;	
		compare(result, Mode.GET);
		return this;
	}
		
	public void creates(E entity) {
		this.entity = entity;
		compare(result, Mode.CREATE);	
		checkIfUnmappedFieldsAreNull(Mode.CREATE);
	}
	
	public void updates(E entity) {
		this.entity = entity;
		compare(result, Mode.UPDATE);
		checkIfUnmappedFieldsAreNull(Mode.UPDATE);
	}
	
	public void createdBy(T requestDto) {
		compare(requestDto, Mode.CREATE);
	}
	
	public void updatedBy(T requestDto) {
		compare(requestDto, Mode.UPDATE);
	}
	
	protected void compare(T dto, Mode mode) {
		Set<String> skippedFields = getSkippedFields(mode);
		
		var assertions = new SoftAssertions();
		conditions.entrySet().stream()
			.filter(entry -> !skippedFields.contains(entry.getKey()))
			.forEach(entry -> {
				Object entityValue = entry.getValue().getLeft().apply(entity);
				Object dtoValue = entry.getValue().getRight().apply(dto);
				assertions.assertThat(entityValue)
					.as(entry.getKey())
					.isEqualTo(dtoValue);
			});
		assertions.assertAll();
	}
	
	protected void checkIfUnmappedFieldsAreNull(Mode mode) {
		Set<String> skippedFields = getSkippedFields(mode);
		
		var assertions = new SoftAssertions();	
		conditions.entrySet().stream()
			.filter(entry -> skippedFields.contains(entry.getKey()))
			.forEach(entry -> {
				Object entityValue;
				try {
					entityValue = entry.getValue().getLeft().apply(entity);
				}
				catch(NullPointerException e) {
					entityValue = null;
				}
				assertions.assertThat(entityValue).as(entry.getKey()).isNull();
			});
		assertions.assertAll();
	}	
	
	private Set<String> getSkippedFields(Mode mode) {
		switch(mode) {
			case GET:
				return Set.of();
			case CREATE: 
				return createSkipFields;
			case UPDATE: 
				return updateSkipFields;
			default: 
				throw new IllegalArgumentException("Unknown mode: " + mode);
		}
	}
}

enum Mode {
	GET, CREATE, UPDATE
}
