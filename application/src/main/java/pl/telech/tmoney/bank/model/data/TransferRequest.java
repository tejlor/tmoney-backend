package pl.telech.tmoney.bank.model.data;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class TransferRequest {

	int transferDefinitionId;
	
	@NotNull
	@PastOrPresent
	LocalDate date;
	
	@NotNull
	@Positive
	BigDecimal amount;
	
	@Size(min = 1, max = 255)
	String name;
	
	String description;
}
