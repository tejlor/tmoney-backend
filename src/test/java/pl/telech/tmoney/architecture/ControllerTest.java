package pl.telech.tmoney.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static pl.telech.tmoney.architecture.ArchUnitConditions.beAnnotatedWithRequiredArgsConstructor;

import java.util.Arrays;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import pl.telech.tmoney.commons.controller.domain.AbstractDomainController;


@AnalyzeClasses(packages = "pl.telech.tmoney", importOptions = ImportOption.DoNotIncludeTests.class)
class ControllerTest {

	private static final String packageName = "pl.telech.tmoney.*.controller.domain";
	
	@ArchTest
    void check(JavaClasses allClasses) {
    	classes()
    		.that().resideInAPackage(packageName)
    		.and().doNotHaveModifier(JavaModifier.ABSTRACT)
    		.and().areNotInnerClasses()
			    .should().haveSimpleNameEndingWith("Controller")
				.andShould().beAssignableTo(AbstractDomainController.class)
				.andShould().beAnnotatedWith(RestController.class)
				.andShould(beAnnotatedWithRequestMapping())
				.andShould(beAnnotatedWithRequiredArgsConstructor())
				.andShould(haveCorrectFields())
				.andShould(haveCorrectMethods())
		.check(allClasses);
    }
    
	@ArchTest
    void checkOthers(JavaClasses allClasses) {
		noClasses()
    		.that().resideOutsideOfPackage(packageName)
				.should().beAssignableTo(AbstractDomainController.class)
		.check(allClasses);
	}
	
	private static ArchCondition<JavaClass> beAnnotatedWithRequestMapping() {
		return new ArchCondition<>("be annotated with @RequestMapping") {
						
			@Override
			public void check(JavaClass clazz, ConditionEvents events) {
				clazz.tryGetAnnotationOfType(RequestMapping.class)
					.ifPresentOrElse(annotation -> {
						String[] paths = annotation.value();
						boolean allPathsValid = Arrays.stream(paths).allMatch(this::isPathValid);
						if (!allPathsValid) {
							events.add(SimpleConditionEvent.violated(clazz, "Class " + clazz.getFullName() + " has @RequestMapping with invalid path"));
						}
					}, 
					() -> {
						events.add(SimpleConditionEvent.violated(clazz, "Class " + clazz.getFullName() + " hasn't @RequestMapping annotation"));
					});
				
			}
			
			private boolean isPathValid(String path) {
				return path.startsWith("/") && path.endsWith("s");
			}
		};
	}
    
    private ArchCondition<JavaClass> haveCorrectFields() {
		return new ArchCondition<>("have correct fields") {
						
			@Override
			public void check(JavaClass clazz, ConditionEvents events) {				
				clazz.getFields()
					.forEach(field -> {
						shouldBeFinal(field, events);	
					});
			}	
			
			private void shouldBeFinal(JavaField field, ConditionEvents events) {
				if (!field.getModifiers().contains(JavaModifier.FINAL)) {
					events.add(SimpleConditionEvent.violated(field, "Field " + field.getFullName() + " is not final"));	
				}	
			}
		};
	}
    
    private ArchCondition<JavaClass> haveCorrectMethods() {
		return new ArchCondition<>("have correct methods") {
						
			@Override
			public void check(JavaClass clazz, ConditionEvents events) {
				clazz.getMethods().stream()
					.filter(method -> !method.getName().contains("lambda"))
					.forEach(method -> {
						shouldBePublic(method, events);	
						shouldHaveRequestMappingAnnotation(method, events);	
					});
			}	
			
			private void shouldBePublic(JavaMethod method, ConditionEvents events) {
				if (!method.getModifiers().contains(JavaModifier.PUBLIC)) {
					events.add(SimpleConditionEvent.violated(method, "Method " + method.getFullName() + " is not public"));	
				}	
			}
			
			private void shouldHaveRequestMappingAnnotation(JavaMethod method, ConditionEvents events) {
				method.tryGetAnnotationOfType(RequestMapping.class)
					.ifPresentOrElse(annotation -> {
						String[] paths = annotation.value();
						boolean allPathsValid = Arrays.stream(paths).allMatch(this::isPathValid);
						if (!allPathsValid) {
							events.add(SimpleConditionEvent.violated(method, "Method " + method.getFullName() + " has @RequestMapping with invalid path"));
						}
						if (annotation.method().length == 0) {
							events.add(SimpleConditionEvent.violated(method, "Method " + method.getFullName() + " has @RequestMapping with no method argument"));
						}
					}, 
					() -> 
						events.add(SimpleConditionEvent.violated(method, "Method " + method.getFullName() + " has not @RequestMapping annotation"))
				);
			}
			
			private boolean isPathValid(String path) {
				return path.equals("") || path.startsWith("/");
			}
		};
	}
}
