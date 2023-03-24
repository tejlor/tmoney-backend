package pl.telech.tmoney.bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.telech.tmoney.utils.TestUtils.*;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.telech.tmoney.bank.logic.tag.EntryTagCalculator;

@ExtendWith(SpringExtension.class)
@Import(EntryTagCalculator.class)
class EntryTagCalculatorTest {
	
	@Autowired
	EntryTagCalculator entryTagCalculator;

	@MethodSource
	@ParameterizedTest
	void replaceTagsWithValues(String text, String expectedResult) {
		// given
		LocalDate date = date("2023-02-15");
		
		// when
		String result = entryTagCalculator.replaceTagsWithValues(text, date);
		
		// then
		assertThat(result).isEqualTo(expectedResult);
	}
	
	private Stream<Arguments> replaceTagsWithValues() {
		return Stream.of(
				Arguments.of(null, null),
				Arguments.of(" ", null),
				Arguments.of("[miesiac] [miesiac-1] [kwartal] [kwartal-1]", "lut sty I IV")
				);
	}
	
}
