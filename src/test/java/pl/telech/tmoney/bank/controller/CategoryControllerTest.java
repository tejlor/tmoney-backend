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
import pl.telech.tmoney.bank.model.dto.EntryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.enums.Mode;
import pl.telech.tmoney.commons.model.dto.TableDataDto;
import pl.telech.tmoney.commons.model.dto.TableDataDto.TableInfoDto;
import pl.telech.tmoney.commons.model.exception.TMoneyException;
import pl.telech.tmoney.commons.model.shared.TableParams;
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
		Category category1 = categoryHelper.save("Zakupy");
		Category category2 = categoryHelper.save("Podatki");
		Category category3 = categoryHelper.save("Samochód");
		flush();
		
		// when
		List<CategoryDto> result = controller.getAll();	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		categoryHelper.assertEqual(result.get(0), category2, Mode.GET);
		categoryHelper.assertEqual(result.get(1), category3, Mode.GET);
		categoryHelper.assertEqual(result.get(2), category1, Mode.GET);
	}
	
	@Test
	@Transactional
	public void getTable() {	
		// given
		categoryHelper.save("Zakupy");
		categoryHelper.save("Podatki"); // 2
		categoryHelper.save("Samochód");
		categoryHelper.save("Inne");
		categoryHelper.save("Praca");
		categoryHelper.save("Gadżety"); // 1
		categoryHelper.save("Zwierzęta"); // 3
		flush();
		
		// when
		TableDataDto<CategoryDto> result = controller.getTable(1, 2, "t", "name ASC");	
		flushAndClear();
		
		// then
		assertThat(result).isNotNull();

		TableParams tableParams = result.getTableParams();
		assertThat(tableParams.getPageNo()).isEqualTo(1);
		assertThat(tableParams.getPageSize()).isEqualTo(2);
		assertThat(tableParams.getFilter()).isEqualTo("t");
		assertThat(tableParams.getSortBy()).isEqualTo("name ASC");
		
		TableInfoDto tableInfo = result.getTableInfo();
		assertThat(tableInfo.getPageCount()).isEqualTo(2);
		assertThat(tableInfo.getRowCount()).isEqualTo(3);
		assertThat(tableInfo.getRowStart()).isEqualTo(3);
		assertThat(tableInfo.getRowEnd()).isEqualTo(3);
		
		List<CategoryDto> rows = result.getRows();
		assertThat(rows).hasSize(1);
		assertThat(rows.get(0).getName()).isEqualTo("Zwierzęta");
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
		Category newCategory = categoryHelper.save("Samochodowe zakupy");
		Entry entry = entryHelper.save("Zakupy", category);
		flush();
		
		// when
		controller.delete(category.getId(), newCategory.getId());	
		flushAndClear();
		
		// then
		Category deletedCategory = load(Category.class, category.getId());
		assertThat(deletedCategory).isNull();
		
		Entry updatedEntry = load(Entry.class, entry.getId());
		assertThat(updatedEntry.getCategoryId()).isEqualTo(newCategory.getId());
	}
	
	@Test
	@Transactional
	public void delete_error() {	
		// given
		Category category = categoryHelper.save("Samochód");
		Entry entry = entryHelper.save("Zakupy", category);
		flush();
		
		// when then
		expectException(() -> controller.delete(category.getId(), null), 
				TMoneyException.class, String.format("Cannot delete! Category %s is used in 1 entries: [%d].", category.getName(), entry.getId())
		);
	}
	
}
