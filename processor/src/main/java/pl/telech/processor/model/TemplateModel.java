package pl.telech.processor.model;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TemplateModel {
	
	String packageName;
	String moduleName;
	String path;
	String type;
	List<String> methods;

}
