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
	private DataSource datasource = DataSource.getInstance();

	/**
	 * @param eid
	 * @return the name of the expertise with given ID
	 * @throws SQLException
	 */
	public String getExpertise(int eid) throws SQLException {
		String sql = "SELECT expertise FROM expertise WHERE EID=?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, eid);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return getExpertiseFromResultSet(resultSet).get(0);
			}
		}
	}

	/**
	 * @param expertise
	 * @return the id of the expertise with given name
	 * @throws SQLException
	 */
	public int getExpertise(String expertise) throws SQLException {
		int eid = -1;
		String sql = "SELECT EID FROM expertise WHERE expertise=?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setString(1, expertise);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					eid = resultSet.getInt("EID");
				}
			}
		}

		return eid;
	}

	/**
	 * @return a List of all expertises in the database
	 * @throws SQLException
	 */
	public List<String> getAllExpertises() throws SQLException {
		String sql = "SELECT expertise FROM expertise";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return getExpertiseFromResultSet(resultSet);
			}
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
