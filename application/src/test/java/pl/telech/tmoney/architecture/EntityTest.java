package pl.telech.tmoney.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import pl.telech.tmoney.commons.model.entity.AbstractEntity;


@AnalyzeClasses(packages = "pl.telech.tmoney", importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class})
class EntityTest {

	private static final String packageName = "pl.telech.tmoney.*.model.entity";
	
	@ArchTest
    void check(JavaClasses allClasses) {
    	classes()
    		.that().resideInAPackage(packageName)
    		.and().areNotNestedClasses()
    		.and().doNotHaveModifier(JavaModifier.ABSTRACT)
			    .should().haveSimpleNameNotEndingWith("Data")
			    .andShould().haveSimpleNameNotEndingWith("Entity")
				.andShould().haveSimpleNameNotEndingWith("Model")
				.andShould().beAssignableTo(AbstractEntity.class)
				.andShould().beAnnotatedWith(Entity.class)
				.andShould(beAnnotatedWithTable())
				.andShould(beAnnotatedWithLombokData())
				.andShould(beAnnotatedWithLombokFieldNameConstants(allClasses))
				.andShould(haveCorrectFields())
		.check(allClasses);
    }
    
	@ArchTest
    void checkOthers(JavaClasses allClasses) {
		noClasses()
    		.that().resideOutsideOfPackage(packageName)
				.should().beAnnotatedWith(Entity.class)
		.check(allClasses);
	}
	
	private ArchCondition<JavaClass> beAnnotatedWithTable() {
		return new ArchCondition<>("be annotated with JPA @Table annotation") {
							
			@Override
			public void check(JavaClass clazz, ConditionEvents events) {
				Optional<Table> tableAnnotation = clazz.tryGetAnnotationOfType(Table.class);
				if (tableAnnotation.isEmpty()) {
					events.add(SimpleConditionEvent.violated(clazz, "Class " + clazz.getName() + " has no @Table annotation"));
				}
				else if (StringUtils.isNoneEmpty(tableAnnotation.get().name())) {
					events.add(SimpleConditionEvent.violated(clazz, "Class " + clazz.getName() + " has @Table annotation with 'name' attribute"));
				}
			}	
		};
	}

    private ArchCondition<JavaClass> beAnnotatedWithLombokData() {
		return new ArchCondition<>("be annotated with Lombok @Data annotation") {
						
			@Override
			public void check(JavaClass clazz, ConditionEvents events) {
				Optional<JavaMethod> toStringMethod = clazz.tryGetMethod("toString");
				Optional<JavaMethod> hashCodeMethod = clazz.tryGetMethod("hashCode");
				Optional<JavaMethod> equalsMethod = clazz.tryGetMethod("equals", Object.class);
				
				if (toStringMethod.isEmpty() || hashCodeMethod.isEmpty() || equalsMethod.isEmpty()) {
					events.add(SimpleConditionEvent.violated(clazz, "Class " + clazz.getFullName() + " has no @Data annotation"));
				}
			}	
		};
	}
    
    private ArchCondition<JavaClass> beAnnotatedWithLombokFieldNameConstants(JavaClasses allClasses) {
		return new ArchCondition<>("be annotated with Lombok @FieldNameConstants annotation") {		
			
			Set<JavaClass> classesWithFieldsNestedClass;
			
			public void init(Collection<JavaClass> classes) {
				classesWithFieldsNestedClass = allClasses.stream()
					.filter(clazz -> clazz.isNestedClass())
					.filter(clazz -> clazz.getSimpleName().equals("Fields"))
					.map(clazz -> clazz.getEnclosingClass().get())
					.collect(Collectors.toSet());	
			}
			
			@Override
			public void check(JavaClass clazz, ConditionEvents events) {		
				if (!classesWithFieldsNestedClass.contains(clazz)) {
					events.add(SimpleConditionEvent.violated(clazz, "Class " + clazz.getFullName() + " has no @FieldNameConstants annotation"));
				}
			}
		};
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
				Optional<Column> columnAnnotation = field.tryGetAnnotationOfType(Column.class);
				Optional<ManyToOne> manyToOneAnnotation = field.tryGetAnnotationOfType(ManyToOne.class);
				Optional<OneToMany> oneToManyAnnotation = field.tryGetAnnotationOfType(OneToMany.class);
				Optional<ManyToMany> manyToManyAnnotation = field.tryGetAnnotationOfType(ManyToMany.class);

				if (columnAnnotation.isEmpty() && manyToOneAnnotation.isEmpty() && oneToManyAnnotation.isEmpty() && manyToManyAnnotation.isEmpty()) {
					events.add(SimpleConditionEvent.violated(field, "Field " + field.getFullName() + " has no JPA annotations"));
				}	
			}
		};
	}
}
