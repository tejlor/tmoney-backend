package pl.telech.tmoney.commons.model.interfaces;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.telech.tmoney.commons.utils.TConstants;

/*
 * Interface for dto objects which contents should be logged.
 */
public interface Loggable {
	
	public default String toFullString(int depth) {
		if (depth == 3) {
			return TConstants.INDENT[depth] + "...\n";
		}
		StringBuilder sb = new StringBuilder();
		List<Field> fields = new ArrayList<Field>();
		
		if(this.getClass().getSuperclass() != null) {
			fields.addAll(Arrays.asList(this.getClass().getSuperclass().getDeclaredFields()));  // add from super class
		}
		
		fields.addAll(Arrays.asList(this.getClass().getDeclaredFields()));						// add from current class
		
		for (Field field : fields) {
			try {
				if(Modifier.isStatic(field.getModifiers()))
					continue;
				
				field.setAccessible(true);
				Object obj = field.get(this);
				sb.append(TConstants.INDENT[depth] + field.getName() + ": ");
				if (obj instanceof Loggable) {
					sb.append("\n");
					sb.append(((Loggable) obj).toFullString(depth + 1));
				}
				else if(obj instanceof List) {
					List<?> list = (List<?>) obj;
					if(list.size() > 0){
						sb.append("list of ").append(list.size()).append(" elements, first:\n");
						Object firstEl = list.get(0);
						if(firstEl instanceof Loggable){
							sb.append(((Loggable) firstEl).toFullString(1));
						}
						else {
							sb.append(firstEl.toString());
						}
					}
					else {
						sb.append("Empty List");
					}
				}
				else {
					sb.append(toString(obj) + "\n");
				}
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				// nothing
			}
		}

		if(depth == 0) {
			sb.setLength(sb.length() - 1);
		}
		
		return sb.toString();
	}
	
	private String toString(Object object) {
		if(object == null) {
			return "";
		}
		
		String str = object.toString();
		if(str.length() > 100) {
			return str.substring(0, 100) + "...";
		}
		
		return str;
	}
}
