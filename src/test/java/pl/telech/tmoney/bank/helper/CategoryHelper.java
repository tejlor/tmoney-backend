package pl.telech.tmoney.bank.helper;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.telech.tmoney.bank.builder.CategoryBuilder;
import pl.telech.tmoney.bank.model.entity.Category;

@Component
@Transactional
public class CategoryHelper {

	@Autowired
	EntityManager entityManager;
	
	
	public Category save(String name) {
		return new CategoryBuilder()
			.name(name)
			.save(entityManager);
	}
	
	public Category save(String name, boolean report) {
		return new CategoryBuilder()
			.name(name)
			.report(report)
			.save(entityManager);
	}
	
	public Category save(String name, int account) {
		return new CategoryBuilder()
			.name(name)
			.account(account)
			.save(entityManager);
	}

}
