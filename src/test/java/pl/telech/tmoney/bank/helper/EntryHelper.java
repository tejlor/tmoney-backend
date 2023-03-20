package pl.telech.tmoney.bank.helper;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.telech.tmoney.bank.builder.EntryBuilder;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;

@Component
public class EntryHelper {

	@Autowired
	EntityManager entityManager;
	
		
//	public Entry save(EntryBuilder entry) {
//		return entry.save(entityManager);
//	}
	
	@Transactional
	public Entry save(String name, Account account) {
		return new EntryBuilder()
			.name(name)
			.account(account)
			.save(entityManager);
	}
	
	@Transactional
	public Entry save(String name, Category category) {
		return new EntryBuilder()
			.name(name)
			.category(category)
			.save(entityManager);
	}
	
	@Transactional
	public Entry save(String name, LocalDate date, Account account) {
		return new EntryBuilder()
			.name(name)
			.date(date)
			.account(account)
			.save(entityManager);
	}

	@Transactional
	public Entry save(String name, Account account, BigDecimal amount) {
		return new EntryBuilder()
			.name(name)
			.account(account)
			.amount(amount)
			.save(entityManager);
	}
	
//	public Entry save(String name, Account account, String amount, String balance, String balanceOverall) {
//		return new EntryBuilder()
//			.name(name)
//			.account(account)
//			.amount(new BigDecimal(amount))
//			.balance(new BigDecimal(balance))
//			.balanceOverall(new BigDecimal(balanceOverall))
//			.save(entityManager);
//	}
	
	@Transactional
	public Entry save(String name, Account account, Category category, LocalDate date, BigDecimal balance) {
		return new EntryBuilder()
			.name(name)
			.account(account)
			.category(category)
			.date(date)
			.balance(balance)
			.save(entityManager);
	}
	
	public Entry save(String name, LocalDate date, Account account, Category category, BigDecimal amount) {
		return new EntryBuilder()
			.name(name)
			.date(date)
			.account(account)
			.category(category)
			.amount(amount)
			.save(entityManager);
	}

}
