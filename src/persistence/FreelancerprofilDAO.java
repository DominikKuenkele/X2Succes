package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Freelancerprofil;
import model.Nutzer;
import util.exception.ValidateConstrArgsException;

/**
 * @author domin
 *
 */
public class FreelancerprofilDAO {
	private Connection connect = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	private void open() throws SQLException {
		DBConnection dbconnection = new DBConnection();
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
	 * @param freelancer
	 * @return the generated ID of the new {@link model.Freelancerprofil}
	 * @throws SQLException
	 */
	public int addFreelancerprofil(Freelancerprofil freelancer) throws SQLException {
		int nutzerId = freelancer.getNutzer().getId();
		int fid = -1;
		try {
			open();

			preparedStatement = connect
					.prepareStatement("INSERT INTO Freelancerprofil values (default, ?, ?, ?, ?, ?, ?, ?, ?)");
			preparedStatement.setInt(1, nutzerId);
			int gid = new AbschlussDAO().getAbschluss(freelancer.getAbschluss());
			preparedStatement.setInt(2, gid);
			int bid = new BrancheDAO().getBranche(freelancer.getBranche());
			preparedStatement.setInt(3, bid);
			preparedStatement.setString(4, freelancer.getBeschreibung());
			Gson gson = new GsonBuilder().create();
			String skillsJSON = gson.toJson(freelancer.getSkills());
			preparedStatement.setString(5, skillsJSON);
			preparedStatement.setString(6, freelancer.getLebenslauf());
			preparedStatement.setString(7, freelancer.getBenefits());

			preparedStatement.executeUpdate();

			preparedStatement = connect.prepareStatement("SELECT LAST_INSERT_ID()");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				fid = resultSet.getInt("last_insert_id()");
			}

			List<String> sprachen = freelancer.getSprachen();
			for (int i = 0; i < sprachen.size(); i++) {
				int sid = new SpracheDAO().getSID(sprachen.get(i));

				preparedStatement = connect.prepareStatement("INSERT INTO SprachenzuordnungFP values (?, ?)");
				preparedStatement.setInt(1, fid);
				preparedStatement.setInt(2, sid);

				preparedStatement.executeUpdate();
			}
		} finally {
			close();
		}
		return fid;
	}

	/**
	 * @param fid
	 * @return a {@link model.Freelancerprofil} with given ID
	 * @throws SQLException
	 */
	public Freelancerprofil getFreelancerprofil(int fid) throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement(
					"SELECT FID, NID, graduation.graduation, branche.branche, description, skills, career, benefits "
							+ "FROM freelancerprofil  "
							+ "INNER JOIN graduation ON freelancerprofil.GID=graduation.GID "
							+ "INNER JOIN branche ON freelancerprofil.BID = branche.BID " + "WHERE FID = ?");
			preparedStatement.setInt(1, fid);

			resultSet = preparedStatement.executeQuery();
			return getFreelancerprofilFromResultSet(resultSet).get(0);
		} finally {
			close();
		}
	}

	/**
	 * @return a List of all {@link model.Freelancerprofil Freelancerprofile} in the
	 *         database
	 * @throws SQLException
	 */
	public List<Freelancerprofil> getAllFreelancer() throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement(
					"SELECT FID, NID, graduation.graduation, branche.branche, description, skills, career, benefits "
							+ "FROM Freelancerprofil  "
							+ "INNER JOIN graduation ON freelancerprofil.GID=graduation.GID "
							+ "INNER JOIN branche ON freelancerprofil.GID=branche.BID");
			resultSet = preparedStatement.executeQuery();
			return getFreelancerprofilFromResultSet(resultSet);
		} finally {
			close();
		}
	}

	/**
	 * @param fid
	 * @throws SQLException
	 */
	public void deleteFreelancerprofil(int fid) throws SQLException {
		try {
			open();
			preparedStatement = connect.prepareStatement("DELETE FROM Freelancer WHERE fid = ?");
			preparedStatement.setInt(1, fid);

			preparedStatement.executeUpdate();
		} finally {
			close();
		}
	}

	/**
	 * @param freelancerprofil
	 * @throws SQLException
	 */
	public void changeFreelancerprofil(Freelancerprofil freelancerprofil) throws SQLException {
		try {
			open();
			List<String> sprachen = freelancerprofil.getSprachen();

			preparedStatement = connect.prepareStatement("DELETE FROM sprachenzuordnungFP WHERE FID = ?");
			preparedStatement.setInt(1, freelancerprofil.getFID());
			preparedStatement.executeUpdate();

			this.preparedStatement = this.connect.prepareStatement(
					"UPDATE freelancerprofil SET NID = ?, GID = ?, BID = ?, description = ?, skills = ?, "
							+ "career = ?, benefits = ? WHERE FID = ?");

			int nid = freelancerprofil.getNutzer().getId();
			this.preparedStatement.setInt(1, nid);
			int gid = new AbschlussDAO().getAbschluss(freelancerprofil.getAbschluss());
			this.preparedStatement.setInt(2, gid);
			int bid = new BrancheDAO().getBranche(freelancerprofil.getBranche());
			this.preparedStatement.setInt(3, bid);
			this.preparedStatement.setString(4, freelancerprofil.getBeschreibung());
			Gson gson = new GsonBuilder().create();
			String skillsJSON = gson.toJson(freelancerprofil.getSkills());
			preparedStatement.setString(5, skillsJSON);
			this.preparedStatement.setString(6, freelancerprofil.getLebenslauf());
			this.preparedStatement.setString(7, freelancerprofil.getBenefits());
			this.preparedStatement.setInt(8, freelancerprofil.getFID());
			this.preparedStatement.executeUpdate();

			for (int i = 0; i < sprachen.size(); i++) {
				int sid = new SpracheDAO().getSID(sprachen.get(i));

				preparedStatement = connect.prepareStatement("INSERT INTO SprachenzuordnungFP values (?, ?)");
				preparedStatement.setInt(1, freelancerprofil.getFID());
				preparedStatement.setInt(2, sid);

				preparedStatement.executeUpdate();
			}
		} finally {
			close();
		}
	}

	private List<Freelancerprofil> getFreelancerprofilFromResultSet(ResultSet resultSet) throws SQLException {
		List<Freelancerprofil> result = new LinkedList<>();
		while (resultSet.next()) {
			int freelancerId = resultSet.getInt("FID");
			int nutzerId = resultSet.getInt("NID");
			Nutzer nutzer = new NutzerDAO().getNutzer(nutzerId);
			String graduation = resultSet.getString("graduation.graduation");
			String branche = resultSet.getString("branche.branche");
			String description = resultSet.getString("description");
			String skillsJSON = resultSet.getString("skills");
			Gson gson = new GsonBuilder().create();
			String[] skills = gson.fromJson(skillsJSON, String[].class);
			String career = resultSet.getString("career");
			String benefits = resultSet.getString("benefits");
			List<String> sprachen = getLanguageInFreelancerprofil(freelancerId);

			try {
				Freelancerprofil tempFreelancer = new Freelancerprofil(graduation, branche, description, skills, career,
						benefits, sprachen, nutzer);
				tempFreelancer.setId(freelancerId);
				result.add(tempFreelancer);
			} catch (ValidateConstrArgsException e) {
				throw new SQLException("Datenbank ist inkonsistent!", e);
			}
		}
		return result;
	}

	private List<String> getLanguageInFreelancerprofil(int fid) throws SQLException {
		List<String> result = new LinkedList<>();
		ResultSet resultSetSprache = null;
		try {
			open();
			preparedStatement = connect.prepareStatement("SELECT SID FROM SprachenzuordnungFP WHERE FID = ?");
			preparedStatement.setInt(1, fid);
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

	public List<Freelancerprofil> searchForAbschlussTest(String aAbschluss, String aBranche) throws SQLException {
		List<Freelancerprofil> result = new LinkedList<>();

		try {
			open();

			String branche = aBranche.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![")
					.replace("*", "%");

			int hierarchy = new AbschlussDAO().getHierarchy(aAbschluss);

			preparedStatement = connect.prepareStatement("SELECT freelancerprofil.FID FROM freelancerprofil "
					+ "INNER JOIN branche ON freelancerprofil.BID = branche.BID "
					+ "INNER JOIN graduation ON freelancerprofil.GID = graduation.GID "
					+ "WHERE branche.branche LIKE ? AND graduation.hierarchy >= ?");
			preparedStatement.setString(1, branche);
			preparedStatement.setInt(2, hierarchy);

			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int fid = resultSet.getInt("freelancerprofil.FID");
				result.add(new FreelancerprofilDAO().getFreelancerprofil(fid));
			}
		} finally {
			close();
		}
		return result;
	}

	/**
	 * @param aGehalt
	 * @return a List of {@link model.Freelancerprofil Freelancerprofilen} with a
	 *         lower salary than aGehalt
	 * @throws SQLException
	 */
	public List<Freelancerprofil> searchForGehaltTest(int aGehalt) throws SQLException {
		List<Freelancerprofil> result = new LinkedList<>();
		try {
			open();
			preparedStatement = connect
					.prepareStatement("SELECT freelancer.FID FROM freelancer WHERE freelancer.salary <= ?");
			preparedStatement.setInt(1, aGehalt);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int fid = resultSet.getInt("freelnacer.FID");
				result.add(new FreelancerprofilDAO().getFreelancerprofil(fid));
			}
		} finally {
			close();
		}
		return result;
	}
}
