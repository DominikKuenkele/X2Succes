/**
 * 
 */
package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class is a DAO for the table 'branche'
 * 
 * @author domin
 *
 */
public class BrancheDAO {
	private final String TABLE = "branche";

	/**
	 * @param bid
	 * @return the name of the branche with given ID
	 * @throws SQLException
	 */
	public String getBranche(int bid) throws SQLException {
		SelectSql statement = new SelectSql();

		String column = "branche";
		String condition = "BID=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(bid);

		statement.select(column).from(TABLE).where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		return getBrancheFromResultSet(result).get(0);
	}

	/**
	 * @param branche
	 * @return the id of the branche with given name
	 * @throws SQLException
	 */
	public int getBID(String branche) throws SQLException {
		int bid = -1;

		SelectSql statement = new SelectSql();

		String column = "BID";
		String condition = "branche=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(branche);

		statement.select(column).from(TABLE).where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		while (result.next()) {
			bid = result.getInt(column);
		}
		return bid;
	}

	/**
	 * @return a List of all branchen in the database
	 * @throws SQLException
	 */
	public List<String> getAllBranchen() throws SQLException {
		SelectSql statement = new SelectSql();

		String column = "branche";
		
		statement.select(column).from(TABLE);
		ResultSet result = statement.executeQuery();

		return getBrancheFromResultSet(result);
	}

	/**
	 * @param resultSet
	 * @return the resultset as list of Strings
	 * @throws SQLException
	 */
	private List<String> getBrancheFromResultSet(ResultSet resultSet) throws SQLException {
		List<String> result = new LinkedList<>();
		// for each returned row from database
		while (resultSet.next()) {
			// get the value
			String branche = resultSet.getString("branche");
			result.add(branche);
		}
		return result;
	}
}
