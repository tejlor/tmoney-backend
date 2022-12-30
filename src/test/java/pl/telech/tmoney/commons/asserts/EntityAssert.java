package pl.telech.tmoney.commons.asserts;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.commons.enums.Mode;
import pl.telech.tmoney.commons.model.dto.AbstractDto;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class EntityAssert<E extends AbstractEntity, T extends AbstractDto> {

	protected final T result;
	protected E entity;
	protected T dto;
	
	
	public EntityAssert<E,T> isMappedFrom(E entity) {
		this.entity = entity;
		
		compare(result, Mode.GET);
		return this;
	}
	
	public void createdBy(T dto) {
		compare(dto, Mode.CREATE);
	}
	
	public void updatedBy(T dto) {
		compare(dto, Mode.UPDATE);
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
	
	protected abstract void compare(T dto, Mode mode);
	
	protected void checkIfUnmappedFieldsAreNull(Mode mode) {
		assertThat(entity.getId()).isNull();
	}
}
