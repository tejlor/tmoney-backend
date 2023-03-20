package pl.telech.tmoney.bank.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.bank.logic.BankLogic;

@ExtendWith(SpringExtension.class)
@Import(BankLogic.class)
class BankLogicTest {


}
