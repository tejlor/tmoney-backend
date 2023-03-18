package pl.telech.tmoney.bank.asserts;

import org.apache.commons.lang3.tuple.Pair;

import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.commons.asserts.EntityAssert;


public class AccountAssert extends EntityAssert<Account, AccountDto> {
		
	private AccountAssert(AccountDto result) {
		super(result);
		
		addCondition("name", 				Pair.of(Account::getName, AccountDto::getName));	
		addCondition("code", 				Pair.of(Account::getCode, AccountDto::getCode));
		addCondition("active", 				Pair.of(Account::isActive, AccountDto::isActive));
		addCondition("includeInSummary", 	Pair.of(Account::isIncludeInSummary, AccountDto::isIncludeInSummary));
		addCondition("color", 				Pair.of(Account::getColor, AccountDto::getColor));
		addCondition("lightColor", 			Pair.of(Account::getLightColor, AccountDto::getLightColor));
		addCondition("darkColor", 			Pair.of(Account::getDarkColor, AccountDto::getDarkColor));
		addCondition("orderNo", 			Pair.of(Account::getOrderNo, AccountDto::getOrderNo));
		addCondition("icon", 				Pair.of(Account::getIcon, AccountDto::getIcon));
		
		addUpdateSkipFields("code");
	}
	
	public static AccountAssert assertThatDto(AccountDto result) {
		return new AccountAssert(result);
	}
	
}
