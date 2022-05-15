package pl.telech.tmoney.bank.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.CategoryHelper;
import pl.telech.tmoney.bank.helper.EntryHelper;
import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.enums.Mode;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class CategoryControllerTest extends BaseTest {

	@Autowired
	CategoryController controller;
	
	@Autowired
	AccountHelper accountHelper;
	@Autowired
	CategoryHelper categoryHelper;
	@Autowired
	EntryHelper entryHelper;
	
	@Test
	@Transactional
	public void getAll() {	
		// given
		Category category0 = categoryHelper.save("Samochód");
		Category category1 = categoryHelper.save("Zakupy");
		Category category2 = categoryHelper.save("Praca");
		flush();
		
		// when
		List<CategoryDto> result = controller.getAll();	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		categoryHelper.assertEqual(result.get(0), category0, Mode.GET);
		categoryHelper.assertEqual(result.get(1), category1, Mode.GET);
		categoryHelper.assertEqual(result.get(2), category2, Mode.GET);
	}
	
	@Test
	@Transactional
	public void getByAccountCode() {
		// given
		Account account = accountHelper.save("Konto bankowe", "BANK");
		Category category0 = categoryHelper.save("Samochód", 1 << account.getId());
		Category category1 = categoryHelper.save("Zakupy", 1 << account.getId());
		categoryHelper.save("Praca", 1 << (account.getId() + 1));
		flush();
		
		// when
		List<CategoryDto> result = controller.getByAccountCode(account.getCode());	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		categoryHelper.assertEqual(result.get(0), category0, Mode.GET);
		categoryHelper.assertEqual(result.get(1), category1, Mode.GET);
	}
	
	@Test
	@Transactional
	public void getById() {	
		// given
		Category category = categoryHelper.save("Samochód");
		flush();
		
		// when
		CategoryDto result = controller.getById(category.getId());	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		categoryHelper.assertEqual(result, category, Mode.GET);
	}
	
	@Test
	@Transactional
	public void create() {	
		// given
		flush();
		
		// when
		Category category = categoryHelper.build("Samochód");
		CategoryDto result = controller.create(new CategoryDto(category));	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		categoryHelper.assertEqual(result, category, Mode.CREATE);
	}
	
	@Test
	@Transactional
	public void update() {	
		// given
		Category category = categoryHelper.save("Samochód");
		flush();
		
		// when
		CategoryDto dto = new CategoryDto(category);
		dto.setDefaultDescription("Nowy opis");
		CategoryDto result = controller.update(category.getId(), dto);	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		Category updatedCategory = load(Category.class, category.getId());
		categoryHelper.assertEqual(result, updatedCategory, Mode.UPDATE);
	}
	
	@Test
	@Transactional
	public void delete() {	
		// given
		Category category = categoryHelper.save("Samochód");
		flush();
		
		// when
		controller.delete(category.getId());	
		flushAndClear();
		
		// then
		Category deletedCategory = load(Category.class, category.getId());
		assertThat(deletedCategory).isNull();
	}
	
	@Test
	@Transactional
	public void delete_error() {	
		// given
		Category category = categoryHelper.save("Samochód");
		Entry entry = entryHelper.save("Zakupy", category);
		flush();
		
		// when then
		expectException(() -> controller.delete(category.getId()), 
				TMoneyException.class, String.format("Cannot delete! Category %s is used in 1 entries: [%d].", category.getName(), entry.getId())
		);
	}
	
}
