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
 * Class is a DAO for the table 'graduation'
 * 
 * @author domin
 *
 */
public class AbschlussDAO {
	private DataSource datasource = DataSource.getInstance();

	/**
	 * @param gid
	 * @return the name of the graduation with given ID
	 * @throws SQLException
	 */
	public String getAbschluss(int gid) throws SQLException {
		String sql = "SELECT graduation FROM graduation WHERE GID=?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, gid);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return getAbschlussFromResultSet(resultSet).get(0);
			}
		}
	}

	/**
	 * @param abschluss
	 * @return the id of the graduation with given name
	 * @throws SQLException
	 */
	public int getAbschluss(String abschluss) throws SQLException {
		int gid = -1;
		String sql = "SELECT GID FROM graduation WHERE graduation=?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setString(1, abschluss);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					gid = resultSet.getInt("GID");
				}
			}
		}
		return gid;
	}

	/**
	 * @return a List of all graduations in the database
	 * @throws SQLException
	 */
	public List<String> getAllAbschluss() throws SQLException {
		String sql = "SELECT graduation FROM graduation";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return getAbschlussFromResultSet(resultSet);
			}
		}
	}

	private List<String> getAbschlussFromResultSet(ResultSet resultSet) throws SQLException {
		List<String> result = new LinkedList<>();
		while (resultSet.next()) {
			String abschluss = resultSet.getString("graduation");
			result.add(abschluss);
		}
		return result;
	}

	/**
	 * @param aAbschluss
	 * @return the hierarchy of a given graduation
	 * @throws SQLException
	 */
	public int getHierarchy(String aAbschluss) throws SQLException {
		int hier = -1;
		String sql = "SELECT hierarchy FROM graduation WHERE graduation=?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setString(1, aAbschluss);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					hier = resultSet.getInt("hierarchy");
				}
			}
		}
		return hier;
	}
}
