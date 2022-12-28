package pl.telech.tmoney.bank.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static pl.telech.tmoney.utils.TestUtils.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.bank.dao.data.CategoryAmount;
import pl.telech.tmoney.bank.dao.data.EntryAmount;
import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.CategoryHelper;
import pl.telech.tmoney.bank.helper.EntryHelper;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({AccountHelper.class, CategoryHelper.class, EntryHelper.class})
@ActiveProfiles("junit")
public class EntryDAOTest {

	@Autowired
	EntryDAO dao;
	
	@Autowired
	AccountHelper accountHelper;
	@Autowired
	CategoryHelper categoryHelper;
	@Autowired
	EntryHelper entryHelper;
	
	private static Integer accountHomeId, accountBankId, categoryCarId;
	
	@BeforeEach
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
		categoryCarId = categoryCar.getId();
		
		entryHelper.save("Przelew na giełdę", 	date("2022-01-01"), accountBank, categoryStock, 		decimal("-25 000.00"));
		entryHelper.save("Odsetki z giełdy",	date("2022-01-02"), accountBank, categoryStock, 		decimal("    250.00"));
		entryHelper.save("Podatki", 			date("2022-01-05"), accountBank, categoryWork, 			decimal(" -1 230.00"));
		entryHelper.save("Wypłata", 			date("2022-01-10"), accountBank, categoryWork, 			decimal(" 10 000.00"));
		entryHelper.save("Benzyna", 			date("2022-01-10"), accountHome, categoryCar, 			decimal("   -289.45"));
		entryHelper.save("Wypłata z bankomatu",	date("2022-01-10"), accountBank, categoryMovements, 	decimal(" -2 000.00"));
		entryHelper.save("Wypłata z bankomatu",	date("2022-01-10"), accountHome, categoryMovements, 	decimal("  2 000.00"));
		entryHelper.save("Zwrot opłaty", 		date("2022-01-19"), accountHome, categoryCar, 			decimal("      5.00"));
		entryHelper.save("Lidl", 				date("2022-01-21"), accountHome, categoryShopping, 		decimal("    -87.95"));
		entryHelper.save("Biedronka", 			date("2022-01-31"), accountBank, categoryShopping, 		decimal("    -24.99"));
		entryHelper.save("Odsetki z giełdy",	date("2022-02-01"), accountBank, categoryStock, 		decimal("    350.00"));
	}
	
	@Test
	public void testFindByCategoryId() {
		// when
		List<Entry> result = dao.findByCategoryId(categoryCarId);
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertThat(result).extracting(Entry.Fields.date, Entry.Fields.name)
			.containsExactly(
					tuple(date("2022-01-10"), "Benzyna"),
					tuple(date("2022-01-19"), "Zwrot opłaty")
			);
	}
	
	@Test
	public void testFindByAccountId() {
		// when
		List<Entry> result = dao.findByAccountId(accountHomeId);
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(4);
		assertThat(result).extracting(Entry.Fields.date, Entry.Fields.name)
			.containsExactly(
					tuple(date("2022-01-10"), "Benzyna"),
					tuple(date("2022-01-10"), "Wypłata z bankomatu"),
					tuple(date("2022-01-19"), "Zwrot opłaty"),
					tuple(date("2022-01-21"), "Lidl")
			);
	}
	
//	@Test
//	public void testFindLastByAccountBeforeDate() {
//		// when
//		Entry result = dao.findLastByAccountBeforeDate(accountHomeId, date("2022-02-01"));
//		
//		// then
//		assertThat(result).isNotNull();
//		assertThat(result.getName()).isEqualTo("Lidl");
//	}
//	
//	@Test
//	public void testFindLastBeforeDate() {
//		// when
//		Entry result = dao.findLastBeforeDate(date("2022-02-01"));
//		
//		// then
//		assertThat(result).isNotNull();
//		assertThat(result.getName()).isEqualTo("Biedronka");
//	}
	
	@Test
	public void testFindAccountIncome() {
		// when
		BigDecimal result = dao.findAccountIncome(accountBankId, date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(decimal("10 250.00"));
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
	public void testFindSummaryIncomeByCategory() {
		// when
		List<CategoryAmount> result = dao.findSummaryIncomeByCategory(date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		assertThat(result).extracting(CategoryAmount.Fields.categoryName, CategoryAmount.Fields.amount)
			.containsOnly(
					tuple("Giełda", decimal("250.00")),
					tuple("Praca", decimal("10 000.00")),
					tuple("Samochód", decimal("5.00"))
			);
	}
	
	@Test
	public void testFindSummaryOutcomeByCategory() {
		// when
		List<CategoryAmount> result = dao.findSummaryOutcomeByCategory(date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(4);
		assertThat(result).extracting(CategoryAmount.Fields.categoryName, CategoryAmount.Fields.amount)
			.containsOnly(
					tuple("Giełda", decimal("25 000.00")), 
					tuple("Praca", decimal("1 230.00")),
					tuple("Samochód", decimal("289.45")),
					tuple("Zakupy", decimal("112.94"))
			);
	}
	
	@Test
	public void testFindEntriesForReport() {
		// when
		List<EntryAmount> result = dao.findEntriesForReport(date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(8);
		assertThat(result).extracting(EntryAmount.Fields.date, EntryAmount.Fields.amount)
			.containsOnly(
					tuple(date("2022-01-01"), decimal("-25 000.00")),
					tuple(date("2022-01-02"), decimal("250.00")),
					tuple(date("2022-01-05"), decimal("-1 230.00")),
					tuple(date("2022-01-10"), decimal("10 000.00")),
					tuple(date("2022-01-10"), decimal("-289.45")),
					tuple(date("2022-01-19"), decimal("5.00")),
					tuple(date("2022-01-21"), decimal("-87.95")),
					tuple(date("2022-01-31"), decimal("-24.99"))
			);
	}
	
}
