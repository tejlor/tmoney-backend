package pl.telech.tmoney.commons.mapper;

import java.util.Collection;
import java.util.List;

import pl.telech.tmoney.commons.model.dto.AbstractDto;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

public interface EntityMapper<E extends AbstractEntity, T extends AbstractDto> {

	T toDto(E entity);	
	List<T> toDtoList(Collection<E> entities);
	E toEntity(T dto);
	E create(T dto);
	E update(E entity, T dto);
}
