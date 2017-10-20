/**
 * 
 */
package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
	private DataSource datasource = DataSource.getInstance();

	/**
	 * @param bid
	 * @return the name of the branche with given ID
	 * @throws SQLException
	 */
	public String getBranche(int bid) throws SQLException {
		// set the sql query
		String sql = "SELECT branche FROM branche WHERE BID=?";

		// try with connection and prepared statement
		// closes the connection and statement after usage
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			// set WHERE in statement
			preparedStatement.setInt(1, bid);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				// return the result
				return getBrancheFromResultSet(resultSet).get(0);
			}
		}
	}

	/**
	 * @param branche
	 * @return the id of the branche with given name
	 * @throws SQLException
	 */
	public int getBID(String branche) throws SQLException {
		int bid = -1;
		// set the sql query
		String sql = "SELECT BID FROM branche WHERE branche=?";

		// try with connection and prepared statement
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			// set WHERE in statement
			preparedStatement.setString(1, branche);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// fetch the result
					bid = resultSet.getInt("BID");
				}
			}
		}
		return bid;
	}

	/**
	 * @return a List of all branchen in the database
	 * @throws SQLException
	 */
	public List<String> getAllBranchen() throws SQLException {
		// set the sql query
		String sql = "SELECT branche FROM branche";

		// try with connection and prepared statement
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				// return the result
				return getBrancheFromResultSet(resultSet);
			}
		}
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
