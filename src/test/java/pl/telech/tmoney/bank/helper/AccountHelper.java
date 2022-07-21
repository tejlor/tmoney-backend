package pl.telech.tmoney.bank.helper;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.telech.tmoney.bank.builder.AccountBuilder;
import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.enums.Mode;

@Component
public class AccountHelper {

	@Autowired
	EntityManager entityManager;
	
	
	public void assertEqual(AccountDto dto, Account model, Mode mode) {
		if(mode != Mode.CREATE) {
			assertThat(dto.getId()).isEqualTo(model.getId());
		}
		assertThat(dto.getCode()).isEqualTo(model.getCode());
		assertThat(dto.getName()).isEqualTo(model.getName());
		assertThat(dto.getActive()).isEqualTo(model.getActive());
		assertThat(dto.getColor()).isEqualTo(model.getColor());
		assertThat(dto.getLightColor()).isEqualTo(model.getLightColor());
		assertThat(dto.getDarkColor()).isEqualTo(model.getDarkColor());
		assertThat(dto.getOrderNo()).isEqualTo(model.getOrderNo());
	}
	
	public Account save(String name) {
		return new AccountBuilder()
			.name(name)
			.save(entityManager);
	}
	
	public Account save(String name, boolean active) {
		return new AccountBuilder()
			.name(name)
			.active(active)
			.save(entityManager);
	}
	
	public Account save(String name, String code) {
		return new AccountBuilder()
			.name(name)
			.code(code)
			.save(entityManager);
	}
	
	public Account build(String name) {
		return new AccountBuilder()
			.name(name)
			.build();
	}
}
