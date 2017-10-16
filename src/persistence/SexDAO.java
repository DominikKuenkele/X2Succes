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
		String sql = "SELECT sex FROM sex WHERE sexID = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, sexId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
		String sql = "SELECT sexID FROM sex WHERE sex = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setString(1, sex);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
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
		String sql = "SELECT sex FROM sex";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return getSexFromResultSet(resultSet);
			}
		}
	}

	private List<String> getSexFromResultSet(ResultSet resultSet) throws SQLException {
		List<String> result = new LinkedList<>();
		while (resultSet.next()) {
			String sex = resultSet.getString("sex");
			result.add(sex);
		}
		return result;
	}
}
