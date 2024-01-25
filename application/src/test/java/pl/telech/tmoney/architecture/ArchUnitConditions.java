package pl.telech.tmoney.architecture;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import lombok.experimental.UtilityClass;
import pl.telech.tmoney.commons.utils.TCollectionUtils;
import pl.telech.tmoney.commons.utils.TStreamUtils;
import pl.telech.tmoney.commons.utils.TStringUtils;

@UtilityClass
public class ArchUnitConditions {

	public static ArchCondition<JavaClass> beAnnotatedWithRequiredArgsConstructor() {
		
		return new ArchCondition<>("be annotated with @RequiredArgsConstructor") {
						
			@Override
			public void check(JavaClass clazz, ConditionEvents events) {
				Set<JavaClass> fieldTypes = clazz.getFields().stream()
					.filter(this::isFinalNonStatic)
					.map(field -> field.getRawType())
					.collect(Collectors.toSet());
				
				Set<JavaConstructor> constructors = clazz.getConstructors();
				if (constructors.size() != 1) {
					events.add(SimpleConditionEvent.violated(clazz, "Class " + clazz.getFullName() + " has many constructors"));
				}
				else {
					JavaConstructor constructor = TCollectionUtils.first(constructors);
					Set<JavaClass> paramsTypes = TStreamUtils.mapToSet(constructor.getParameters(), param -> param.getRawType());
					fieldTypes.removeAll(paramsTypes);
					if (!fieldTypes.isEmpty()) {
						events.add(SimpleConditionEvent.violated(clazz, "Class " + clazz.getFullName() + " hasn't @RequiredArgsConstructor annotation"));
					}
				}
			}
			
			private boolean isFinalNonStatic(JavaField field) {
				Set<JavaModifier> modifiers = field.getModifiers();
				return modifiers.contains(JavaModifier.FINAL) && !modifiers.contains(JavaModifier.STATIC);
			}
		};
	}
	
    public static ArchCondition<JavaClass> beAnnotatedWithGetter() {
		return new ArchCondition<>("be annotated with @Getter") {
						
			@Override
			public void check(JavaClass clazz, ConditionEvents events) {
				clazz.getFields().stream()
					.filter(field -> !field.getModifiers().contains(JavaModifier.STATIC))
					.forEach(field -> {
						Optional<JavaMethod> getter = clazz.tryGetMethod(calculateGetterName(field));
						if (getter.isEmpty()) {
							events.add(SimpleConditionEvent.violated(field, "Field " + field.getFullName() + " hasn't @Getter annotation"));
						}
					});			
			}	
			
		    private String calculateGetterName(JavaField field) {
		    	if (field.getType().getName() == "boolean") {
		    		return "is" + TStringUtils.toCamelCase(field.getName());
		    	}
		    	else {
		    		return "get" + TStringUtils.toCamelCase(field.getName());
		    	}
		    }
		};
	}

    public static ArchCondition<JavaClass> beAnnotatedWithSetter() {
		return new ArchCondition<>("be annotated with @Setter") {
						
			@Override
			public void check(JavaClass clazz, ConditionEvents events) {
				clazz.getFields().stream()
					.filter(field -> !field.getModifiers().contains(JavaModifier.STATIC))
					.forEach(field -> {
						Optional<JavaMethod> setter = clazz.tryGetMethod(calculateSetterName(field), field.getRawType().reflect());
						if (setter.isEmpty()) {
							events.add(SimpleConditionEvent.violated(field, "Field " + field.getFullName() + " hasn't @Setter annotation"));
						}
					});			
			}	
			
		    private String calculateSetterName(JavaField field) {
		    	return "set" + TStringUtils.toCamelCase(field.getName());
		    }
		};
	}

}
