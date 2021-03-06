package persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import model.Jobangebot;
import model.Unternehmensprofil;
import util.exception.ValidateArgsException;

/**
 * @author domin
 *
 */
public class JobangebotDAO {
	private DataSource datasource = DataSource.getInstance();

	private final String TABLE = "jobangebot";

	/**
	 * @param jobangebot
	 * @return the generated ID of the new {@link model.Jobangebot}
	 * @throws SQLException
	 */
	public int addJobangebot(Jobangebot jobangebot) throws SQLException {
		// fetch unternehmensid from jobangebot
		int unternehmensId = jobangebot.getUnternehmensprofil().getUid();
		int jid = -1;

		// try with connection
		try (Connection connect = datasource.getConnection()) {
			// set the sql query
			String sql = "INSERT INTO Jobangebot values (default, ?, ?, ?, ?, ?, ?, ?, ?)";
			// try with statement
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				// set params
				preparedStatement.setInt(1, unternehmensId);
				// get the graduation id
				int gid = new AbschlussDAO().getAbschluss(jobangebot.getAbschluss());
				preparedStatement.setInt(2, gid);
				// get the expertise id
				int eid = new ExpertiseDAO().getExpertise(jobangebot.getFachgebiet());
				preparedStatement.setInt(3, eid);
				preparedStatement.setString(4, jobangebot.getJobTitel());
				preparedStatement.setString(5, jobangebot.getBeschreibung());
				preparedStatement.setObject(6, jobangebot.getFrist());
				preparedStatement.setInt(7, jobangebot.getGehalt());
				preparedStatement.setInt(8, jobangebot.getWochenstunden());

				preparedStatement.executeUpdate();
			}

			// fetch the id generated by database
			sql = "SELECT LAST_INSERT_ID()";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						jid = resultSet.getInt("last_insert_id()");
					}
				}
			}

			// insert the languages in another table
			sql = "INSERT INTO SprachenzuordnungJA values (?, ?)";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				List<String> sprachen = jobangebot.getSprachen();
				// for each language from jobangebot
				for (int i = 0; i < sprachen.size(); i++) {
					int sid = new SpracheDAO().getSID(sprachen.get(i));

					preparedStatement.setInt(1, jid);
					preparedStatement.setInt(2, sid);

					preparedStatement.executeUpdate();
				}
			}
		}
		return jid;
	}

	/**
	 * @param jid
	 * @return a {@link model.Jobangebot} with given ID
	 * @throws SQLException
	 */
	public Jobangebot getJobangebot(int jid) throws SQLException {
		SelectSql statement = new SelectSql();

		String columns[] = { "JID", "UID", "graduation.graduation", "expertise.expertise", "jobTitle", "description",
				"deadline", "salary", "weeklyHours" };
		String condition = "JID=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(jid);

		statement.select(columns).from(TABLE).innerJoin("graduation", "jobangebot.GID=graduation.GID")
				.innerJoin("expertise", "jobangebot.EID = expertise.EID").where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		return getJobangebotFromResultSet(result).get(0);
	}

	/**
	 * @return a List of all {@link model.Jobangebot Jobangebote} in the database
	 * @throws SQLException
	 */
	public List<Jobangebot> getAllJobangebote() throws SQLException {
		SelectSql statement = new SelectSql();

		String columns[] = { "JID", "UID", "graduation.graduation", "expertise.expertise", "jobTitle", "description",
				"deadline", "salary", "weeklyHours" };

		statement.select(columns).from(TABLE).innerJoin("graduation", "jobangebot.GID=graduation.GID")
				.innerJoin("expertise", "jobangebot.EID = expertise.EID");
		ResultSet result = statement.executeQuery();

		return getJobangebotFromResultSet(result);
	}

	/**
	 * @param jid
	 * @throws SQLException
	 */
	public void deleteJobangebot(int jid) throws SQLException {
		// set the sql query
		String sql = "DELETE FROM Jobangebot WHERE JID = ?";

		// try with connection and prepared statement
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			// set param
			preparedStatement.setInt(1, jid);

			preparedStatement.executeUpdate();
		}
	}

	/**
	 * changes an existing jobangebot in the database
	 * 
	 * @param jobangebot
	 * @throws SQLException
	 */
	public void changeJobangebot(Jobangebot jobangebot) throws SQLException {
		// get all languages of jobangebot
		List<String> sprachen = jobangebot.getSprachen();

		// try with connection
		try (Connection connect = datasource.getConnection()) {
			// delete all current languages in database for this jobangebot
			String sql = "DELETE FROM sprachenzuordnungJA WHERE JID = ?";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				preparedStatement.setInt(1, jobangebot.getJID());

				preparedStatement.executeUpdate();
			}

			// change existing jobangebot
			sql = "UPDATE jobangebot SET GID = ?, EID = ?, jobTitle = ?, description = ?, deadline = ?, "
					+ "salary = ?, weeklyHours = ? WHERE JID = ?";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				// get graduation id
				int gid = new AbschlussDAO().getAbschluss(jobangebot.getAbschluss());
				preparedStatement.setInt(1, gid);
				// get expertise id
				int eid = new ExpertiseDAO().getExpertise(jobangebot.getFachgebiet());
				preparedStatement.setInt(2, eid);
				preparedStatement.setString(3, jobangebot.getJobTitel());
				preparedStatement.setString(4, jobangebot.getBeschreibung());
				preparedStatement.setObject(5, jobangebot.getFrist());
				preparedStatement.setInt(6, jobangebot.getGehalt());
				preparedStatement.setInt(7, jobangebot.getWochenstunden());
				preparedStatement.setInt(8, jobangebot.getJID());

				preparedStatement.executeUpdate();

				// insert new languages in extra table
				for (int i = 0; i < sprachen.size(); i++) {
					int sid = new SpracheDAO().getSID(sprachen.get(i));

					String sql2 = "INSERT INTO SprachenzuordnungJA values (?, ?)";
					try (PreparedStatement preparedStatement2 = connect.prepareStatement(sql2)) {
						preparedStatement.setInt(1, jobangebot.getJID());
						preparedStatement.setInt(2, sid);

						preparedStatement.executeUpdate();
					}
				}
			}
		}
	}

	/**
	 * processes fetched data from database to a {@link model.Jobangebot Jobangebot}
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private List<Jobangebot> getJobangebotFromResultSet(ResultSet resultSet) throws SQLException {
		List<Jobangebot> result = new LinkedList<>();
		// for each row returned from database
		while (resultSet.next()) {
			// get values from resultset
			int jobangebotsId = resultSet.getInt("jobangebot.JID");
			int unternehmensId = resultSet.getInt("jobangebot.uid");
			Unternehmensprofil unternehmen = new UnternehmensprofilDAO().getUnternehmensprofil(unternehmensId);
			String graduation = resultSet.getString("graduation.graduation");
			String expertise = resultSet.getString("expertise.expertise");
			String jobTitle = resultSet.getString("jobangebot.jobTitle");
			String description = resultSet.getString("jobangebot.description");
			Date deadlineSQL = resultSet.getDate("jobangebot.deadline");
			LocalDate deadline = deadlineSQL.toLocalDate();
			int salary = resultSet.getInt("jobangebot.salary");
			int weeklyHours = resultSet.getInt("jobangebot.weeklyHours");
			List<String> sprachen = getLanguageInJobangebot(jobangebotsId);
			try {
				// store fetched data in Jobangebot-Object
				Jobangebot tempJobangebot = new Jobangebot(jobangebotsId, graduation, expertise, sprachen, jobTitle,
						description, deadline, salary, weeklyHours, unternehmen);
				result.add(tempJobangebot);

			} catch (ValidateArgsException e) {
				throw new SQLException("Datenbank ist inkonsistent!", e);
			}
		}
		return result;
	}

	/**
	 * @param jid
	 * @return the languages from extra table
	 * @throws SQLException
	 */
	private List<String> getLanguageInJobangebot(int jid) throws SQLException {
		List<String> res = new LinkedList<>();
		SelectSql statement = new SelectSql();

		String columns = "SID";
		String condition = "JID=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(jid);

		statement.select(columns).from("SprachenzuordnungJA").where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		while (result.next()) {
			int sid = result.getInt("SID");
			String sprache = new SpracheDAO().getSprache(sid);
			res.add(sprache);
		}
		
		return res;
	}

	/**
	 * @param aName
	 * @return a list of {@link model.Jobangebot Jobangebote} with given name
	 * @throws SQLException
	 */
	public List<Jobangebot> searchForName(String aName) throws SQLException {
		List<Jobangebot> tempResult = new LinkedList<>();
		// escape controlchars and replace * with % (wildcard)
		String name = aName.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![").replace("*",
				"%");
		String sql = "SELECT jobangebot.JID FROM jobangebot "
				+ "INNER JOIN unternehmensprofil ON jobangebot.UID=unternehmensprofil.UID "
				+ "WHERE unternehmensprofil.name LIKE ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setString(1, name);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// get the jids
					int jid = resultSet.getInt("jobangebot.JID");
					// get the profil with given id
					Jobangebot tempJobangebot = new JobangebotDAO().getJobangebot(jid);
					// add to result if not null
					if (tempJobangebot != null) {
						tempResult.add(tempJobangebot);
					}
				}
			}
		}

		// remove duplicates
		List<Jobangebot> result = new LinkedList<>();
		for (Jobangebot j : tempResult) {
			if (!result.contains(j)) {
				result.add(j);
			}
		}
		return result;
	}

	/**
	 * @param aBranche
	 * @return a list of {@link model.Jobangebot Jobangebote} with given branche
	 * @throws SQLException
	 */
	public List<Jobangebot> searchForBranche(String aBranche) throws SQLException {
		List<Jobangebot> result = new LinkedList<>();
		// escape controlchars and replace * with % (wildcard)
		String branche = aBranche.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![")
				.replace("*", "%");
		String sql = "SELECT jobangebot.JID FROM jobangebot "
				+ "INNER JOIN unternehmensprofil ON jobangebot.UID = unternehmensprofil.UID "
				+ "INNER JOIN branche ON unternehmensprofil.BID = branche.BID WHERE branche.branche LIKE ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setString(1, branche);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// get the jids
					int jid = resultSet.getInt("jobangebot.JID");
					// get the profil with given id
					Jobangebot tempJobangebot = new JobangebotDAO().getJobangebot(jid);
					// add to result if not null
					if (tempJobangebot != null) {
						result.add(tempJobangebot);
					}
				}
			}
		}
		return result;
	}

	/**
	 * @param aAbschluss
	 * @param aExpertise
	 * @return a list of {@link model.Jobangebot Jobangebote} with lower graduation
	 *         in given expertise
	 * @throws SQLException
	 */
	public List<Jobangebot> searchForAbschluss(String aAbschluss, String aExpertise) throws SQLException {
		List<Jobangebot> result = new LinkedList<>();
		// fetch the hierarchy of given graduation
		int hierarchy = new AbschlussDAO().getHierarchy(aAbschluss);
		// escape controlchars and replace * with % (wildcard)
		String expertise = aExpertise.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![")
				.replace("*", "%");
		String sql = "SELECT jobangebot.JID FROM jobangebot INNER JOIN expertise ON jobangebot.EID = expertise.EID "
				+ "INNER JOIN graduation ON jobangebot.GID = graduation.GID "
				+ "WHERE expertise.expertise LIKE ? AND graduation.hierarchy <= ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setString(1, expertise);
			preparedStatement.setInt(2, hierarchy);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// get the jids
					int jid = resultSet.getInt("jobangebot.JID");
					// get the profil with given id
					Jobangebot tempJobangebot = new JobangebotDAO().getJobangebot(jid);
					// add to result if not null
					if (tempJobangebot != null) {
						result.add(tempJobangebot);
					}
				}
			}
		}
		return result;
	}

	/**
	 * @param aGehalt
	 * @return a List of {@link model.Jobangebot Jobangeboten} with a higher salary
	 *         than aGehalt
	 * @throws SQLException
	 */
	public List<Jobangebot> searchForGehalt(int aGehalt) throws SQLException {
		List<Jobangebot> result = new LinkedList<>();
		String sql = "SELECT jobangebot.JID FROM jobangebot WHERE jobangebot.salary >= ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, aGehalt);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// get the jids
					int jid = resultSet.getInt("jobangebot.JID");
					// get the profil with given id
					Jobangebot tempJobangebot = new JobangebotDAO().getJobangebot(jid);
					// add to result if not null
					if (tempJobangebot != null) {
						result.add(tempJobangebot);
					}
				}
			}
		}
		return result;
	}

	/**
	 * @param min
	 * @param max
	 * @return a List of {@link model.Jobangebot Jobangeboten} with an
	 *         {@link model.Unternehmensprofil Unternehmensprofil} with employees in
	 *         range
	 * @throws SQLException
	 */
	public List<Jobangebot> searchForMitarbeiter(int min, int max) throws SQLException {
		List<Jobangebot> result = new LinkedList<>();
		String sql = "SELECT jobangebot.JID FROM jobangebot "
				+ "INNER JOIN unternehmensprofil ON jobangebot.UID = unternehmensprofil.UID "
				+ "WHERE unternehmensprofil.employees >= ? AND unternehmensprofil.employees <= ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, min);
			preparedStatement.setInt(2, max);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// get the jids
					int jid = resultSet.getInt("jobangebot.JID");
					// get the profil with given id
					Jobangebot tempJobangebot = new JobangebotDAO().getJobangebot(jid);
					// add to result if not null
					if (tempJobangebot != null) {
						result.add(tempJobangebot);
					}
				}
			}
		}
		return result;
	}
}
