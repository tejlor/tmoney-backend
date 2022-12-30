package pl.telech.tmoney.bank.model.data;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportData {

	LocalDate dateFrom;
	LocalDate dateTo;
	List<AccountReportData> accountsData;
	SummaryReportData summaryData;
	ChartData chartData;
}

