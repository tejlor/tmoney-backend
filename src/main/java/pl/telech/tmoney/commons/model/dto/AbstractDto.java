package pl.telech.tmoney.commons.model.dto;

import static lombok.AccessLevel.PROTECTED;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtilsBean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.model.entity.AbstractEntity;
import pl.telech.tmoney.commons.model.interfaces.Loggable;
import pl.telech.tmoney.commons.utils.TUtilsBean;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PROTECTED)
public abstract class AbstractDto implements Loggable {

	private static final BeanUtilsBean beanUtils = new TUtilsBean();
	
	Integer id; 
	
	public AbstractDto(AbstractEntity model) {
		try {
			beanUtils.copyProperties(this, model);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract AbstractEntity toModel();

	protected void fillModel(AbstractEntity model) {
		try {
			beanUtils.copyProperties(model, this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static <M extends AbstractEntity, T extends AbstractDto> List<T> toDtoList(Class<M> modelType, Class<T> dtoType, Collection<M> models) {
		if (models == null) {
			return new ArrayList<T>();
		}
		
		List<T> newList = new ArrayList<>();

		try {
			Constructor<T> cons = dtoType.getDeclaredConstructor(modelType);
			for (M model : models) {
				newList.add(cons.newInstance(model));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return newList;
	}
	
	protected static <M extends AbstractEntity, T extends AbstractDto> Set<T> toDtoSet(Class<M> modelType, Class<T> dtoType, Collection<M> models) {
		if (models == null) {
			return new HashSet<T>();
		}
		
		Set<T> newSet = new HashSet<>();

		try {
			Constructor<T> cons = dtoType.getDeclaredConstructor(modelType);
			for (M model : models) {
				newSet.add(cons.newInstance(model));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return newSet;
	}

	@SuppressWarnings("unchecked")
	public static <M extends AbstractEntity, T extends AbstractDto> List<M> toModelList(Collection<T> dtos) {	
		if(dtos == null) {
			return new ArrayList<M>();
		}
		
		return dtos.stream()
				.map(dto -> (M) dto.toModel())
				.collect(Collectors.toList());
	}
	
	@SuppressWarnings("unchecked")
	public static <M extends AbstractEntity, T extends AbstractDto> Set<M> toModelSet(Collection<T> dtos) {
		if(dtos == null) {
			return new HashSet<M>();
		}
		
		return dtos.stream()
				.map(dto -> (M) dto.toModel())
				.collect(Collectors.toSet());
	}
}
