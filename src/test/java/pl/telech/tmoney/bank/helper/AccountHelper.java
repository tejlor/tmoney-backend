package pl.telech.tmoney.bank.helper;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.telech.tmoney.bank.builder.AccountBuilder;
import pl.telech.tmoney.bank.model.entity.Account;

@Component
public class AccountHelper {

	@Autowired
	EntityManager entityManager;
	
	
	@Transactional
	public Account save(String name) {
		return new AccountBuilder()
			.name(name)
			.save(entityManager);
	}
	
	@Transactional
	public Account save(String name, String code) {
		return new AccountBuilder()
			.name(name)
			.code(code)
			.save(entityManager);
	}
	
	@Transactional
	public Account save(String name, boolean active) {
		return new AccountBuilder()
			.name(name)
			.active(active)
			.save(entityManager);
	}
	
	public Account build(String name) {
		return new AccountBuilder()
			.name(name)
			.build();
	}
}
