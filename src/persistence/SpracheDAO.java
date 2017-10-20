package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
	private DataSource datasource = DataSource.getInstance();

	/**
	 * @param sprache
	 * @return the Id with given language
	 * @throws SQLException
	 */
	public int getSID(String sprache) throws SQLException {
		int result = 0;
		// set the sql query
		String sql = "SELECT SID FROM Sprachen WHERE sprache = ?";

		// try with connection and prepared statement
		// closes the connection and statement after usage
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			// set WHERE in statement
			preparedStatement.setString(1, sprache);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// return the result
					int sid = resultSet.getInt("SID");
					result = sid;
				}
			}
		}
		return result;
	}

	/**
	 * @param sid
	 * @return the language with given id
	 * @throws SQLException
	 */
	public String getSprache(int sid) throws SQLException {
		String result = "";
		// set the sql query
		String sql = "SELECT * FROM Sprachen WHERE SID = ?";

		// try with connection and prepared statement
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			// set WHERE in statement
			preparedStatement.setInt(1, sid);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				// fetch the result
				result = getSpracheFromResultSet(resultSet).get(0);
			}
		}
		return result;
	}

	/**
	 * @return a list of all languages in database
	 * @throws SQLException
	 */
	public List<String> getAllSprachen() throws SQLException {
		// set the sql query
		List<String> result = new LinkedList<>();
		String sql = "SELECT * FROM Sprachen";

		// try with connection and prepared statement
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				// return the result
				result = getSpracheFromResultSet(resultSet);
			}
		}
		return result;
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
