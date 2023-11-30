package pl.telech.tmoney.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static pl.telech.tmoney.architecture.ArchUnitConditions.beAnnotatedWithGetter;
import static pl.telech.tmoney.architecture.ArchUnitConditions.beAnnotatedWithSetter;

import java.util.Optional;

import javax.validation.constraints.Size;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import pl.telech.tmoney.commons.model.dto.AbstractDto;


@AnalyzeClasses(packages = "pl.telech.tmoney", importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class})
class DtoTest {

	private static final String packageName = "pl.telech.tmoney.*.model.dto";
	
	@ArchTest
    void check(JavaClasses allClasses) {
    	classes()
    		.that().resideInAPackage(packageName)
    		.and().doNotHaveModifier(JavaModifier.ABSTRACT)
			    .should().haveSimpleNameEndingWith("Dto")
				.andShould().beAssignableTo(AbstractDto.class)
				.andShould(beAnnotatedWithGetter())
				.andShould(beAnnotatedWithSetter())
				.andShould(haveCorrectFields())
		.check(allClasses);
    }
    
	@ArchTest
    void checkOthers(JavaClasses allClasses) {
		noClasses()
    		.that().resideOutsideOfPackage(packageName)
				.should().haveSimpleNameEndingWith("Dto")
		.check(allClasses);
	}
    
    private ArchCondition<JavaClass> haveCorrectFields() {
		return new ArchCondition<>("have correct fields") {
						
			@Override
			public void check(JavaClass clazz, ConditionEvents events) {
				clazz.getFields().stream()
					.filter(field -> !field.getModifiers().contains(JavaModifier.STATIC))
					.forEach(field -> {
						checkJpaAnnotations(field, events);	
					});
			}	
			
			private void checkJpaAnnotations(JavaField field, ConditionEvents events) {
				if (field.getType().getName().equals(String.class.getName())) {
					Optional<Size> sizeAnnotation = field.tryGetAnnotationOfType(Size.class);	
					if (sizeAnnotation.isEmpty()) {
						events.add(SimpleConditionEvent.violated(field, "Field " + field.getFullName() + " has no @Size annotation"));
					}	
				}	
			}
		};
	}
}
