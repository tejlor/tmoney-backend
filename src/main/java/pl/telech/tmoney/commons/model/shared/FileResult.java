package pl.telech.tmoney.commons.model.shared;

import lombok.Value;

@Value
public class FileResult {
	String name;
	String type;
	byte[] content;
}
