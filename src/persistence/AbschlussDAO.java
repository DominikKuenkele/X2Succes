/**
 * 
 */
package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class is a DAO for the table 'graduation'
 * 
 * @author domin
 *
 */
public class AbschlussDAO {
	private final String TABLE = "graduation";

	/**
	 * @param gid
	 * @return the name of the graduation with given ID
	 * @throws SQLException
	 */
	public String getAbschluss(int gid) throws SQLException {
		Sql statement = new Sql();

		String column = "graduation";
		String condition = "GID=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(gid);

		statement.select(column).from(TABLE).where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		return getAbschlussFromResultSet(result).get(0);
	}

	/**
	 * @param abschluss
	 * @return the id of the graduation with given name
	 * @throws SQLException
	 */
	public int getAbschluss(String abschluss) throws SQLException {
		int gid = -1;

		Sql statement = new Sql();

		String column = "GID";
		String condition = "graduation=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(abschluss);

		statement.select(column).from(TABLE).where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		while (result.next()) {
			gid = result.getInt(column);
		}
		return gid;
	}

	/**
	 * @return a List of all graduations in the database
	 * @throws SQLException
	 */
	public List<String> getAllAbschluss() throws SQLException {
		Sql statement = new Sql();

		String column = "graduation";
		String orderBy = "hierarchy";
		
		statement.select(column).from(TABLE).orderBy(orderBy);
		ResultSet result = statement.executeQuery();

		return getAbschlussFromResultSet(result);
	}

	/**
	 * @param resultSet
	 * @return the resultset as list of Strings
	 * @throws SQLException
	 */
	private List<String> getAbschlussFromResultSet(ResultSet resultSet) throws SQLException {
		List<String> result = new LinkedList<>();
		// for each returned row from database
		while (resultSet.next()) {
			// get the value
			String abschluss = resultSet.getString("graduation");
			result.add(abschluss);
		}
		return result;
	}

	/**
	 * @param abschluss
	 * @return the hierarchy of a given graduation
	 * @throws SQLException
	 */
	public int getHierarchy(String abschluss) throws SQLException {
		int hier = -1;

		Sql statement = new Sql();

		String column = "hierarchy";
		String condition = "graduation=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(abschluss);

		statement.select(column).from(TABLE).where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		while (result.next()) {
			hier = result.getInt(column);
		}
		return hier;
	}
}
