package pl.telech.tmoney.bank.model.data;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
	LocalDate date;
	
	@NotNull
	@Positive
	BigDecimal amount;
	
	@NotBlank
	String name;
	
	String description;
}
