package pl.telech.tmoney.commons.model.shared;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

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
	boolean sortAsc;
	
	public TableParams() {
		pageNo = 0;
		pageSize = 10;
		sortBy = "name";
		sortAsc = true;
	}
	
	public TableParams(Integer pageNo, Integer pageSize, String filter, String sortBy, Boolean sortAsc) {
		this();

		if(pageNo != null)
			this.pageNo = pageNo;
		
		if(pageSize != null)
			this.pageSize = pageSize;
		
		if(!TUtils.isEmpty(filter))
			this.filter = filter.toLowerCase();
		
		if(sortBy != null && sortBy.length() > 0)
			this.sortBy = sortBy;
		
		if(sortAsc != null)
			this.sortAsc = sortAsc;	
	}
	
	@JsonIgnore
	public Sort getSort() {
		return Sort.by(sortAsc ? Direction.ASC : Direction.DESC, sortBy);
	}
	
	@JsonIgnore
	public Pageable getPage() {
		return PageRequest.of(pageNo, pageSize, getSort());
	}
}
