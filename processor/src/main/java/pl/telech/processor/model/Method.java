package pl.telech.processor.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Method {

	String name;
	List<Argument> args;
}
