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
	private Connection connect = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	private void open() throws SQLException {
		final DataSource dbconnection = new DataSource();
		this.connect = dbconnection.getConnection();
	}

	// You need to close the resultSet
	private void close() throws SQLException {
		if (this.resultSet != null) {
			this.resultSet.close();
		}

		if (this.preparedStatement != null) {
			this.preparedStatement.close();
		}

		if (this.connect != null) {
			this.connect.close();
		}

	}

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
		try {
			open();

			this.preparedStatement = this.connect
					.prepareStatement("INSERT INTO Nutzer values (default, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?)");
			this.preparedStatement.setString(1, nutzer.getFirstName());
			this.preparedStatement.setString(2, nutzer.getLastName());
			int sexId = new SexDAO().getSex(nutzer.getSex());
			this.preparedStatement.setInt(3, sexId);
			this.preparedStatement.setObject(4, nutzer.getBirthdate());
			this.preparedStatement.setString(5, nutzer.geteMail());
			this.preparedStatement.setString(6, nutzer.getPassword());
			this.preparedStatement.setString(7, address.getPlz());
			this.preparedStatement.setString(8, address.getCity());
			this.preparedStatement.setString(9, address.getStrasse());
			this.preparedStatement.setString(10, address.getNumber());
			this.preparedStatement.setString(11, nutzer.getStatus().getText());
			this.preparedStatement.executeUpdate();

			this.preparedStatement = this.connect.prepareStatement("SELECT LAST_INSERT_ID()");
			this.resultSet = this.preparedStatement.executeQuery();
			while (this.resultSet.next()) {
				nid = this.resultSet.getInt("last_insert_id()");
			}
		} finally {
			close();
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
				final Nutzer tempNutzer = new Nutzer(firstName, lastName, sex, birthdate, eMail, password,
						new Adresse(plz, city, street, number), Status.valueOf(status));
				tempNutzer.setNID(nutzerId);
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
		try {
			open();
			this.preparedStatement = this.connect.prepareStatement("SELECT * FROM Nutzer WHERE eMail = ?");
			this.preparedStatement.setString(1, eMail);

			this.resultSet = this.preparedStatement.executeQuery();
			final List<Nutzer> result = getNutzerFromResultSet(this.resultSet);
			if (result.isEmpty()) {
				return null;
			} else {
				return result.get(0);
			}
		} finally {
			close();
		}
	}

	/**
	 * @param id
	 * @return a {@link model.Nutzer} with given eMail
	 * @throws SQLException
	 */
	public Nutzer getNutzer(final int id) throws SQLException {
		try {
			open();
			this.preparedStatement = this.connect.prepareStatement("SELECT * FROM Nutzer WHERE NID = ?");
			this.preparedStatement.setInt(1, id);

			this.resultSet = this.preparedStatement.executeQuery();
			return getNutzerFromResultSet(this.resultSet).get(0);
		} finally {
			close();
		}
	}

	/**
	 * @return a List of all {@link model.Nutzer Nutzer} in database
	 * @throws SQLException
	 */
	public List<Nutzer> getAllNutzer() throws SQLException {
		try {
			open();
			this.preparedStatement = this.connect.prepareStatement("SELECT * FROM Nutzer");
			this.resultSet = this.preparedStatement.executeQuery();
			return getNutzerFromResultSet(this.resultSet);
		} finally {
			close();
		}
	}

	/**
	 * @param nutzer
	 * @throws SQLException
	 */
	public void changeNutzer(Nutzer nutzer) throws SQLException {
		final Adresse address = nutzer.getAddress();
		try {
			open();
			this.preparedStatement = this.connect.prepareStatement(
					"UPDATE Nutzer SET eMail = ?, password = ?, firstName = ?, lastName = ?, sexId = ?, birthdate = ?, "
							+ "plz = ?, city = ?, street = ?, number = ?, status = ? WHERE NID = ?");
			this.preparedStatement.setString(1, nutzer.geteMail());
			this.preparedStatement.setString(2, nutzer.getPassword());
			this.preparedStatement.setString(3, nutzer.getFirstName());
			this.preparedStatement.setString(4, nutzer.getLastName());
			int sexId = new SexDAO().getSex(nutzer.getSex());
			this.preparedStatement.setInt(5, sexId);
			this.preparedStatement.setObject(6, nutzer.getBirthdate());
			this.preparedStatement.setString(7, address.getPlz());
			this.preparedStatement.setString(8, address.getCity());
			this.preparedStatement.setString(9, address.getStrasse());
			this.preparedStatement.setString(10, address.getNumber());
			this.preparedStatement.setString(11, nutzer.getStatus().getText());
			this.preparedStatement.setInt(12, nutzer.getNID());
			this.preparedStatement.executeUpdate();
		} finally {
			close();
		}
	}

	/**
	 * @param nid
	 * @throws SQLException
	 */
	public void deleteNutzer(final int nid) throws SQLException {
		try {
			open();
			this.preparedStatement = this.connect.prepareStatement("DELETE FROM Nutzer WHERE NID = ?");
			this.preparedStatement.setInt(1, nid);

			this.preparedStatement.executeUpdate();
		} finally {
			close();
		}
	}
}
