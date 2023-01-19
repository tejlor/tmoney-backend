package pl.telech.tmoney.commons.helper;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

@Component
@RequiredArgsConstructor
public class DBHelper {
	
	final EntityManager entityManager;
	
	
	public <T extends AbstractEntity> T load(Class<T> clazz, int id) {
		return entityManager.find(clazz, id);
	}
	
	public <T extends AbstractEntity> void reload(T entity) {
		entityManager.refresh(entity);
	}
	
	@Transactional
	public void truncateDb(List<String> tables) {
		entityManager.createNativeQuery("SET DATABASE REFERENTIAL INTEGRITY FALSE").executeUpdate();
		tables.forEach(table -> entityManager.createNativeQuery("DELETE FROM " + table).executeUpdate());
		entityManager.createNativeQuery("SET DATABASE REFERENTIAL INTEGRITY TRUE").executeUpdate();
	}
}
