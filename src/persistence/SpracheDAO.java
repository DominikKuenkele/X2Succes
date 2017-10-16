package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
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
		String sql = "SELECT SID FROM Sprachen WHERE sprache = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setString(1, sprache);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
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
		String sql = "SELECT * FROM Sprachen WHERE SID = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, sid);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
		List<String> result = new LinkedList<>();
		String sql = "SELECT * FROM Sprachen";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				result = getSpracheFromResultSet(resultSet);
			}
		}
		return result;
	}

	private List<String> getSpracheFromResultSet(ResultSet resultSet) throws SQLException {
		List<String> result = new LinkedList<>();
		while (resultSet.next()) {
			String sprache = resultSet.getString("Sprache");
			result.add(sprache);
		}
		return result;
	}
}
