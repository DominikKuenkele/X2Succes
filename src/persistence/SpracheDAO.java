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
	 * @param sprache
	 * @return the Id with given language
	 * @throws SQLException
	 */
	public int getSID(String sprache) throws SQLException {
		int result = 0;
		try {
			open();
			preparedStatement = connect.prepareStatement("SELECT SID FROM Sprachen WHERE sprache = ?");
			preparedStatement.setString(1, sprache);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int sid = resultSet.getInt("SID");
				result = sid;
			}
		} finally {
			close();
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
		try {
			open();
			preparedStatement = connect.prepareStatement("SELECT * FROM Sprachen WHERE SID = ?");
			preparedStatement.setInt(1, sid);

			resultSet = preparedStatement.executeQuery();
			result = getSpracheFromResultSet(resultSet).get(0);
		} finally {
			close();
		}
		return result;
	}

	/**
	 * @return a list of all languages in database
	 * @throws SQLException
	 */
	public List<String> getAllSprachen() throws SQLException {
		List<String> result = new LinkedList<>();
		try {
			open();
			preparedStatement = connect.prepareStatement("SELECT * FROM Sprachen");
			resultSet = preparedStatement.executeQuery();
			result = getSpracheFromResultSet(resultSet);
		} finally {
			close();
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
