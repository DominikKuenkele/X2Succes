package persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import model.Adresse;
import model.Nutzer;
import model.Status;
import util.exception.DuplicateEntryException;
import util.exception.ValidateConstrArgsException;

/**
 * @author domin
 *
 */
public class NutzerDAO {
	private DataSource datasource = DataSource.getInstance();

	/**
	 * @param nutzer
	 * @return the generated ID of the new {@link model.Nutzer}
	 * @throws DuplicateEntryException
	 * @throws SQLException
	 */
	public int addNutzer(final Nutzer nutzer) throws DuplicateEntryException, SQLException {
		final Adresse address = nutzer.getAddress();
		int nid = -1;
		if (getNutzer(nutzer.geteMail()) != null) {
			throw new DuplicateEntryException("E-Mail wird schon verwendet!");
		}

		try (Connection connect = datasource.getConnection()) {
			String sql = "INSERT INTO Nutzer values (default, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				preparedStatement.setString(1, nutzer.getFirstName());
				preparedStatement.setString(2, nutzer.getLastName());
				int sexId = new SexDAO().getSex(nutzer.getSex());
				preparedStatement.setInt(3, sexId);
				preparedStatement.setObject(4, nutzer.getBirthdate());
				preparedStatement.setString(5, nutzer.geteMail());
				preparedStatement.setString(6, nutzer.getPassword());
				preparedStatement.setString(7, address.getPlz());
				preparedStatement.setString(8, address.getCity());
				preparedStatement.setString(9, address.getStreet());
				preparedStatement.setString(10, address.getNumber());
				preparedStatement.setString(11, nutzer.getStatus().getText());

				preparedStatement.executeUpdate();
			}

			sql = "SELECT LAST_INSERT_ID()";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						nid = resultSet.getInt("last_insert_id()");
					}
				}
			}
		}
		return nid;
	}

	private List<Nutzer> getNutzerFromResultSet(final ResultSet resultSet) throws SQLException {
		final List<Nutzer> result = new LinkedList<>();
		while (resultSet.next()) {
			final int nutzerId = resultSet.getInt("NID");
			final String firstName = resultSet.getString("firstName");
			final String lastName = resultSet.getString("lastName");
			final int sexId = resultSet.getInt("sexId");
			final String sex = new SexDAO().getSex(sexId);
			final Date birthdateSQL = resultSet.getDate("birthdate");
			final LocalDate birthdate = birthdateSQL.toLocalDate();
			final String eMail = resultSet.getString("eMail");
			final String password = resultSet.getString("password");
			final String plz = resultSet.getString("plz");
			final String city = resultSet.getString("city");
			final String street = resultSet.getString("street");
			final String number = resultSet.getString("number");
			final String status = resultSet.getString("status");
			try {
				final Nutzer tempNutzer = new Nutzer(nutzerId, firstName, lastName, sex, birthdate, eMail, password,
						new Adresse(plz, city, street, number), Status.valueOf(status));
				result.add(tempNutzer);

			} catch (final ValidateConstrArgsException e) {
				throw new SQLException("Datenbank ist inkonsistent!", e);
			}
		}
		return result;
	}

	/**
	 * @param eMail
	 * @return a {@link model.Nutzer} with given ID
	 * @throws SQLException
	 */
	public Nutzer getNutzer(final String eMail) throws SQLException {
		String sql = "SELECT * FROM Nutzer WHERE eMail = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setString(1, eMail);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				final List<Nutzer> result = getNutzerFromResultSet(resultSet);
				if (result.isEmpty()) {
					return null;
				} else {
					return result.get(0);
				}
			}
		}
	}

	/**
	 * @param id
	 * @return a {@link model.Nutzer} with given eMail
	 * @throws SQLException
	 */
	public Nutzer getNutzer(final int id) throws SQLException {
		String sql = "SELECT * FROM Nutzer WHERE NID = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				final List<Nutzer> result = getNutzerFromResultSet(resultSet);
				if (result.isEmpty()) {
					return null;
				} else {
					return result.get(0);
				}
			}
		}
	}

	/**
	 * @return a List of all {@link model.Nutzer Nutzer} in database
	 * @throws SQLException
	 */
	public List<Nutzer> getAllNutzer() throws SQLException {
		String sql = "SELECT * FROM Nutzer";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return getNutzerFromResultSet(resultSet);
			}
		}
	}

	/**
	 * @param nutzer
	 * @throws SQLException
	 */
	public void changeNutzer(Nutzer nutzer) throws SQLException {
		final Adresse address = nutzer.getAddress();
		String sql = "UPDATE Nutzer SET eMail = ?, password = ?, firstName = ?, lastName = ?, sexId = ?, birthdate = ?, "
				+ "plz = ?, city = ?, street = ?, number = ?, status = ? WHERE NID = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setString(1, nutzer.geteMail());
			preparedStatement.setString(2, nutzer.getPassword());
			preparedStatement.setString(3, nutzer.getFirstName());
			preparedStatement.setString(4, nutzer.getLastName());
			int sexId = new SexDAO().getSex(nutzer.getSex());
			preparedStatement.setInt(5, sexId);
			preparedStatement.setObject(6, nutzer.getBirthdate());
			preparedStatement.setString(7, address.getPlz());
			preparedStatement.setString(8, address.getCity());
			preparedStatement.setString(9, address.getStreet());
			preparedStatement.setString(10, address.getNumber());
			preparedStatement.setString(11, nutzer.getStatus().getText());
			preparedStatement.setInt(12, nutzer.getNID());

			preparedStatement.executeUpdate();
		}
	}

	/**
	 * @param nid
	 * @throws SQLException
	 */
	public void deleteNutzer(final int nid) throws SQLException {
		String sql = "DELETE FROM Nutzer WHERE NID = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, nid);

			preparedStatement.executeUpdate();
		}
	}
}
