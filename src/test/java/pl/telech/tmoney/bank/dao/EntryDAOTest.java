package pl.telech.tmoney.bank.dao;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static pl.telech.tmoney.utils.TestUtils.date;
import static pl.telech.tmoney.utils.TestUtils.decimal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.builder.EntryBuilder;
import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.CategoryHelper;
import pl.telech.tmoney.bank.helper.EntryHelper;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.bank.model.shared.CategoryAmount;
import pl.telech.tmoney.bank.model.shared.EntryAmount;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({AccountHelper.class, CategoryHelper.class, EntryHelper.class})
@ActiveProfiles("junit")
@FieldDefaults(level = PRIVATE)
public class EntryDAOTest {

	@Autowired
	EntryDAO dao;
	
	@Autowired
	AccountHelper accountHelper;
	@Autowired
	CategoryHelper categoryHelper;
	@Autowired
	EntryHelper entryHelper;
	
	private static Integer accountHomeId, accountBankId;
	
	@Before
	public void initDB() {
		Account accountHome = accountHelper.save("Dom");
		Account accountBank = accountHelper.save("Konto bankowe");	
		accountHomeId = accountHome.getId();
		accountBankId = accountBank.getId();
		
		Category categoryStock = categoryHelper.save("Giełda");
		Category categoryWork = categoryHelper.save("Praca");
		Category categoryCar = categoryHelper.save("Samochód");
		Category categoryShopping = categoryHelper.save("Zakupy");
		Category categoryMovements = categoryHelper.save("Przelewy pomiędzy rachunkami", false);
		
		saveEntry("Zasilenie konta", date("2022-01-01"), accountBank, categoryStock, decimal("-25 000.00"));
		saveEntry("Podatki", date("2022-01-05"), accountBank, categoryWork, decimal("-1 230.00"));
		saveEntry("Wypłata", date("2022-01-10"), accountBank, categoryWork, decimal("10 000.00"));
		saveEntry("Benzyna", date("2022-01-10"), accountHome, categoryCar, decimal("-289.45"));
		saveEntry("Przelew z firmowego", date("2022-01-10"), accountBank, categoryMovements, decimal("2000.00"));
		saveEntry("Zwrot opłaty", date("2022-01-19"), accountHome, categoryCar, decimal("5.00"));
		saveEntry("Lidl", date("2022-01-21"), accountHome, categoryShopping, decimal("-87.95"));
		saveEntry("Biedronka", date("2022-01-31"), accountBank, categoryShopping, decimal("-24.99"));
		saveEntry("Odsetki", date("2022-02-01"), accountBank, categoryStock, decimal("350.00"));
	}
	
	@Test
	public void testFindLastByAccountBeforeDate() {
		// when
		Entry result = dao.findLastByAccountBeforeDate(accountHomeId, date("2022-02-01"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo("Lidl");
	}
	
	@Test
	public void testFindLastBeforeDate() {
		// when
		Entry result = dao.findLastBeforeDate(date("2022-02-01"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo("Biedronka");
	}
	
	@Test
	public void testFindAccountIncome() {
		// when
		BigDecimal result = dao.findAccountIncome(accountBankId, date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(decimal("12 000.00"));
	}
	
	@Test
	public void testFindAccountOutcome() {
		// when
		BigDecimal result = dao.findAccountOutcome(accountHomeId, date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(decimal("377.40"));
	}
	
	@Test
	public void testFindSummaryIncome() {
		// when
		List<CategoryAmount> result = dao.findSummaryIncome(date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertThat(result).extracting(CategoryAmount.Fields.categoryName, CategoryAmount.Fields.amount)
			.contains(
					tuple("Praca", decimal("10 000.00")),
					tuple("Samochód", decimal("5.00"))
			);
	}
	
	@Test
	public void testFindSummaryOutcome() {
		// when
		List<CategoryAmount> result = dao.findSummaryOutcome(date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(4);
		assertThat(result).extracting(CategoryAmount.Fields.categoryName, CategoryAmount.Fields.amount)
			.contains(
					tuple("Giełda", decimal("25 000.00")), 
					tuple("Praca", decimal("1 230.00")),
					tuple("Samochód", decimal("289.45")),
					tuple("Zakupy", decimal("112.94"))
			);
	}
	
	@Test
	public void testFindSummaryChart() {
		// when
		List<EntryAmount> result = dao.findSummaryChart(date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(7);
		assertThat(result).extracting(EntryAmount.Fields.date, EntryAmount.Fields.amount)
			.contains(
					tuple(date("2022-01-01"), decimal("-25 000.00")), 		
					tuple(date("2022-01-05"), decimal("-1 230.00")),
					tuple(date("2022-01-10"), decimal("10 000.00")),
					tuple(date("2022-01-10"), decimal("-289.45")),
					tuple(date("2022-01-19"), decimal("5.00")),
					tuple(date("2022-01-21"), decimal("-87.95")),
					tuple(date("2022-01-31"), decimal("-24.99"))
			);
	}
	
	private void saveEntry(String name, LocalDate date, Account account, Category category, BigDecimal amount) {
		entryHelper.save(
			new EntryBuilder()
				.name(name)
				.date(date)
				.account(account)
				.category(category)
				.amount(amount));
	}
	
}
