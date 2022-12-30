package pl.telech.tmoney.bank.logic.pdf;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.bank.dao.EntryDAO;
import pl.telech.tmoney.bank.dao.data.EntryAmount;
import pl.telech.tmoney.bank.logic.interfaces.AccountLogic;
import pl.telech.tmoney.bank.logic.interfaces.EntryLogic;
import pl.telech.tmoney.bank.model.data.*;
import pl.telech.tmoney.bank.model.entity.Account;
import pl.telech.tmoney.bank.model.entity.Entry;
import pl.telech.tmoney.commons.model.shared.FileResult;
import pl.telech.tmoney.commons.utils.TStreamUtils;
import pl.telech.tmoney.commons.utils.TUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

	final EntryDAO entryDao;

	final AccountLogic accountLogic;
	final EntryLogic entryLogic;
	
	final PdfTableGenerator pdfTableGenerator;	
	final PdfReportGenerator pdfReportGenerator;
	
	
	public FileResult generateTable(String accountCode) { 	
		Account account = accountCode != null 
				? accountLogic.loadByCode(accountCode) 
				: accountLogic.getSummaryAccount();
		
		List<Entry> entries = entryLogic.loadAll(accountCode);
		return pdfTableGenerator.generateFile(account, entries);
	}
	
	public FileResult generateReport(LocalDate dateFrom, LocalDate dateTo) {
		BigDecimal initialBalance = entryDao.findLastBeforeDate(dateFrom).getBalanceOverall();
		
		var data = ReportData.builder()
				.dateFrom(dateFrom)
				.dateTo(dateTo)
				.accountsData(calculateAccountsData(dateFrom, dateTo))
				.summaryData(calculateSummaryData(dateFrom, dateTo, initialBalance))
				.chartData(calculateChartData(dateFrom, dateTo, initialBalance))
				.build();
	
		return pdfReportGenerator.generateFile(data);
	}
	
	private List<AccountReportData> calculateAccountsData(LocalDate dateFrom, LocalDate dateTo) {
		return TStreamUtils.map(accountLogic.loadActive(), account -> calculateAccountData(account, dateFrom, dateTo));
	}
	
	private AccountReportData calculateAccountData(Account account, LocalDate dateFrom, LocalDate dateTo) {
		int accountId = account.getId();
		
		return AccountReportData.builder()
			.account(account)
			.initialBalance(entryDao.findLastByAccountBeforeDate(accountId, dateFrom).getBalance())
			.finalBalance(entryDao.findLastByAccountBeforeDate(accountId, dateTo.plusDays(1)).getBalance())
			.incomesSum(entryDao.findAccountIncome(accountId, dateFrom, dateTo))
			.outcomesSum(entryDao.findAccountOutcome(accountId, dateFrom, dateTo))
			.build();
	}
	
	private SummaryReportData calculateSummaryData(LocalDate dateFrom, LocalDate dateTo, BigDecimal initialBalance) {	
		return SummaryReportData.builder()
			.initialBalance(initialBalance)
			.finalBalance(entryDao.findLastBeforeDate(dateTo.plusDays(1)).getBalanceOverall())
			.incomes(TStreamUtils.sort(entryDao.findSummaryIncomeByCategory(dateFrom, dateTo)))
			.outcomes(TStreamUtils.sort(entryDao.findSummaryOutcomeByCategory(dateFrom, dateTo)))
			.build();
	}
	
	private ChartData calculateChartData(LocalDate dateFrom, LocalDate dateTo, BigDecimal initialBalance) {
		return ChartData.builder()
				.months(calculateMonthList(dateFrom, dateTo, initialBalance))
				.build();
	}
	
	private List<MonthData> calculateMonthList(LocalDate dateFrom, LocalDate dateTo, BigDecimal initialBalance) {
		List<MonthData> result = entryDao.findEntriesForReport(dateFrom, dateTo).stream()
			.collect(Collectors.groupingBy(entry -> entry.getDate().getMonth()))
			.entrySet()
			.stream()
			.map(this::mapMonthData)
			.sorted()
			.collect(Collectors.toList());
		
		calculateBalance(result, initialBalance);
		
		return result;
	}
	
	private MonthData mapMonthData(Map.Entry<Month, List<EntryAmount>> entry) {
		Month month = entry.getKey();
		List<EntryAmount> entries = entry.getValue();
		
		return MonthData.builder()
				.month(month)
				.income(TUtils.sum(entries, EntryAmount::getAmount, e -> e.getAmount().signum() > 0))
				.outcome(TUtils.sum(entries, EntryAmount::getAmount, e -> e.getAmount().signum() < 0).negate())
				.build();
	}
	
	private void calculateBalance(List<MonthData> months, BigDecimal initialBalance) {
		BigDecimal balance = initialBalance;
		
		for(MonthData month : months) {
			System.out.println(month.getMonth().name() + " " + balance + " " + month.calcProfit());
			balance = balance.add(month.calcProfit());
			month.setBalance(balance);
		}
	}
	
}
