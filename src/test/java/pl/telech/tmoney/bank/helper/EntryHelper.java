package pl.telech.tmoney.bank.helper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.telech.tmoney.bank.builder.EntryBuilder;
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.enums.Mode;

@Component
public class EntryHelper {

	@Autowired
	EntityManager entityManager;
	
	
	public void assertEqual(EntryDto dto, Entry model, Mode mode) {
		if(mode != Mode.CREATE) {
			assertThat(dto.getId()).isEqualTo(model.getId());
		}
		assertThat(dto.getAccount().getId()).isEqualTo(model.getAccount().getId());
		assertThat(dto.getDate()).isEqualTo(model.getDate());
		assertThat(dto.getCategory().getId()).isEqualTo(model.getCategory().getId());
		assertThat(dto.getName()).isEqualTo(model.getName());
		assertThat(dto.getAmount()).isEqualTo(model.getAmount());
		assertThat(dto.getDescription()).isEqualTo(model.getDescription());
		if(mode == Mode.GET) {
			assertThat(dto.getBalance()).isEqualTo(model.getBalance());
			assertThat(dto.getBalanceOverall()).isEqualTo(model.getBalanceOverall());
		}
	}
	
	public Entry save(EntryBuilder entry) {
		return entry.save(entityManager);
	}
	
	public Entry save(String name, Account account) {
		return new EntryBuilder()
			.name(name)
			.account(account)
			.save(entityManager);
	}
	
	public Entry save(String name, Category category) {
		return new EntryBuilder()
			.name(name)
			.category(category)
			.save(entityManager);
	}
	
	public Entry save(String name, LocalDate date, Account account) {
		return new EntryBuilder()
			.name(name)
			.date(date)
			.account(account)
			.save(entityManager);
	}

	public Entry save(String name, Account account, String amount) {
		return new EntryBuilder()
			.name(name)
			.account(account)
			.amount(new BigDecimal(amount))
			.save(entityManager);
	}
	
	public Entry save(String name, Account account, String amount, String balance, String balanceOverall) {
		return new EntryBuilder()
			.name(name)
			.account(account)
			.amount(new BigDecimal(amount))
			.balance(new BigDecimal(balance))
			.balanceOverall(new BigDecimal(balanceOverall))
			.save(entityManager);
	}
	
	public Entry save(String name, Account account, String date, String amount, String balance, String balanceOverall) {
		return new EntryBuilder()
			.name(name)
			.account(account)
			.date(LocalDate.parse(date))
			.amount(new BigDecimal(amount))
			.balance(new BigDecimal(balance))
			.balanceOverall(new BigDecimal(balanceOverall))
			.save(entityManager);
	}
	
	public Entry save(String name, LocalDate date, Account account, Category category, BigDecimal amount) { //
		return new EntryBuilder()
			.name(name)
			.date(date)
			.account(account)
			.category(category)
			.amount(amount)
			.save(entityManager);
	}
	
	public Entry build(String name, Account account, Category category, String amount) {
		return new EntryBuilder()
			.name(name)
			.account(account)
			.category(category)
			.amount(new BigDecimal(amount))
			.build();
	}
}
