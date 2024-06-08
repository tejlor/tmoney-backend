package pl.telech.processor.annotation;

import java.lang.annotation.*;

import pl.telech.processor.annotation.enums.Type;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(AutoMethod.List.class)
public @interface AutoMethod {
	Type type();
	Argument[] args() default {};
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.SOURCE)
	@interface List {
	    AutoMethod[] value();
	}
}