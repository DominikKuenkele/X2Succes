/**
 * 
 */
package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class is a DAO for the table 'sex'
 * 
 * @author domin
 *
 */
public class SexDAO {
	private final String TABLE = "sex";

	/**
	 * @param sexId
	 * @return the name of the sex with given ID
	 * @throws SQLException
	 */
	public String getSex(int sexId) throws SQLException {
		Sql statement = new Sql();

		String columns = "sex";
		String condition = "sexID=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(sexId);

		statement.select(columns).from(TABLE).where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		return getSexFromResultSet(result).get(0);
	}

	/**
	 * @param sex
	 * @return the id of the sex with given name
	 * @throws SQLException
	 */
	public int getSex(String sex) throws SQLException {
		int sexId = -1;

		Sql statement = new Sql();

		String column = "sexID";
		String condition = "sex=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(sex);

		statement.select(column).from(TABLE).where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		while (result.next()) {
			sexId = result.getInt(column);
		}
		return sexId;
	}

	/**
	 * @return a List of all sex in the database
	 * @throws SQLException
	 */
	public List<String> getAllSex() throws SQLException {
		Sql statement = new Sql();

		String column = "sex";
		
		statement.select(column).from(TABLE);
		ResultSet result = statement.executeQuery();

		return getSexFromResultSet(result);
	}

	/**
	 * @param resultSet
	 * @return the resultset as list of Strings
	 * @throws SQLException
	 */
	private List<String> getSexFromResultSet(ResultSet resultSet) throws SQLException {
		List<String> result = new LinkedList<>();
		// for each returned row from database
		while (resultSet.next()) {
			// get the value
			String sex = resultSet.getString("sex");
			result.add(sex);
		}
		return result;
	}
}
