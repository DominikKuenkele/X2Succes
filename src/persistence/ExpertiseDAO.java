/**
 * 
 */
package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class is a DAO for the table 'expertise'
 * 
 * @author domin
 *
 */
public class ExpertiseDAO {
	private final String TABLE = "expertise";

	/**
	 * @param eid
	 * @return the name of the expertise with given ID
	 * @throws SQLException
	 */
	public String getExpertise(int eid) throws SQLException {
		SelectSql statement = new SelectSql();

		String column = "expertise";
		String condition = "EID=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(eid);

		statement.select(column).from(TABLE).where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		return getExpertiseFromResultSet(result).get(0);
	}

	/**
	 * @param expertise
	 * @return the id of the expertise with given name
	 * @throws SQLException
	 */
	public int getExpertise(String expertise) throws SQLException {
		int eid = -1;

		SelectSql statement = new SelectSql();

		String column = "EID";
		String condition = "expertise=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(expertise);

		statement.select(column).from(TABLE).where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		while (result.next()) {
			eid = result.getInt(column);
		}
		return eid;
	}

	/**
	 * @return a List of all expertises in the database
	 * @throws SQLException
	 */
	public List<String> getAllExpertises() throws SQLException {
		SelectSql statement = new SelectSql();

		String column = "expertise";
		
		statement.select(column).from(TABLE);
		ResultSet result = statement.executeQuery();

		return getExpertiseFromResultSet(result);
	}

	/**
	 * @param resultSet
	 * @return the resultset as list of Strings
	 * @throws SQLException
	 */
	private List<String> getExpertiseFromResultSet(ResultSet resultSet) throws SQLException {
		List<String> result = new LinkedList<>();
		// for each returned row from database
		while (resultSet.next()) {
			// get the value
			String expertise = resultSet.getString("expertise");
			result.add(expertise);
		}
		return result;
	}
}
