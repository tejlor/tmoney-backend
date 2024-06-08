package pl.telech.tmoney.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import pl.telech.tmoney.commons.mapper.EntityMapper;
import pl.telech.tmoney.commons.mapper.EntityMapperConfig;


@AnalyzeClasses(packages = "pl.telech.tmoney", importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class})
class MapperTest {

	private static final String packageName = "pl.telech.tmoney.*.mapper";
	
	@ArchTest
    void check(JavaClasses allClasses) {
    	classes()
    		.that().resideInAPackage(packageName)
    		.and().haveSimpleNameNotContaining("Entity")
    		.and().areInterfaces()
			    .should().haveSimpleNameEndingWith("Mapper")
				.andShould().beAssignableTo(EntityMapper.class)
				.andShould(beAnnotatedWithMapper())
				.andShould(haveCorrectMethods())
		.check(allClasses);
    }
	
	@ArchTest
    void checkOthers(JavaClasses allClasses) {
		noClasses()
    		.that().resideOutsideOfPackage(packageName)
				.should().haveSimpleNameEndingWith("Mapper")
		.check(allClasses);
	}
	
	private static ArchCondition<JavaClass> beAnnotatedWithMapper() {
		return new ArchCondition<>("be annotated with @Mapper") {
						
			@Override
			public void check(JavaClass clazz, ConditionEvents events) {
				clazz.tryGetAnnotationOfType(Mapper.class).ifPresentOrElse(annotation -> {
					if (!Objects.equals(annotation.config(), EntityMapperConfig.class)) {
						events.add(SimpleConditionEvent.violated(clazz, "Interface " + clazz.getFullName() + " has @Mapper without config defined"));
					}
				}, 
				() -> {
					events.add(SimpleConditionEvent.violated(clazz, "Interface " + clazz.getFullName() + " hasn't @Mapper"));
				});	
			}	
		};
	}
    

    private ArchCondition<JavaClass> haveCorrectMethods() {
		return new ArchCondition<>("have correct methods") {
						
			private final Pattern pEntity = Pattern.compile("^(.+).mapper.(.+)Mapper");
			
			@Override
			public void check(JavaClass clazz, ConditionEvents events) {		
				String className = clazz.getFullName();	
				Matcher m = pEntity.matcher(className);
				m.find();
				String entityName = String.format("%s.model.entity.%s", m.group(1), m.group(2));
				String dtoName = String.format("%s.model.dto.%sDto", m.group(1), m.group(2));	
				
				Set<String> requiredMethods = calculateRequiredMethods(className, entityName, dtoName);			
				Set<String> classMethods = clazz.getMethods().stream()
						.map(method-> method.getReturnType().toErasure().getFullName() + " " + method.getFullName())
						.collect(Collectors.toSet());
				
				for (String methodName : requiredMethods) {
					if (!classMethods.contains(methodName)) {
						events.add(SimpleConditionEvent.violated(clazz, "Interface " + clazz.getFullName() + " hasn't required method " + methodName));
					}
				}
			}	
			
			private Set<String> calculateRequiredMethods(String className, String entityName, String dtoName) {
				return Set.of(
						String.format("%s %s.toDto(%s)", dtoName, className, entityName),
						String.format("%s %s.toDtoList(%s)", "java.util.List", className, "java.util.Collection"),
						String.format("%s %s.toEntity(%s)", entityName, className, dtoName),
						String.format("%s %s.create(%s)", entityName, className, dtoName),
						String.format("%s %s.update(%s, %s)", entityName, className, entityName, dtoName)
				);
			}
		};
	}
}
