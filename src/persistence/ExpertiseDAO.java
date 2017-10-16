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
 * Class is a DAO for the table 'expertise'
 * 
 * @author domin
 *
 */
public class ExpertiseDAO {
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
	 * @param eid
	 * @return the name of the expertise with given ID
	 * @throws SQLException
	 */
	public String getExpertise(int eid) throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement("SELECT expertise FROM expertise WHERE EID=?");
			preparedStatement.setInt(1, eid);

			resultSet = preparedStatement.executeQuery();
			return getExpertiseFromResultSet(resultSet).get(0);
		} finally {
			close();
		}
	}

	/**
	 * @param expertise
	 * @return the id of the expertise with given name
	 * @throws SQLException
	 */
	public int getExpertise(String expertise) throws SQLException {
		int eid = -1;
		try {
			open();
			preparedStatement = connect.prepareStatement("SELECT EID FROM expertise WHERE expertise=?");
			preparedStatement.setString(1, expertise);

			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				eid = resultSet.getInt("EID");
			}
		} finally {
			close();
		}
		return eid;
	}

	/**
	 * @return a List of all expertises in the database
	 * @throws SQLException
	 */
	public List<String> getAllExpertises() throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement("SELECT expertise FROM expertise");
			resultSet = preparedStatement.executeQuery();
			return getExpertiseFromResultSet(resultSet);
		} finally {
			close();
		}
	}

	private List<String> getExpertiseFromResultSet(ResultSet resultSet) throws SQLException {
		List<String> result = new LinkedList<>();
		while (resultSet.next()) {
			String expertise = resultSet.getString("expertise");
			result.add(expertise);
		}
		return result;
	}
}
