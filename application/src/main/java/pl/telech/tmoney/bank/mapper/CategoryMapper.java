package pl.telech.tmoney.bank.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.*;

import pl.telech.tmoney.bank.model.dto.CategoryDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Category;
import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;

@Mapper(config = EntityMapperConfig.class)
public interface CategoryMapper extends EntityMapper<Category, CategoryDto> {
	
	@Mapping(target = "accountIds", source = "accounts")
	CategoryDto toDto(Category entity);
	
	List<CategoryDto> toDtoList(Collection<Category> entities);
	
	@Named("toEntity")
	@Mapping(target = "accounts", source = "accountIds")
	Category toEntity(CategoryDto dto);
	
	@Named("create")
	@InheritConfiguration(name = "create")
	@Mapping(target = "accounts", source = "accountIds")
	Category create(CategoryDto dto);
	
	@InheritConfiguration(name = "update")
	@Mapping(target = "accounts", source = "accountIds")
	Category update(@MappingTarget Category entity, CategoryDto dto);
	
	default List<Integer> mapAccounts(List<Account> accounts) {
		if (accounts == null) {
			return List.of();
		}
		return accounts.stream().map(Account::getId).collect(Collectors.toList());
	}
	
	default List<Account> mapAccountIds(List<Integer> accountIds) {
		if (accountIds == null) {
			return List.of();
		}
		
		return accountIds.stream()
				.map(id -> {
					var account = new Account();
					account.setId(id);
					return account;
				})
				.collect(Collectors.toList());
	}
}
