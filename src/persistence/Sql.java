package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import util.StringConverter;

public class Sql {
	private static DataSource datasource = DataSource.getInstance();

	private String select = "";
	private String from = "";
	private String where = "";
	private String orderBy = "";
	private String[] innerJoins = {};
	private List<Object> conditionWildcards = null;

	public Sql select(String... columns) {
		this.select = StringConverter.arrayToString(columns);
		return this;
	}

	public Sql from(String... tables) {
		this.from = StringConverter.arrayToString(tables);
		return this;
	}

	public Sql where(List<Object> conditionWildcards, String... conditions) {
		this.where = StringConverter.arrayToString(conditions, "AND");
		this.conditionWildcards = conditionWildcards;
		return this;
	}

	public Sql orderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}
	
	public Sql innerJoin(String table, String criteria) {
		this.innerJoins[innerJoins.length] = table + " ON " + criteria;
		return this;
	}

	private String buildSql() {
		StringBuffer sql = new StringBuffer();

		if (!select.isEmpty() && select != null) {
			sql.append("SELECT ");
			sql.append(select);
		}
		if (!from.isEmpty() && from != null) {
			sql.append(" FROM ");
			sql.append(from);
		}
		for(String innerJoin : innerJoins) {
			sql.append(" INNER JOIN ");
			sql.append(innerJoin);
		}
		if (!where.isEmpty() && where != null) {
			sql.append(" WHERE ");
			sql.append(where);
		}
		if (!orderBy.isEmpty() && orderBy != null) {
			sql.append(" ORDER BY ");
			sql.append(orderBy);
		}

		return sql.toString();
	}

	public ResultSet executeQuery() throws SQLException {
		String sql = buildSql();
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			// set WHERE in statement
			for (int counter = 0; counter < conditionWildcards.size(); counter++) {
				preparedStatement.setObject(counter, conditionWildcards.get(counter));
			}
			return preparedStatement.executeQuery();
		}
	}
}
