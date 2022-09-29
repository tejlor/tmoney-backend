package pl.telech.tmoney.bank.logic.pdf;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.bank.logic.interfaces.AccountLogic;
import pl.telech.tmoney.bank.logic.interfaces.EntryLogic;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.model.shared.FileResult;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class ReportService {

	@Autowired
	AccountLogic accountLogic;
	
	@Autowired
	EntryLogic entryLogic;
	
	@Autowired
	PdfTableGenerator pdfGenerator;
	
	
	public FileResult generateTable(String accountCode) { 	
		Account account;
		if (accountCode != null) {
			account = accountLogic.loadByCode(accountCode);
		}
		else {
			account = accountLogic.getSummaryAccount();
		}
		List<Entry> entries = entryLogic.loadAll(accountCode);
		return pdfGenerator.generateFile(account, entries);
	}
}
