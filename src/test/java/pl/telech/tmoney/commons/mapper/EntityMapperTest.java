package pl.telech.tmoney.commons.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.commons.asserts.EntityAssert;
import pl.telech.tmoney.commons.builder.AbstractBuilder;
import pl.telech.tmoney.commons.model.dto.AbstractDto;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

@ExtendWith(SpringExtension.class)
public abstract class EntityMapperTest<E extends AbstractEntity, T extends AbstractDto> {

	protected abstract AbstractBuilder<E> getBuilder();
	protected abstract EntityMapper<E, T> getMapper();
	protected abstract Class<?> getAssertionsClass();
	
	@Test
	public void testToDto() throws Exception {
		// given
		E entity = getBuilder().id(1).build();

		// when
		T result = getMapper().toDto(entity);

		// then
		assertThat(result).isNotNull();
		EntityAssert<E,T> entityAssert = (EntityAssert<E, T>) getAssertThat(result);
		entityAssert.isMappedFrom(entity);
	}
	
	@Test
	public void testToDtoList() throws Exception {
		// given
		List<E> list = List.of(getBuilder().id(1).build());

		// when
		List<T> result = getMapper().toDtoList(list);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		EntityAssert<E,T> entityAssert = (EntityAssert<E, T>) getAssertThat(result.get(0));
		entityAssert.isMappedFrom(list.get(0));
	}
	
	@Test
	public void testToEntity() throws Exception {
		// given
		T dto = getMapper().toDto(getBuilder().id(1).build());

		// when
		E result = getMapper().toEntity(dto);

		// then
		assertThat(result).isNotNull();
		EntityAssert<E,T> entityAssert = (EntityAssert<E, T>) getAssertThat(dto);
		entityAssert.isMappedFrom(result);
	}
	
	@Test
	public void testCreate() throws Exception {
		// given
		T dto = getMapper().toDto(getBuilder().id(1).build());

		// when
		E result = getMapper().create(dto);

		// then
		assertThat(result).isNotNull();
		EntityAssert<E,T> entityAssert = (EntityAssert<E, T>) getAssertThat(dto);
		entityAssert.creates(result);
	}

	@Test
	public void testUpdate() throws Exception {
		// given
		T dto = getMapper().toDto(getBuilder().id(1).build());
		prepareDtoForUpdateTest(dto);

		// when
		E updatedEntity = getEntityType().getDeclaredConstructor().newInstance();
		E result = getMapper().update(updatedEntity, dto);

		// then
		assertThat(result).isSameAs(updatedEntity);
		EntityAssert<E,T> entityAssert = (EntityAssert<E, T>) getAssertThat(dto);
		entityAssert.updates(result);
	}
	
	protected void prepareDtoForUpdateTest(T dto) {
		// set ids of child objects
	}
	
	private EntityAssert<E,T> getAssertThat(T arg) throws Exception {
		return (EntityAssert<E, T>) getAssertionsClass().getMethod("assertThatDto", getDtoType()).invoke(null, arg);
	}
	
	private Class<E> getEntityType() {
		return (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	private Class<T> getDtoType() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
}
