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
 * Class is a DAO for the table 'sex'
 * 
 * @author domin
 *
 */
public class SexDAO {
	private DataSource datasource = DataSource.getInstance();

	/**
	 * @param sexId
	 * @return the name of the sex with given ID
	 * @throws SQLException
	 */
	public String getSex(int sexId) throws SQLException {
		// set the sql query
		String sql = "SELECT sex FROM sex WHERE sexID = ?";

		// try with connection and prepared statement
		// closes the connection and statement after usage
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			// set WHERE in statement
			preparedStatement.setInt(1, sexId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				// return the result
				return getSexFromResultSet(resultSet).get(0);
			}
		}
	}

	/**
	 * @param sex
	 * @return the id of the sex with given name
	 * @throws SQLException
	 */
	public int getSex(String sex) throws SQLException {
		int sexId = -1;
		// set the sql query
		String sql = "SELECT sexID FROM sex WHERE sex = ?";

		// try with connection and prepared statement
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			// set WHERE in statement
			preparedStatement.setString(1, sex);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// fetch the result
					sexId = resultSet.getInt("sexID");
				}
			}
		}

		return sexId;
	}

	/**
	 * @return a List of all sex in the database
	 * @throws SQLException
	 */
	public List<String> getAllSex() throws SQLException {
		// set the sql query
		String sql = "SELECT sex FROM sex";

		// try with connection and prepared statement
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				// return the result
				return getSexFromResultSet(resultSet);
			}
		}
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
