package pl.telech.tmoney.utils;

import static lombok.AccessLevel.PROTECTED;
import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;

import org.assertj.core.api.Fail;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.adm.builder.UserBuilder;
import pl.telech.tmoney.adm.model.entity.User;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

@SpringBootTest 
@ActiveProfiles("junit")
@Import(TMoneyTestConfiguration.class)
@FieldDefaults(level = PROTECTED)
public class BaseTest {

	User defaultUser;
	BeanFieldSetter beanFieldSetter;
	
	@Autowired
	EntityManager entityManager;
	
	public BaseTest() {
		beanFieldSetter = new BeanFieldSetter();
	}
	
    @Before
    public void before() {
    	// restart autoincrement values
        entityManager.createNativeQuery("TRUNCATE TABLE adm.User RESTART IDENTITY AND COMMIT NO CHECK").executeUpdate();
        // create required user references in createdBy fields
        defaultUser = new UserBuilder().save(entityManager);
    }
	
	@After
	public void after() {
		beanFieldSetter.restoreAllOriginValues();
	}
	
	protected <T extends AbstractEntity> T load(Class<T> clazz, int id)  {
		return entityManager.find(clazz, id);
	}
	
	protected <T extends AbstractEntity> void reload(T entity)  {
		entityManager.refresh(entity);
	}
	
	protected <T extends AbstractEntity> void flush() {
		entityManager.flush();
	}
	
	protected <T extends AbstractEntity> void flushAndClear() {
		entityManager.flush();
		entityManager.clear();
	}
	
	protected void setBeanField(Object targetObject, String name, Object value) {
		beanFieldSetter.setObjectField(targetObject, name, value);
	}

	protected static <T extends Exception> void expectException(Runnable action, Class<T> exceptionClass, String exceptionMessage) {
		try {
			action.run();
			Fail.failBecauseExceptionWasNotThrown(exceptionClass);
		} 
		catch (Exception e) {
			assertThat(e).isInstanceOf(exceptionClass);
			assertThat(e.getMessage()).contains(exceptionMessage);
		}
	}
}
