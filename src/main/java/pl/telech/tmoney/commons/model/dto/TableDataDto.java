package pl.telech.tmoney.commons.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.model.interfaces.Loggable;
import pl.telech.tmoney.commons.model.shared.TableParams;

/*
 * 
 */
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class TableDataDto<T> implements Loggable {

	TableParams tableParams;
	TableInfoDto tableInfo;
	List<T> rows;
	
	public TableDataDto(TableParams tableParams) {
		this.tableParams = tableParams;
		tableInfo = new TableInfoDto();
	}
	
	public void setCount(int count) {
		tableInfo.setCount(tableParams, count);
	}
	
	@Getter @Setter
	@FieldDefaults(level = PRIVATE)
	public static class TableInfoDto implements Loggable {
		int pageCount;
		int rowCount;
		int rowStart;
		int rowEnd;
		
		public void setCount(TableParams tableParams, int count) {
			rowCount = count;
			pageCount = (int) Math.ceil((double) count / tableParams.getPageSize());
			rowStart = Math.min(tableParams.getPageNo() * tableParams.getPageSize() + 1, rowCount);
			rowEnd = Math.min(rowStart + tableParams.getPageSize() - 1, rowCount);
		}
	}
}


