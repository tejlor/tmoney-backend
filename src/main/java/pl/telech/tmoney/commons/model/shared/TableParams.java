package pl.telech.tmoney.commons.model.shared;

import static lombok.AccessLevel.PRIVATE;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.model.interfaces.Loggable;
import pl.telech.tmoney.commons.utils.TUtils;

@Getter @Setter
@FieldDefaults(level = PRIVATE)
public class TableParams implements Loggable {

	int pageNo;
	int pageSize;
	String filter;
	String sortBy;
	
	public TableParams() {
		pageNo = 0;
		pageSize = 10;
		sortBy = "name ASC";
	}
	
	public TableParams(Integer pageNo, Integer pageSize, String filter, String sortBy) {
		this();

		if(pageNo != null)
			this.pageNo = pageNo;
		
		if(pageSize != null)
			this.pageSize = pageSize;
		
		if(!TUtils.isEmpty(filter))
			this.filter = filter.toLowerCase();
		
		if(!TUtils.isEmpty(sortBy))
			this.sortBy = sortBy;
		
	}
	
	@JsonIgnore
	public Sort getSort() {	
		return Sort.by(
			Arrays.stream(sortBy.split(","))
				.map(col -> {
					String[] arr = col.trim().split(" ");
					String column = arr[0];
					Direction direction = arr.length > 1 ? Direction.fromString(arr[1]) : Direction.ASC;
					return new Order(direction, column);
				})
				.collect(Collectors.toList())
		);
	}
	
	@JsonIgnore
	public Pageable getPage() {
		return PageRequest.of(pageNo, pageSize, getSort());
	}
}
