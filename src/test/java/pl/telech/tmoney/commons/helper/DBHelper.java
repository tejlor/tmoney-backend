package pl.telech.tmoney.commons.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;

@Component
@RequiredArgsConstructor
public class DBHelper {
	
	final EntityManager entityManager;
	
	
	public <T extends AbstractEntity> T load(Class<T> clazz, int id) {
		EntityGraph<?> graph = null;
		try {
			graph = entityManager.getEntityGraph(clazz.getSimpleName() + ".all");
		}
		catch(IllegalArgumentException e) {
			System.out.println("WARN: no entity graph for " + clazz.getSimpleName());
		}
		
		Map<String, Object> props = new HashMap<>(); 
		props.put(EntityGraphType.FETCH.getKey(), graph);
		return entityManager.find(clazz, id, props);
	}
	
	public <T extends AbstractEntity> List<T> loadAll(Class<T> clazz) {
		return entityManager.createQuery("SELECT x FROM " + clazz.getSimpleName() + " x", clazz)
				.getResultList().stream().sorted().collect(Collectors.toList());
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
