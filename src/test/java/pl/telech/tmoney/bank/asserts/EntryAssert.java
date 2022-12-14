package pl.telech.tmoney.bank.asserts;

import static org.assertj.core.api.Assertions.assertThat;

import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.asserts.EntityAssert;
import pl.telech.tmoney.commons.enums.Mode;


public class EntryAssert extends EntityAssert<Entry, EntryDto> {

	
	private EntryAssert(EntryDto result) {
		super(result);
	}
	
	public static EntryAssert assertThatDto(EntryDto result) {
		return new EntryAssert(result);
	}
	
	@Override
	protected void compare(EntryDto dto, Mode mode) {
		if(mode == Mode.GET) {
			assertThat(dto.getId()).isEqualTo(entity.getId());
		}
		if(mode != Mode.UPDATE) {
			assertThat(dto.getAccount()).isNotNull();
			assertThat(dto.getAccount().getId()).isEqualTo(entity.getAccount().getId());
			assertThat(dto.getBalance()).isEqualTo(entity.getBalance());
			assertThat(dto.getBalanceOverall()).isEqualTo(entity.getBalanceOverall());
		}
		
		assertThat(dto.getDate()).isEqualTo(entity.getDate());
		assertThat(dto.getCategory()).isNotNull();
		assertThat(dto.getCategory().getId()).isEqualTo(entity.getCategory().getId());
		assertThat(dto.getName()).isEqualTo(entity.getName());
		assertThat(dto.getAmount()).isEqualTo(entity.getAmount());
		assertThat(dto.getDescription()).isEqualTo(entity.getDescription());
	}
	
	@Override
	protected void checkIfUnmappedFieldsAreNull(Mode mode) {
		super.checkIfUnmappedFieldsAreNull(mode);
		
		if (mode == Mode.UPDATE) {
			assertThat(entity.getAccount()).isNull();	
			assertThat(entity.getBalance()).isNull();
			assertThat(entity.getBalanceOverall()).isNull();
		}
	}
		
}
