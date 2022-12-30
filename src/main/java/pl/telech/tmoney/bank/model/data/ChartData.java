package pl.telech.tmoney.bank.model.data;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChartData {
	
	List<MonthData> months;
}



