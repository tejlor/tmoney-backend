package pl.telech.tmoney.bank.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;

import pl.telech.tmoney.bank.asserts.CategoryAssert;
import pl.telech.tmoney.bank.helper.AccountHelper;
import pl.telech.tmoney.bank.helper.CategoryHelper;
import pl.telech.tmoney.bank.helper.EntryHelper;
import pl.telech.tmoney.bank.mapper.CategoryMapper;
import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.model.dto.TableDataDto;
import pl.telech.tmoney.commons.model.dto.TableDataDto.TableInfoDto;
import pl.telech.tmoney.commons.model.shared.TableParams;
import pl.telech.tmoney.utils.BaseMvcTest;

@ExtendWith(SpringExtension.class)
public class CategoryControllerTest extends BaseMvcTest {

	private static final String baseUrl = "/categories";

	@Autowired
	AccountHelper accountHelper;

	@Autowired
	CategoryHelper categoryHelper;
	
	@Autowired
	EntryHelper entryHelper;
	
	@Autowired
	CategoryMapper categoryMapper;
	
	@Test
	void getById() throws Exception {	
		// given
		Category category = categoryHelper.save("Samochód");
		
		// when
		CategoryDto responseDto = get(baseUrl + "/" + category.getId(), CategoryDto.class);
		
		// then
		System.out.print(responseDto.getDefaultName());
		CategoryAssert.assertThatDto(responseDto)
			.isMappedFrom(category);
	}
	
	@Test
	void getAll() throws Exception {	
		// given
		Category category0 = categoryHelper.save("Zakupy");
		Category category1 = categoryHelper.save("Podatki");
		
		// when
		List<CategoryDto> response = get(baseUrl, new TypeReference<List<CategoryDto>>() {});
		
		// then
		assertThat(response).hasSize(2);
		CategoryAssert.assertThatDto(response.get(0)).isMappedFrom(category1);
		CategoryAssert.assertThatDto(response.get(1)).isMappedFrom(category0);
	}
	
	@Test
	void getByAccountCode() throws Exception {
		// given
		Account account = accountHelper.save("Konto bankowe", "BANK");
		Category category0 = categoryHelper.save("Samochód", 1 << account.getId());
		Category category1 = categoryHelper.save("Zakupy", 1 << account.getId());
		categoryHelper.save("Praca", 1 << (account.getId() + 1));
		
		// when
		List<CategoryDto> result = get(baseUrl + "/account/" + account.getCode(), new TypeReference<List<CategoryDto>>() {});
		
		// then
		assertThat(result).hasSize(2);
		CategoryAssert.assertThatDto(result.get(0)).isMappedFrom(category0);
		CategoryAssert.assertThatDto(result.get(1)).isMappedFrom(category1);
	}
	
	@Test
	void getTable() throws Exception {	
		// given
		categoryHelper.save("Zakupy");
		categoryHelper.save("Podatki"); // 2
		categoryHelper.save("Samochód");
		categoryHelper.save("Inne");
		categoryHelper.save("Praca");
		categoryHelper.save("Gadżety"); // 1
		categoryHelper.save("Zwierzęta"); // 3
		
		// when
		String url = String.format(baseUrl + "/table?pageNo=%d&pageSize=%d&filter=%s&sortBy=%s", 1, 2, "t", "name ASC");
		TableDataDto<CategoryDto> result = get(url, new TypeReference<TableDataDto<CategoryDto>>() {});	
		
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
	void create() throws Exception {	
		// given
		Category category = categoryHelper.build("Zakupy");
		CategoryDto requestDto = categoryMapper.toDto(category);
		
		// when
		CategoryDto responseDto = post(baseUrl, requestDto, CategoryDto.class);
		
		// then	
		Category createdCategory = dbHelper.load(Category.class, responseDto.getId());	
		CategoryAssert.assertThatDto(responseDto)
			.isMappedFrom(createdCategory)
			.createdBy(requestDto);
	}
	
	@Test
	void update() throws Exception {	
		// given
		Category category = categoryHelper.save("Zakupy");
		CategoryDto requestDto = categoryMapper.toDto(category);
		requestDto.setDefaultName("Zakupy spożywcze");
		
		// when
		CategoryDto responseDto = put(baseUrl + "/" + category.getId(), requestDto, CategoryDto.class);
		
		// then	
		assertThat(responseDto.getId()).isEqualTo(category.getId());
		Category updatedCategory = dbHelper.load(Category.class, responseDto.getId());	
		CategoryAssert.assertThatDto(responseDto)
			.isMappedFrom(updatedCategory)
			.updatedBy(requestDto);
	}
	
	@Test
	void delete() throws Exception {	
		// given
		Category category = categoryHelper.save("Samochód");
		Category newCategory = categoryHelper.save("Samochodowe zakupy");
		Entry entry = entryHelper.save("Zakupy", category);
		
		// when
		String url = String.format(baseUrl + "/%d?newCategoryId=%d", category.getId(), newCategory.getId());
		MvcResult result = delete(url);	
		
		// then
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
		
		Category deletedCategory = dbHelper.load(Category.class, category.getId());
		assertThat(deletedCategory).isNull();
		
		Entry updatedEntry = dbHelper.load(Entry.class, entry.getId());
		assertThat(updatedEntry.getCategory().getId()).isEqualTo(newCategory.getId());
	}
	
	@Test
	void delete_error() throws Exception {	
		// given
		Category category = categoryHelper.save("Samochód");
		entryHelper.save("Zakupy", category);
		
		// when
		MvcResult result = delete(baseUrl + "/" + category.getId());	
		
		// then
		assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(result.getResponse().getContentAsString()).contains("Cannot delete");
		
	}

}
