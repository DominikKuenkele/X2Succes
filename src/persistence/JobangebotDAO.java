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
import util.exception.ValidateConstrArgsException;

/**
 * @author domin
 *
 */
public class JobangebotDAO {
	private DataSource datasource = DataSource.getInstance();

	/**
	 * @param jobangebot
	 * @return the generated ID of the new {@link model.Jobangebot}
	 * @throws SQLException
	 */
	public int addJobangebot(Jobangebot jobangebot) throws SQLException {
		int unternehmensId = jobangebot.getUnternehmensprofil().getUid();
		int jid = -1;

		try (Connection connect = datasource.getConnection()) {
			String sql = "INSERT INTO Jobangebot values (default, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				preparedStatement.setInt(1, unternehmensId);
				int gid = new AbschlussDAO().getAbschluss(jobangebot.getAbschluss());
				preparedStatement.setInt(2, gid);
				int eid = new ExpertiseDAO().getExpertise(jobangebot.getFachgebiet());
				preparedStatement.setInt(3, eid);
				preparedStatement.setString(4, jobangebot.getJobTitel());
				preparedStatement.setString(5, jobangebot.getBeschreibung());
				preparedStatement.setObject(6, jobangebot.getFrist());
				preparedStatement.setInt(7, jobangebot.getGehalt());
				preparedStatement.setInt(8, jobangebot.getWochenstunden());

				preparedStatement.executeUpdate();
			}

			sql = "SELECT LAST_INSERT_ID()";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						jid = resultSet.getInt("last_insert_id()");
					}
				}
			}

			sql = "INSERT INTO SprachenzuordnungJA values (?, ?)";

			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				List<String> sprachen = jobangebot.getSprachen();
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
		String sql = "SELECT JID, UID, graduation.graduation, expertise.expertise, "
				+ "jobTitle, description, deadline, salary, weeklyHours FROM jobangebot "
				+ "INNER JOIN graduation ON jobangebot.GID=graduation.GID "
				+ "INNER JOIN expertise ON jobangebot.EID = expertise.EID WHERE JID = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, jid);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				List<Jobangebot> resultList = getJobangebotFromResultSet(resultSet);
				if (resultList.size() > 0) {
					return resultList.get(0);
				} else {
					return null;
				}
			}
		}
	}

	/**
	 * @return a List of all {@link model.Jobangebot Jobangebote} in the database
	 * @throws SQLException
	 */
	public List<Jobangebot> getAllJobangebote() throws SQLException {
		String sql = "SELECT JID, UID, graduation.graduation, expertise.expertise, "
				+ "jobTitle, description, deadline, salary, weeklyHours FROM jobangebot "
				+ "INNER JOIN graduation ON jobangebot.GID = graduation.GID "
				+ "INNER JOIN expertise ON jobangebot.EID = expertise.EID";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return getJobangebotFromResultSet(resultSet);
			}
		}
	}

	/**
	 * @param jid
	 * @throws SQLException
	 */
	public void deleteJobangebot(int jid) throws SQLException {
		String sql = "DELETE FROM Jobangebot WHERE JID = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, jid);

			preparedStatement.executeUpdate();
		}
	}

	/**
	 * @param jobangebot
	 * @throws SQLException
	 */
	public void changeJobangebot(Jobangebot jobangebot) throws SQLException {
		List<String> sprachen = jobangebot.getSprachen();

		try (Connection connect = datasource.getConnection()) {
			String sql = "DELETE FROM sprachenzuordnungJA WHERE JID = ?";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				preparedStatement.setInt(1, jobangebot.getJID());

				preparedStatement.executeUpdate();
			}

			sql = "UPDATE jobangebot SET GID = ?, EID = ?, jobTitle = ?, description = ?, deadline = ?, "
					+ "salary = ?, weeklyHours = ? WHERE JID = ?";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				int gid = new AbschlussDAO().getAbschluss(jobangebot.getAbschluss());
				preparedStatement.setInt(1, gid);
				int eid = new ExpertiseDAO().getExpertise(jobangebot.getFachgebiet());
				preparedStatement.setInt(2, eid);
				preparedStatement.setString(3, jobangebot.getJobTitel());
				preparedStatement.setString(4, jobangebot.getBeschreibung());
				preparedStatement.setObject(5, jobangebot.getFrist());
				preparedStatement.setInt(6, jobangebot.getGehalt());
				preparedStatement.setInt(7, jobangebot.getWochenstunden());
				preparedStatement.setInt(8, jobangebot.getJID());

				preparedStatement.executeUpdate();

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

	private List<Jobangebot> getJobangebotFromResultSet(ResultSet resultSet) throws SQLException {
		List<Jobangebot> result = new LinkedList<>();
		while (resultSet.next()) {
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
				Jobangebot tempJobangebot = new Jobangebot(jobangebotsId, graduation, expertise, sprachen, jobTitle,
						description, deadline, salary, weeklyHours, unternehmen);
				result.add(tempJobangebot);

			} catch (ValidateConstrArgsException e) {
				throw new SQLException("Datenbank ist inkonsistent!", e);
			}
		}
		return result;
	}

	private List<String> getLanguageInJobangebot(int jid) throws SQLException {
		List<String> result = new LinkedList<>();
		String sql = "SELECT SID FROM SprachenzuordnungJA WHERE JID = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, jid);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					int sid = resultSet.getInt("SID");
					String sprache = new SpracheDAO().getSprache(sid);
					result.add(sprache);
				}
			}
		}
		return result;
	}

	public List<Jobangebot> searchForName(String aName) throws SQLException {
		List<Jobangebot> result = new LinkedList<>();
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
					int jid = resultSet.getInt("jobangebot.JID");
					result.add(new JobangebotDAO().getJobangebot(jid));
				}
			}
		}
		return result;
	}

	public List<Jobangebot> searchForBranche(String aBranche) throws SQLException {
		List<Jobangebot> result = new LinkedList<>();
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
					int jid = resultSet.getInt("jobangebot.JID");
					result.add(new JobangebotDAO().getJobangebot(jid));
				}
			}
		}
		return result;
	}

	public List<Jobangebot> searchForAbschluss(String aAbschluss, String aExpertise) throws SQLException {
		List<Jobangebot> result = new LinkedList<>();
		int hierarchy = new AbschlussDAO().getHierarchy(aAbschluss);
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
					int jid = resultSet.getInt("jobangebot.JID");
					result.add(new JobangebotDAO().getJobangebot(jid));
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
					int jid = resultSet.getInt("jobangebot.JID");
					result.add(new JobangebotDAO().getJobangebot(jid));
				}
			}
		}
		return result;
	}

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
					int jid = resultSet.getInt("jobangebot.JID");
					result.add(new JobangebotDAO().getJobangebot(jid));
				}
			}
		}
		return result;
	}
}
