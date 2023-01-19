package pl.telech.tmoney.bank.mapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.bank.asserts.EntryAssert;
import pl.telech.tmoney.bank.builder.EntryBuilder;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.builder.AbstractBuilder;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperTest;

@ExtendWith(SpringExtension.class)
@Import({EntryBuilder.class, EntryMapperImpl.class, AccountMapperImpl.class, CategoryMapperImpl.class})
public class EntryMapperTest extends EntityMapperTest<Entry, EntryDto> {

	@Autowired
	EntryBuilder builder;

	@Autowired
	EntryMapper mapper;

	@Override
	protected AbstractBuilder<Entry> getBuilder() {
		return builder;
	}

	@Override
	protected EntityMapper<Entry, EntryDto> getMapper() {
		return mapper;
	}

	@Override
	protected Class<EntryAssert> getAssertionsClass() {
		return EntryAssert.class;
	}
	
	@Override
	protected void prepareDtoForUpdateTest(EntryDto dto) {
		dto.getAccount().setId(22);
		dto.getCategory().setId(33);
	}
}
