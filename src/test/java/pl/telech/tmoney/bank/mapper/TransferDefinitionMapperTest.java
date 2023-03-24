package pl.telech.tmoney.bank.mapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.bank.asserts.TransferDefinitionAssert;
import pl.telech.tmoney.bank.builder.TransferDefinitionBuilder;
import pl.telech.tmoney.bank.model.dto.TransferDefinitionDto;
import pl.telech.tmoney.bank.model.entity.TransferDefinition;
import pl.telech.tmoney.commons.builder.AbstractBuilder;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperTest;

@ExtendWith(SpringExtension.class)
@Import({TransferDefinitionBuilder.class, TransferDefinitionMapperImpl.class, AccountMapperImpl.class, CategoryMapperImpl.class})
public class TransferDefinitionMapperTest extends EntityMapperTest<TransferDefinition, TransferDefinitionDto> {

	@Autowired
	TransferDefinitionBuilder builder;

	@Autowired
	TransferDefinitionMapper mapper;

	@Override
	protected AbstractBuilder<TransferDefinition> getBuilder() {
		return builder;
	}

	@Override
	protected EntityMapper<TransferDefinition, TransferDefinitionDto> getMapper() {
		return mapper;
	}

	@Override
	protected Class<TransferDefinitionAssert> getAssertionsClass() {
		return TransferDefinitionAssert.class;
	}
	
	@Override
	protected void prepareDtoForUpdateTest(TransferDefinitionDto dto) {
		dto.getSourceAccount().setId(11);
		dto.getDestinationAccount().setId(22);
		dto.getCategory().setId(33);
	}
}
