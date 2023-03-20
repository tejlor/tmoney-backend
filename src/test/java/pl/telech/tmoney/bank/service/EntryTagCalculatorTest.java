package pl.telech.tmoney.bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.telech.tmoney.utils.TestUtils.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.bank.logic.tag.EntryTagCalculator;

@ExtendWith(SpringExtension.class)
@Import(EntryTagCalculator.class)
class EntryTagCalculatorTest {
	
	@Autowired
	EntryTagCalculator entryTagCalculator;

	
	@Test
	void replaceTagsWithValues() {
		// given
		LocalDate date = date("2023-02-15");
		String text = "[miesiac] [miesiac-1] [kwartal] [kwartal-1]";
		
		// when
		String result = entryTagCalculator.replaceTagsWithValues(text, date);
		
		// then
		assertThat(result).isEqualTo("lut sty I IV");
	}
	
}
