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
	private Connection connect = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	private void open() throws SQLException {
		DataSource dbconnection = new DataSource();
		connect = dbconnection.getConnection();
	}

	private void close() throws SQLException {
		if (resultSet != null) {
			resultSet.close();
		}

		if (preparedStatement != null) {
			preparedStatement.close();
		}

		if (connect != null) {
			connect.close();
		}

	}

	/**
	 * @param sexId
	 * @return the name of the sex with given ID
	 * @throws SQLException
	 */
	public String getSex(int sexId) throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement("SELECT sex FROM sex WHERE sexID = ?");
			preparedStatement.setInt(1, sexId);

			resultSet = preparedStatement.executeQuery();
			return getSexFromResultSet(resultSet).get(0);
		} finally {
			close();
		}
	}

	/**
	 * @param sex
	 * @return the id of the sex with given name
	 * @throws SQLException
	 */
	public int getSex(String sex) throws SQLException {
		int sexId = -1;
		try {
			open();
			preparedStatement = connect.prepareStatement("SELECT sexID FROM sex WHERE sex = ?");
			preparedStatement.setString(1, sex);

			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				sexId = resultSet.getInt("sexID");
			}
		} finally {
			close();
		}
		return sexId;
	}

	/**
	 * @return a List of all sex in the database
	 * @throws SQLException
	 */
	public List<String> getAllSex() throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement("SELECT sex FROM sex");
			resultSet = preparedStatement.executeQuery();
			return getSexFromResultSet(resultSet);
		} finally {
			close();
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
