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
	private Connection connect = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	/**
	 * @throws SQLException
	 */
	private void open() throws SQLException {
		DataSource dbconnection = new DataSource();
		connect = dbconnection.getConnection();
	}

	/**
	 * @throws SQLException
	 */
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
	 * @param jobangebot
	 * @return the generated ID of the new {@link model.Jobangebot}
	 * @throws SQLException
	 */
	public int addJobangebot(Jobangebot jobangebot) throws SQLException {
		int unternehmensId = jobangebot.getUnternehmensprofil().getId();
		int jid = -1;
		try {
			open();
			preparedStatement = connect
					.prepareStatement("INSERT INTO Jobangebot values (default, ?, ?, ?, ?, ?, ?, ?, ?)");
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

			preparedStatement = connect.prepareStatement("SELECT LAST_INSERT_ID()");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				jid = resultSet.getInt("last_insert_id()");
			}

			List<String> sprachen = jobangebot.getSprachen();

			for (int i = 0; i < sprachen.size(); i++) {
				int sid = new SpracheDAO().getSID(sprachen.get(i));

				preparedStatement = connect.prepareStatement("INSERT INTO SprachenzuordnungJA values (?, ?)");
				preparedStatement.setInt(1, jid);
				preparedStatement.setInt(2, sid);

				preparedStatement.executeUpdate();
			}
		} finally {
			close();
		}
		return jid;
	}

	/**
	 * @param jid
	 * @return a {@link model.Jobangebot} with given ID
	 * @throws SQLException
	 */
	public Jobangebot getJobangebot(int jid) throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement("SELECT JID, UID, graduation.graduation, expertise.expertise, "
					+ "jobTitle, description, deadline, salary, weeklyHours FROM jobangebot "
					+ "INNER JOIN graduation ON jobangebot.GID=graduation.GID "
					+ "INNER JOIN expertise ON jobangebot.EID = expertise.EID WHERE JID = ?");
			preparedStatement.setInt(1, jid);
			resultSet = preparedStatement.executeQuery();
			return getJobangebotFromResultSet(resultSet).get(0);
		} finally {
			close();
		}
	}

	/**
	 * @return a List of all {@link model.Jobangebot Jobangebote} in the database
	 * @throws SQLException
	 */
	public List<Jobangebot> getAllJobangebote() throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement("SELECT JID, UID, graduation.graduation, expertise.expertise, "
					+ "jobTitle, description, deadline, salary, weeklyHours FROM jobangebot "
					+ "INNER JOIN graduation ON jobangebot.GID = graduation.GID "
					+ "INNER JOIN expertise ON jobangebot.EID = expertise.EID");
			resultSet = preparedStatement.executeQuery();
			return getJobangebotFromResultSet(resultSet);
		} finally {
			close();
		}
	}

	/**
	 * @param jid
	 * @throws SQLException
	 */
	public void deleteJobangebot(int jid) throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement("DELETE FROM Jobangebot WHERE JID = ?");
			preparedStatement.setInt(1, jid);

			preparedStatement.executeUpdate();
		} finally {
			close();
		}
	}

	/**
	 * @param jobangebot
	 * @throws SQLException
	 */
	public void changeJobangebot(Jobangebot jobangebot) throws SQLException {
		try {
			open();
			List<String> sprachen = jobangebot.getSprachen();

			preparedStatement = connect.prepareStatement("DELETE FROM sprachenzuordnungJA WHERE JID = ?");
			preparedStatement.setInt(1, jobangebot.getJID());
			preparedStatement.executeUpdate();

			this.preparedStatement = this.connect.prepareStatement(
					"UPDATE jobangebot SET GID = ?, EID = ?, jobTitle = ?, description = ?, deadline = ?, "
							+ "salary = ?, weeklyHours = ? WHERE JID = ?");

			int gid = new AbschlussDAO().getAbschluss(jobangebot.getAbschluss());
			this.preparedStatement.setInt(1, gid);
			int eid = new ExpertiseDAO().getExpertise(jobangebot.getFachgebiet());
			this.preparedStatement.setInt(2, eid);
			this.preparedStatement.setString(3, jobangebot.getJobTitel());
			this.preparedStatement.setString(4, jobangebot.getBeschreibung());
			this.preparedStatement.setObject(5, jobangebot.getFrist());
			this.preparedStatement.setInt(6, jobangebot.getGehalt());
			this.preparedStatement.setInt(7, jobangebot.getWochenstunden());
			this.preparedStatement.setInt(8, jobangebot.getJID());
			this.preparedStatement.executeUpdate();

			for (int i = 0; i < sprachen.size(); i++) {
				int sid = new SpracheDAO().getSID(sprachen.get(i));

				preparedStatement = connect.prepareStatement("INSERT INTO SprachenzuordnungJA values (?, ?)");
				preparedStatement.setInt(1, jobangebot.getJID());
				preparedStatement.setInt(2, sid);

				preparedStatement.executeUpdate();
			}
		} finally {
			close();
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
				Jobangebot tempJobangebot = new Jobangebot(graduation, expertise, sprachen, jobTitle, description,
						deadline, salary, weeklyHours, unternehmen);
				tempJobangebot.setId(jobangebotsId);
				result.add(tempJobangebot);

			} catch (ValidateConstrArgsException e) {
				throw new SQLException("Datenbank ist inkonsistent!", e);
			}
		}
		return result;
	}

	private List<String> getLanguageInJobangebot(int jid) throws SQLException {
		List<String> result = new LinkedList<>();
		ResultSet resultSetSprache = null;
		try {
			preparedStatement = connect.prepareStatement("SELECT SID FROM SprachenzuordnungJA WHERE JID = ?");
			preparedStatement.setInt(1, jid);
			resultSetSprache = preparedStatement.executeQuery();

			while (resultSetSprache.next()) {
				int sid = resultSetSprache.getInt("SID");

				String sprache = new SpracheDAO().getSprache(sid);
				result.add(sprache);
			}
		} finally {
			resultSetSprache.close();
		}
		return result;
	}

	public List<Jobangebot> searchForName(String aName) throws SQLException {
		List<Jobangebot> result = new LinkedList<>();
		try {
			open();
			String name = aName.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![").replace("*",
					"%");
			preparedStatement = connect.prepareStatement("SELECT jobangebot.JID FROM jobangebot "
					+ "INNER JOIN unternehmensprofil ON jobangebot.UID=unternehmensprofil.UID "
					+ "WHERE unternehmensprofil.name LIKE ?");
			preparedStatement.setString(1, name);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int jid = resultSet.getInt("jobangebot.JID");
				result.add(new JobangebotDAO().getJobangebot(jid));
			}
		} finally {
			close();
		}
		return result;
	}

	public List<Jobangebot> searchForBranche(String aBranche) throws SQLException {
		List<Jobangebot> result = new LinkedList<>();
		try {
			open();
			String branche = aBranche.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![")
					.replace("*", "%");
			preparedStatement = connect.prepareStatement("SELECT jobangebot.JID FROM jobangebot "
					+ "INNER JOIN unternehmensprofil ON jobangebot.UID = unternehmensprofil.UID "
					+ "INNER JOIN branche ON unternehmensprofil.BID = branche.BID WHERE branche.branche LIKE ?");
			preparedStatement.setString(1, branche);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int jid = resultSet.getInt("jobangebot.JID");
				result.add(new JobangebotDAO().getJobangebot(jid));
			}
		} finally {
			close();
		}
		return result;
	}

	public List<Jobangebot> searchForAbschluss(String aAbschluss, String aExpertise) throws SQLException {
		List<Jobangebot> result = new LinkedList<>();
		try {
			open();

			String expertise = aExpertise.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![")
					.replace("*", "%");

			int hierarchy = new AbschlussDAO().getHierarchy(aAbschluss);

			preparedStatement = connect.prepareStatement(
					"SELECT jobangebot.JID FROM jobangebot INNER JOIN expertise ON jobangebot.EID = expertise.EID "
							+ "INNER JOIN graduation ON jobangebot.GID = graduation.GID "
							+ "WHERE expertise.expertise LIKE ? AND graduation.hierarchy <= ?");
			preparedStatement.setString(1, expertise);
			preparedStatement.setInt(2, hierarchy);

			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int jid = resultSet.getInt("jobangebot.JID");
				result.add(new JobangebotDAO().getJobangebot(jid));
			}
		} finally {
			close();
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
		try {
			open();
			preparedStatement = connect
					.prepareStatement("SELECT jobangebot.JID FROM jobangebot WHERE jobangebot.salary >= ?");
			preparedStatement.setInt(1, aGehalt);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int jid = resultSet.getInt("jobangebot.JID");
				result.add(new JobangebotDAO().getJobangebot(jid));
			}
		} finally {
			close();
		}
		return result;
	}

	public List<Jobangebot> searchForMitarbeiter(int min, int max) throws SQLException {
		List<Jobangebot> result = new LinkedList<>();
		try {
			open();
			preparedStatement = connect.prepareStatement("SELECT jobangebot.JID FROM jobangebot "
					+ "INNER JOIN unternehmensprofil ON jobangebot.UID = unternehmensprofil.UID "
					+ "WHERE unternehmensprofil.employees >= ? AND unternehmensprofil.employees <= ?");
			preparedStatement.setInt(1, min);
			preparedStatement.setInt(2, max);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int jid = resultSet.getInt("jobangebot.JID");
				result.add(new JobangebotDAO().getJobangebot(jid));
			}
		} finally {
			close();
		}
		return result;
	}

	// public List<Jobangebot> searchForName(String aName) {
	// List<Jobangebot> list = new LinkedList<>();
	// try {
	// Jobangebot[] jA = new Jobangebot[4];
	// for (int i = 0; i < 4; i++) {
	// jA[i] = new Jobangebot("sdf", "sdf", new LinkedList<String>(), "sdf",
	// LocalDate.of(1999, 12, 1), 1, 3,
	// null);
	// jA[i].setId(i * 2);
	// }
	// list = Arrays.asList(jA);
	// } catch (ValidateConstrArgsException e) {
	// e.printStackTrace();
	// }
	// return list;
	// }
	//
	// public List<Jobangebot> searchForAbschluss(String abschluss, String branche)
	// {
	// List<Jobangebot> list = new LinkedList<>();
	// try {
	// Jobangebot[] jA = new Jobangebot[4];
	// for (int i = 0; i < 4; i++) {
	// jA[i] = new Jobangebot("sdf", "sdf", new LinkedList<String>(), "sdf",
	// LocalDate.of(1999, 12, 1), 1, 3,
	// null);
	// jA[i].setId(i * 2);
	// }
	// list = Arrays.asList(jA);
	// } catch (ValidateConstrArgsException e) {
	// e.printStackTrace();
	// }
	// return list;
	// }
	//
	// public List<Jobangebot> searchForGehalt(int gehalt) {
	// List<Jobangebot> list = new LinkedList<>();
	// try {
	// Jobangebot[] jA = new Jobangebot[4];
	// for (int i = 0; i < 4; i++) {
	// jA[i] = new Jobangebot("dg", "sdf", new LinkedList<String>(), "sdf",
	// LocalDate.of(1999, 12, 1), 1, 3,
	// null);
	// jA[i].setId(i * 3);
	// }
	// list = Arrays.asList(jA);
	// } catch (ValidateConstrArgsException e) {
	// e.printStackTrace();
	// }
	// return list;
	// }
	//
	// public List<Jobangebot> searchForMitarbeiter(int min, int max) {
	// List<Jobangebot> list = new LinkedList<>();
	// try {
	// Jobangebot[] jA = new Jobangebot[4];
	// for (int i = 0; i < 4; i++) {
	// jA[i] = new Jobangebot(Integer.toString(i), "sdf", new LinkedList<String>(),
	// "sdf",
	// LocalDate.of(1999, 12, 1), 1, 3, null);
	// jA[i].setId(4 - i);
	// }
	// list = Arrays.asList(jA);
	// } catch (ValidateConstrArgsException e) {
	// e.printStackTrace();
	// }
	// return list;
	// }
}
