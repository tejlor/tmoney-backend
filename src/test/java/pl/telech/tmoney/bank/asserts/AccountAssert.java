package pl.telech.tmoney.bank.asserts;

import static org.assertj.core.api.Assertions.assertThat;

import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.asserts.EntityAssert;
import pl.telech.tmoney.commons.enums.Mode;


public class AccountAssert extends EntityAssert<Account, AccountDto> {
	
	private AccountAssert(AccountDto result) {
		super(result);
	}
	
	public static AccountAssert assertThatDto(AccountDto result) {
		return new AccountAssert(result);
	}
		
	@Override
	protected void compare(AccountDto dto, Mode mode) {
		if(mode == Mode.GET) {
			assertThat(dto.getId()).isEqualTo(entity.getId());
		}
		if(mode != Mode.UPDATE) {
			assertThat(dto.getCode()).isEqualTo(entity.getCode());
		}
		assertThat(dto.getName()).isEqualTo(entity.getName());
		assertThat(dto.getActive()).isEqualTo(entity.getActive());
		assertThat(dto.getColor()).isEqualTo(entity.getColor());
		assertThat(dto.getLightColor()).isEqualTo(entity.getLightColor());
		assertThat(dto.getDarkColor()).isEqualTo(entity.getDarkColor());
		assertThat(dto.getOrderNo()).isEqualTo(entity.getOrderNo());
		assertThat(dto.getIcon()).isEqualTo(entity.getIcon());
	}

	@Override
	protected void checkIfUnmappedFieldsAreNull(Mode mode) {
		super.checkIfUnmappedFieldsAreNull(mode);
		
		if (mode == Mode.UPDATE) {
			assertThat(entity.getCode()).isNull();
		}	
	}		
}
