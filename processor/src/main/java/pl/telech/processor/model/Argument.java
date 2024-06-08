package pl.telech.processor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Argument {

	String name;
	String type;
	String defaultValue;
}
