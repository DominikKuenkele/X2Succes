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
import util.exception.ValidateArgsException;

/**
 * Class is a DAO for the table 'unternehmensprofil'
 * 
 * @author domin
 *
 */
public class UnternehmensprofilDAO {
	private DataSource datasource = DataSource.getInstance();

	private final String TABLE = "unternehmensprofil";

	/**
	 * @param unternehmen
	 * @return the generated ID of the new {@link model.Unternehmensprofil}
	 * @throws SQLException
	 */
	public int addUnternehmensprofil(Unternehmensprofil unternehmen) throws SQLException {
		// get address from the unternehmensprofil
		Adresse address = unternehmen.getAddress();
		int uid = -1;

		// try with connection
		try (Connection connect = datasource.getConnection()) {
			// set the sql query
			String sql = "INSERT INTO Unternehmensprofil values (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			// try with statement
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				// set params
				// get the nutzer id
				int nutzerId = unternehmen.getNutzer().getNID();
				preparedStatement.setInt(1, nutzerId);
				// get the branchen id
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
				preparedStatement.setString(13, address.getStreet());
				preparedStatement.setString(14, address.getNumber());

				preparedStatement.executeUpdate();
			}

			// fetch the id generated by database
			sql = "SELECT LAST_INSERT_ID()";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						uid = resultSet.getInt("last_insert_id()");
					}
				}
			}
		}
		return uid;
	}

	/**
	 * @param uid
	 * @return a {@link model.Unternehmensprofil} with given ID
	 * @throws SQLException
	 */
	public Unternehmensprofil getUnternehmensprofil(int uid) throws SQLException {
		Sql statement = new Sql();

		String columns[] = { "UID", "NID", "branche.branche", "name", "legalForm", "founding", "employees",
				"description", "website", "ceoFirstName", "ceoLastName", "plz", "city", "street", "number" };
		String condition = "UID=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(uid);

		statement.select(columns).from(TABLE).innerJoin("branche", "unternehmensprofil.BID = branche.BID")
				.where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		return getUnternehmensprofilFromResultSet(result).get(0);
	}

	/**
	 * @return a List of all {@link model.Unternehmensprofil Unternehmensprofile} in
	 *         database
	 * @throws SQLException
	 */
	public List<Unternehmensprofil> getAllUnternehmen() throws SQLException {
		Sql statement = new Sql();

		String columns[] = { "UID", "NID", "branche.branche", "name", "legalForm", "founding", "employees",
				"description", "website", "ceoFirstName", "ceoLastName", "plz", "city", "street", "number" };

		statement.select(columns).from(TABLE).innerJoin("branche", "unternehmensprofil.BID = branche.BID");
		ResultSet result = statement.executeQuery();

		return getUnternehmensprofilFromResultSet(result);
	}

	/**
	 * @param uid
	 * @throws SQLException
	 */
	public void deleteUnternehmensprofil(int uid) throws SQLException {
		// set the sql query
		String sql = "DELETE FROM Unternehmensprofil WHERE UID = ?";

		// try with connection and prepared statement
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			// set param
			preparedStatement.setInt(1, uid);

			preparedStatement.executeUpdate();
		}
	}

	/**
	 * processes fetched data from database to a {@link model.Unternehmensprofil
	 * Unternehmensprofil}
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private List<Unternehmensprofil> getUnternehmensprofilFromResultSet(ResultSet resultSet) throws SQLException {
		List<Unternehmensprofil> result = new LinkedList<>();
		// for each row returned from database
		while (resultSet.next()) {
			// get values from resultset
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
				// store fetched data in Unternehmensprofil-Object
				Unternehmensprofil tempUnternehmen = new Unternehmensprofil(unternehmensId, name, legalForm,
						new Adresse(plz, city, street, number), founding, employees, description, branche, website,
						ceoFirstName, ceoLastName, nutzer);
				result.add(tempUnternehmen);
			} catch (ValidateArgsException e) {
				throw new SQLException("Datenbank ist inkonsistent!", e);
			}
		}
		return result;
	}

	/**
	 * changes an existing unternehmensprofil in the database
	 * 
	 * @param aUnternehmen
	 * @throws SQLException
	 */
	public void changeUnternehmen(Unternehmensprofil aUnternehmen) throws SQLException {
		// get address from unternehmensprofil
		Adresse address = aUnternehmen.getAddress();

		String sql = "UPDATE unternehmensprofil SET NID = ?, BID = ?, name = ?, legalForm = ?, founding = ?, employees = ?, description = ?, "
				+ "website = ?, ceoFirstName = ?, ceoLastName = ?, plz = ?, city = ?, street = ?,"
				+ " number = ? WHERE UID = ?";

		// try with connection
		// change existing unternehmensprofil
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			// get nutzer id
			int nutzerId = aUnternehmen.getNutzer().getNID();
			preparedStatement.setInt(1, nutzerId);
			// get branchen id
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
			preparedStatement.setString(13, address.getStreet());
			preparedStatement.setString(14, address.getNumber());
			preparedStatement.setInt(15, aUnternehmen.getUid());

			preparedStatement.executeUpdate();
		}
	}

	/**
	 * @param nid
	 * @return {@link model.Unternehmensprofil} with given {@link model.Nutzer}
	 * @throws SQLException
	 */
	public Unternehmensprofil getUnternehmensprofilByNutzer(int nid) throws SQLException {
		Sql statement = new Sql();

		String columns[] = { "UID", "NID", "branche.branche", "name", "legalForm", "founding", "employees",
				"description", "website", "ceoFirstName", "ceoLastName", "plz", "city", "street", "number" };
		String condition = "NID=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(nid);

		statement.select(columns).from(TABLE).innerJoin("branche", "unternehmensprofil.BID = branche.BID")
				.where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		return getUnternehmensprofilFromResultSet(result).get(0);
	}
}
