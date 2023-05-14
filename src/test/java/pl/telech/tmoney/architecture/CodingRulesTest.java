package pl.telech.tmoney.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.ProxyRules.no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchIgnore;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;

import pl.telech.tmoney.TMoneyApplication;


@AnalyzeClasses(packages = "pl.telech.tmoney", importOptions = ImportOption.DoNotIncludeTests.class)
class CodingRulesTest {

	@ArchTest
    ArchRule doNotUseSystemOutPrint = GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
	
	@ArchTest
    ArchRule doNotThrowGenericExceptions = GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS; // e.g. RuntimeException
	
	@ArchTest
	ArchRule doNotUseJavaLogging = GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    ArchRule doNotUseJodaTime = GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

    @ArchIgnore
    @ArchTest
    ArchRule doNotUseFieldInjection = GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
    
	@ArchTest
	ArchRule layerDependenciesMustBeRespected = 
		layeredArchitecture().consideringOnlyDependenciesInLayers()
			.layer("Controllers").definedBy("pl.telech.tmoney.*.controller..")
			.layer("Logic").definedBy("pl.telech.tmoney.*.logic..")
	        .layer("Dao").definedBy("pl.telech.tmoney.*.dao..")
	        .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
	        .whereLayer("Logic").mayOnlyBeAccessedByLayers("Controllers")
	        .whereLayer("Dao").mayOnlyBeAccessedByLayers("Logic");
	

    @ArchTest
    ArchRule no_bypass_of_proxy_logic_async =
            no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(Async.class);

	@ArchTest
	ArchRule no_bypass_of_proxy_logic_cache =
	        no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(Cacheable.class);
	
	@ArchTest
	ArchRule beansShouldHaveFinalfields = 
	    	fields()
	    		.that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
	    		.or().areDeclaredInClassesThat().areAnnotatedWith(Service.class) 
	    		.or().areDeclaredInClassesThat().areAnnotatedWith(Component.class) 
	    			.should().beFinal();
		
	@ArchTest
	ArchRule configurationShouldNotBePublic = 
	    	classes()
	    		.that().areAnnotatedWith(Configuration.class)
	    			.should().bePackagePrivate();
	
	@ArchTest
	ArchRule utilityClassShouldHavePrivateConstructor = 
	    	classes()
	    		.that().doNotHaveSimpleName("TMoneyApplication")
	    		.and(haveOnlyStaticMethods())
	    			.should().haveOnlyPrivateConstructors();
	
	@ArchTest
	ArchRule loggerShouldBePrivate = 
	    	fields()
	    	.that()
	    		.haveRawType(Logger.class).or().haveRawType(org.slf4j.Logger.class)
	    			.should().bePrivate()
	    			.andShould().beFinal()
	    			.andShould().beStatic();
	
	@ArchIgnore
	@ArchTest
	ArchRule codeShouldBeFreeOfCycles = 
			slices()
				.matching("pl.telech.tmoney.(*)..")
						.should().beFreeOfCycles();
		
	
	private DescribedPredicate<JavaClass> haveOnlyStaticMethods() {
		return new DescribedPredicate<>("with all method static") {
			
			@Override
			public boolean test(JavaClass clazz) {
				return !clazz.getMethods().isEmpty() && clazz.getMethods().stream()
						.filter(method -> !method.isConstructor())
						.filter(method -> !method.getName().equals("main"))
						.allMatch(method -> method.getModifiers().contains(JavaModifier.STATIC));
			}
			
		};
	}
}
