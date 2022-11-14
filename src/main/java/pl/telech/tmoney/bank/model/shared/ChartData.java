package pl.telech.tmoney.bank.model.shared;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = PRIVATE)
public class ChartData {
	
	List<MonthData> months;
}



