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
import model.Unternehmensprofil;
import util.exception.ValidateConstrArgsException;

/**
 * @author domin
 *
 */
public class UnternehmensprofilDAO {
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
	 * @param unternehmen
	 * @return the generated ID of the new {@link model.Unternehmensprofil}
	 * @throws SQLException
	 */
	public int addUnternehmensprofil(Unternehmensprofil unternehmen) throws SQLException {
		Adresse address = unternehmen.getAddress();
		int uid = -1;
		try {
			open();

			preparedStatement = connect.prepareStatement(
					"INSERT INTO Unternehmensprofil values (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int nutzerId = unternehmen.getNutzer().getNID();
			preparedStatement.setInt(1, nutzerId);
			int bid = new BrancheDAO().getBID(unternehmen.getBranche());
			preparedStatement.setInt(2, bid);
			preparedStatement.setString(3, unternehmen.getName());
			preparedStatement.setString(4, unternehmen.getLegalForm());
			preparedStatement.setObject(5, unternehmen.getFounding());
			preparedStatement.setInt(6, unternehmen.getEmployees());
			preparedStatement.setString(7, unternehmen.getDescription());
			preparedStatement.setString(8, unternehmen.getWebsite());
			preparedStatement.setString(9, unternehmen.getCeoFirstName());
			preparedStatement.setString(10, unternehmen.getCeoLastName());
			preparedStatement.setString(11, address.getPlz());
			preparedStatement.setString(12, address.getCity());
			preparedStatement.setString(13, address.getStrasse());
			preparedStatement.setString(14, address.getNumber());
			preparedStatement.executeUpdate();

			preparedStatement = connect.prepareStatement("SELECT LAST_INSERT_ID()");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				uid = resultSet.getInt("last_insert_id()");
			}
		} finally {
			close();
		}
		return uid;
	}

	/**
	 * @param uid
	 * @return a {@link model.Unternehmensprofil} with given ID
	 * @throws SQLException
	 */
	public Unternehmensprofil getUnternehmensprofil(int uid) throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement(
					"SELECT UID, NID, branche.branche, name, legalForm, founding, employees, description, "
							+ "website, ceoFirstName, ceoLastName, plz, city, street, number "
							+ "FROM unternehmensprofil " + "INNER JOIN branche ON unternehmensprofil.BID = branche.BID "
							+ "WHERE UID = ?");
			preparedStatement.setInt(1, uid);

			resultSet = preparedStatement.executeQuery();
			return getUnternehmensprofilFromResultSet(resultSet).get(0);
		} finally {
			close();
		}
	}

	/**
	 * @return a List of all {@link model.Unternehmensprofil Unternehmensprofile} in
	 *         database
	 * @throws SQLException
	 */
	public List<Unternehmensprofil> getAllUnternehmen() throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement(
					"SELECT UID, NID, branche.branche, name, legalForm, founding, employees, description, "
							+ "website, ceoFirstName, ceoLastName, plz, city, street, number "
							+ "FROM Unternehmensprofil"
							+ "INNER JOIN branche ON unternehmensprofil.BID = branche.BID ");
			resultSet = preparedStatement.executeQuery();
			return getUnternehmensprofilFromResultSet(resultSet);
		} finally {
			close();
		}
	}

	/**
	 * @param uid
	 * @throws SQLException
	 */
	public void deleteUnternehmensprofil(int uid) throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement("DELETE FROM Unternehmensprofil WHERE UID = ?");
			preparedStatement.setInt(1, uid);

			preparedStatement.executeUpdate();
		} finally {
			close();
		}
	}

	private List<Unternehmensprofil> getUnternehmensprofilFromResultSet(ResultSet resultSet) throws SQLException {
		List<Unternehmensprofil> result = new LinkedList<>();
		while (resultSet.next()) {
			int unternehmensId = resultSet.getInt("UID");
			int nutzerId = resultSet.getInt("NID");
			Nutzer nutzer = new NutzerDAO().getNutzer(nutzerId);
			String branche = resultSet.getString("branche.branche");
			String name = resultSet.getString("name");
			String legalForm = resultSet.getString("legalForm");
			Date foundingSQL = resultSet.getDate("founding");
			LocalDate founding = foundingSQL.toLocalDate();
			int employees = resultSet.getInt("employees");
			String description = resultSet.getString("description");
			String website = resultSet.getString("website");
			String ceoFirstName = resultSet.getString("ceoFirstName");
			String ceoLastName = resultSet.getString("ceoLastName");
			String plz = resultSet.getString("plz");
			String city = resultSet.getString("city");
			String street = resultSet.getString("street");
			String number = resultSet.getString("number");
			try {
				Unternehmensprofil tempUnternehmen = new Unternehmensprofil(name, legalForm,
						new Adresse(plz, city, street, number), founding, employees, description, branche, website,
						ceoFirstName, ceoLastName, nutzer);
				tempUnternehmen.setId(unternehmensId);
				result.add(tempUnternehmen);
			} catch (ValidateConstrArgsException e) {
				throw new SQLException("Datenbank ist inkonsistent!", e);
			}
		}
		return result;
	}

	/**
	 * @param aUnternehmen
	 * @throws SQLException
	 */
	public void changeUnternehmen(Unternehmensprofil aUnternehmen) throws SQLException {
		Adresse address = aUnternehmen.getAddress();
		try {
			open();
			this.preparedStatement = this.connect.prepareStatement(
					"UPDATE unternehmensprofil SET NID = ?, BID = ?, name = ?, legalForm = ?, founding = ?, employees = ?, description = ?, "
							+ "website = ?, ceoFirstName = ?, ceoLastName = ?, plz = ?, city = ?, street = ?,"
							+ " number = ? WHERE UID = ?");
			int nutzerId = aUnternehmen.getNutzer().getNID();
			preparedStatement.setInt(1, nutzerId);
			int bid = new BrancheDAO().getBID(aUnternehmen.getBranche());
			preparedStatement.setInt(2, bid);
			preparedStatement.setString(3, aUnternehmen.getName());
			preparedStatement.setString(4, aUnternehmen.getLegalForm());
			preparedStatement.setObject(5, aUnternehmen.getFounding());
			preparedStatement.setInt(6, aUnternehmen.getEmployees());
			preparedStatement.setString(7, aUnternehmen.getDescription());
			preparedStatement.setString(8, aUnternehmen.getWebsite());
			preparedStatement.setString(9, aUnternehmen.getCeoFirstName());
			preparedStatement.setString(10, aUnternehmen.getCeoLastName());
			preparedStatement.setString(11, address.getPlz());
			preparedStatement.setString(12, address.getCity());
			preparedStatement.setString(13, address.getStrasse());
			preparedStatement.setString(14, address.getNumber());
			preparedStatement.setInt(15, aUnternehmen.getId());
			preparedStatement.executeUpdate();
		} finally {
			close();
		}
	}

	/**
	 * @param aNid
	 * @return {@link model.Unternehmensprofil} with given {@link model.Nutzer}
	 * @throws SQLException
	 */
	public Unternehmensprofil getUnternehmensprofilByNutzer(int aNid) throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement(
					"SELECT UID, NID, branche.branche, name, legalForm, founding, employees, description, "
							+ "website, ceoFirstName, ceoLastName, plz, city, street, number "
							+ "FROM unternehmensprofil " + "INNER JOIN branche ON unternehmensprofil.BID = branche.BID "
							+ "WHERE NID = ?");
			preparedStatement.setInt(1, aNid);
			resultSet = preparedStatement.executeQuery();
			List<Unternehmensprofil> resultList = getUnternehmensprofilFromResultSet(resultSet);
			if (resultList.size() > 0) {
				return resultList.get(0);
			} else {
				return null;
			}
		} finally {
			close();
		}
	}
}
