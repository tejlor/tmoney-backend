package pl.telech.tmoney.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

import pl.telech.tmoney.commons.dao.DAO;


@AnalyzeClasses(packages = "pl.telech.tmoney", importOptions = ImportOption.DoNotIncludeTests.class)
class DaoTest {

	private static final String packageName = "pl.telech.tmoney.*.dao";
	
	@ArchTest
    void check(JavaClasses allClasses) {
    	classes()
    		.that().resideInAPackage(packageName)
    		.and().areInterfaces()
    		.and().areNotAnnotatedWith(NoRepositoryBean.class)
			    .should().haveSimpleNameEndingWith("DAO")
				.andShould().beAssignableTo(DAO.class)
				.andShould().beAssignableTo(JpaSpecificationExecutor.class)
		.check(allClasses);
    }
    
	@ArchTest
    void checkOthers(JavaClasses allClasses) {
		noClasses()
    		.that().resideOutsideOfPackage(packageName)
				.should().haveSimpleNameEndingWith("DAO")
		.check(allClasses);
	}

}
