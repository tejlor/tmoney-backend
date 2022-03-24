package pl.telech.tmoney.commons.dao.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/*
 * Implementation of old and good ImprovedNamingStrategy.
 */
public class TNamingStrategy extends PhysicalNamingStrategyStandardImpl {

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		return new Identifier(addUnderscores(name.getText()), name.isQuoted());
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
		return new Identifier(addUnderscores(name.getText()), name.isQuoted());
	}

	private static String addUnderscores(String name) {
		StringBuilder sb = new StringBuilder(name.replace('.', '_'));
		for (int i = 1; i < sb.length() - 1; i++) {
			if (Character.isLowerCase(sb.charAt(i - 1)) && Character.isUpperCase(sb.charAt(i)) && Character.isLowerCase(sb.charAt(i + 1))) {
				sb.insert(i++, '_');
			}
		}
		return sb.toString().toLowerCase();
	}
}
