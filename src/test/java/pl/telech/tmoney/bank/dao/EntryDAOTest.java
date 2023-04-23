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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EntryDAOTest {

	@Autowired
	EntryDAO dao;
	
	@Autowired
	AccountHelper accountHelper;
	@Autowired
	CategoryHelper categoryHelper;
	@Autowired
	EntryHelper entryHelper;
	
	private Integer accountHomeId, accountBankId, categoryCarId;
	
	@BeforeEach
	void initDB() {
		Account accountHome = accountHelper.save("Dom", true, true);
		Account accountBank = accountHelper.save("Konto bankowe", true, true);	
		Account accountIkze = accountHelper.save("IKZE", true, false);	
		accountHomeId = accountHome.getId();
		accountBankId = accountBank.getId();
		
		Category categoryStock = categoryHelper.save("Giełda");
		Category categoryWork = categoryHelper.save("Praca");
		Category categoryCar = categoryHelper.save("Samochód");
		Category categoryShopping = categoryHelper.save("Zakupy");
		Category categoryMovements = categoryHelper.save("Przelewy pomiędzy rachunkami", false);
		categoryCarId = categoryCar.getId();
		
		entryHelper.save("Przelew na giełdę", 	date("2022-01-01"), accountBank, categoryStock, 		dec("-25 000.00"));
		entryHelper.save("Przelew na IKE", 		date("2022-01-01"), accountIkze, categoryStock, 		dec(" -9 000.00"));
		entryHelper.save("Odsetki z giełdy",	date("2022-01-02"), accountBank, categoryStock, 		dec("    250.00"));
		entryHelper.save("Podatki", 			date("2022-01-05"), accountBank, categoryWork, 			dec(" -1 230.00"));
		entryHelper.save("Wypłata", 			date("2022-01-10"), accountBank, categoryWork, 			dec(" 10 000.00"));
		entryHelper.save("Benzyna", 			date("2022-01-10"), accountHome, categoryCar, 			dec("   -289.45"));
		entryHelper.save("Wypłata z bankomatu",	date("2022-01-10"), accountBank, categoryMovements, 	dec(" -2 000.00"));
		entryHelper.save("Wypłata z bankomatu",	date("2022-01-10"), accountHome, categoryMovements, 	dec("  2 000.00"));
		entryHelper.save("Zwrot opłaty", 		date("2022-01-19"), accountHome, categoryCar, 			dec("      5.00"));
		entryHelper.save("Lidl", 				date("2022-01-21"), accountHome, categoryShopping, 		dec("    -87.95"));
		entryHelper.save("Biedronka", 			date("2022-01-31"), accountBank, categoryShopping, 		dec("    -24.99"));
		entryHelper.save("Odsetki z giełdy",	date("2022-02-01"), accountBank, categoryStock, 		dec("    350.00"));
	}
	
	@Test
	void testFindByCategoryId() {
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
	void testFindByAccountId() {
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
	void testFindAccountIncome() {
		// when
		BigDecimal result = dao.findAccountIncome(accountBankId, date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(dec("10 250.00"));
	}
	
	@Test
	void testFindAccountOutcome() {
		// when
		BigDecimal result = dao.findAccountOutcome(accountHomeId, date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(dec("377.40"));
	}
	
	@Test
	void testFindSummaryIncomeByCategory() {
		// when
		List<CategoryAmount> result = dao.findSummaryIncomeByCategory(date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		assertThat(result).extracting(CategoryAmount.Fields.categoryName, CategoryAmount.Fields.amount)
			.containsOnly(
					tuple("Giełda", dec("250.00")),
					tuple("Praca", dec("10 000.00")),
					tuple("Samochód", dec("5.00"))
			);
	}
	
	@Test
	void testFindSummaryOutcomeByCategory() {
		// when
		List<CategoryAmount> result = dao.findSummaryOutcomeByCategory(date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(4);
		assertThat(result).extracting(CategoryAmount.Fields.categoryName, CategoryAmount.Fields.amount)
			.containsOnly(
					tuple("Giełda", dec("25 000.00")), 
					tuple("Praca", dec("1 230.00")),
					tuple("Samochód", dec("289.45")),
					tuple("Zakupy", dec("112.94"))
			);
	}
	
	@Test
	void testFindEntriesForReport() {
		// when
		List<EntryAmount> result = dao.findEntriesForReport(date("2022-01-01"), date("2022-01-31"));
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(8);
		assertThat(result).extracting(EntryAmount.Fields.date, EntryAmount.Fields.amount)
			.containsOnly(
					tuple(date("2022-01-01"), dec("-25 000.00")),
					tuple(date("2022-01-02"), dec("250.00")),
					tuple(date("2022-01-05"), dec("-1 230.00")),
					tuple(date("2022-01-10"), dec("10 000.00")),
					tuple(date("2022-01-10"), dec("-289.45")),
					tuple(date("2022-01-19"), dec("5.00")),
					tuple(date("2022-01-21"), dec("-87.95")),
					tuple(date("2022-01-31"), dec("-24.99"))
			);
	}
	
}
