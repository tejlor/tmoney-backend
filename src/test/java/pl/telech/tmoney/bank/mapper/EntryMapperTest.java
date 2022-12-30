package pl.telech.tmoney.bank.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import pl.telech.tmoney.bank.asserts.EntryAssert;
import pl.telech.tmoney.bank.builder.EntryBuilder;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Entry;

@RunWith(SpringRunner.class)
@Import({EntryBuilder.class, EntryMapperImpl.class, AccountMapperImpl.class, CategoryMapperImpl.class})
public class EntryMapperTest {

	@Autowired
	EntryBuilder builder;

	@Autowired
	EntryMapper mapper;

	@Test
	public void testToDto() {
		// given
		Entry entity = builder.id(1).build();

		// when
		EntryDto result = mapper.toDto(entity);

		// then
		assertThat(result).isNotNull();
		EntryAssert.assertThatDto(result).isMappedFrom(entity);
	}
	
	@Test
	public void testToDtoList() {
		// given
		List<Entry> list = List.of(builder.id(1).build());

		// when
		List<EntryDto> result = mapper.toDtoList(list);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSameSizeAs(list);
		EntryAssert.assertThatDto(result.get(0)).isMappedFrom(list.get(0));
	}
	
	@Test
	public void testToNewEntity() {
		// given
		EntryDto dto = mapper.toDto(builder.id(1).build());

		// when
		Entry result = mapper.toNewEntity(dto);

		// then
		assertThat(result).isNotNull();
		EntryAssert.assertThatDto(dto).creates(result);
	}

	@Test
	public void testUpdate() {
		// given
		EntryDto dto = mapper.toDto(builder.id(1).build());

		// when
		Entry updatedEntity = new Entry();
		Entry result = mapper.update(dto, updatedEntity);

		// then
		assertThat(result).isSameAs(updatedEntity);
		EntryAssert.assertThatDto(dto).updates(result);
	}
}
