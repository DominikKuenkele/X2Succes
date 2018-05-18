package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class is a DAO for the table 'sprache'
 * 
 * @author domin
 *
 */
public class SpracheDAO {
	private final String TABLE = "sprachen";

	/**
	 * @param sprache
	 * @return the Id with given language
	 * @throws SQLException
	 */
	public int getSID(String sprache) throws SQLException {
		int sid = -1;

		Sql statement = new Sql();

		String column = "SID";
		String condition = "sprache=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(sprache);

		statement.select(column).from(TABLE).where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		while (result.next()) {
			sid = result.getInt(column);
		}
		return sid;
	}

	/**
	 * @param sid
	 * @return the language with given id
	 * @throws SQLException
	 */
	public String getSprache(int sid) throws SQLException {
		Sql statement = new Sql();

		String columns = "*";
		String condition = "SID=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(sid);

		statement.select(columns).from(TABLE).where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		return getSpracheFromResultSet(result).get(0);
	}

	/**
	 * @return a list of all languages in database
	 * @throws SQLException
	 */
	public List<String> getAllSprachen() throws SQLException {
		Sql statement = new Sql();

		String column = "*";
		
		statement.select(column).from(TABLE);
		ResultSet result = statement.executeQuery();

		return getSpracheFromResultSet(result);
	}

	/**
	 * @param resultSet
	 * @return the resultset as list of Strings
	 * @throws SQLException
	 */
	private List<String> getSpracheFromResultSet(ResultSet resultSet) throws SQLException {
		List<String> result = new LinkedList<>();
		// for each returned row from database
		while (resultSet.next()) {
			// get the value
			String sprache = resultSet.getString("Sprache");
			result.add(sprache);
		}
		return result;
	}
}
