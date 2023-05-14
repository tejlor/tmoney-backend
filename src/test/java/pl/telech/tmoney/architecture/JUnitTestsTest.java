package pl.telech.tmoney.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;


@AnalyzeClasses(packages = "pl.telech.tmoney", importOptions = ImportOption.OnlyIncludeTests.class)
class JUnitTestsTest {
	
	@ArchTest
	ArchRule testsShouldNotBePublic = 
		methods()
			.that().areDeclaredInClassesThat().doNotHaveModifier(JavaModifier.ABSTRACT)
			.and().areAnnotatedWith(Test.class)
			.or().areAnnotatedWith(ParameterizedTest.class)
				.should().beDeclaredInClassesThat().arePackagePrivate()
				.andShould().bePackagePrivate();  

}
