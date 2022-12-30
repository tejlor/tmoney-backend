package pl.telech.tmoney.bank.logic.interfaces;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import pl.telech.tmoney.bank.model.dto.AccountDto;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.interfaces.AbstractLogic;

public interface AccountLogic extends AbstractLogic<Account> {

	Account create(AccountDto _account);

	Account update(int id, AccountDto _account);


	Account loadByCode(String code);

	List<Account> loadActive();

	List<Pair<Account, Entry>> getAccountSummaries(String accountCode);

	Account getSummaryAccount();

}
