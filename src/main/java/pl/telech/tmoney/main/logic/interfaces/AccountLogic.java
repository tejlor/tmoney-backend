package pl.telech.tmoney.main.logic.interfaces;

import pl.telech.tmoney.commons.logic.interfaces.AbstractLogic;
import pl.telech.tmoney.main.model.entity.Account;

public interface AccountLogic extends AbstractLogic<Account> {

	Account create(Account _account);

	Account update(int id, Account _account);

}
