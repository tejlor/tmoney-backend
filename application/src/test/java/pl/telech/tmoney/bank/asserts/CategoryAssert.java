package pl.telech.tmoney.bank.asserts;

import org.apache.commons.lang3.tuple.Pair;

import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.asserts.EntityAssert;
import pl.telech.tmoney.commons.utils.TStreamUtils;

public class CategoryAssert extends EntityAssert<Category, CategoryDto> {
	
	private CategoryAssert(CategoryDto result) {
		super(result);
		
		addCondition("name", 				Pair.of(Category::getName, CategoryDto::getName));	
		addCondition("accounts", 			Pair.of(cat -> TStreamUtils.map(cat.getAccounts(), Account::getId), CategoryDto::getAccountIds));	
		addCondition("report", 				Pair.of(Category::getReport, CategoryDto::getReport));	
		addCondition("defaultName", 		Pair.of(Category::getDefaultName, CategoryDto::getDefaultName));	
		addCondition("defaultAmount", 		Pair.of(Category::getDefaultAmount, CategoryDto::getDefaultAmount));	
		addCondition("deefaultDescription", Pair.of(Category::getDefaultDescription, CategoryDto::getDefaultDescription));	
		
		addUpdateSkipFields("accountId", "categoryId", "balance", "balanceOverall");
	}
	
	public static CategoryAssert assertThatDto(CategoryDto result) {
		return new CategoryAssert(result);
	}	
}
