package pl.telech.tmoney.bank.helper;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.telech.tmoney.bank.builder.AccountBuilder;
import pl.telech.tmoney.bank.model.entity.Account;

@Component
@Transactional
public class AccountHelper {

	@Autowired
	EntityManager entityManager;
	
	
	public Account save(String name) {
		return new AccountBuilder()
			.name(name)
			.save(entityManager);
	}
	
	public Account save(String name, String code) {
		return new AccountBuilder()
			.name(name)
			.code(code)
			.save(entityManager);
	}
	
	public Account save(String name, boolean active) {
		return new AccountBuilder()
			.name(name)
			.active(active)
			.save(entityManager);
	}
	
	public Account save(String name, boolean active, boolean includeInSummary) {
		return new AccountBuilder()
			.name(name)
			.active(active)
			.includeInSummary(includeInSummary)
			.save(entityManager);
	}
}
