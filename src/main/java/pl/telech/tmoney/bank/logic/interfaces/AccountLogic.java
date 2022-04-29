package pl.telech.tmoney.bank.logic.interfaces;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.logic.interfaces.AbstractLogic;

public interface AccountLogic extends AbstractLogic<Account> {

	Account create(Account _account);

	Account update(int id, Account _account);

	List<Pair<Account, Entry>> getAccountSummaryList();

	Account loadByCode(String code);

}
