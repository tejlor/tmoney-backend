package pl.telech.tmoney.bank.model.shared;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = PRIVATE)
public class ReportData {

	LocalDate dateFrom;
	LocalDate dateTo;
	List<AccountReportData> accountsData;
	SummaryReportData summaryData;
	ChartData chartData;
}

