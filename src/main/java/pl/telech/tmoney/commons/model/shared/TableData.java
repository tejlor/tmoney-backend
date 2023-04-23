package pl.telech.tmoney.commons.model.shared;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.telech.tmoney.commons.model.interfaces.Loggable;

/*
 * 
 */
@Getter @Setter
@NoArgsConstructor
public class TableData<T> implements Loggable {

	TableParams tableParams;
	TableInfo tableInfo;
	List<T> rows;
	
	public TableData(TableParams tableParams) {
		this.tableParams = tableParams;
		tableInfo = new TableInfo();
	}
	
	public void setCount(int count) {
		tableInfo.setCount(tableParams, count);
	}
	
	@Getter @Setter
	public static class TableInfo implements Loggable {
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


