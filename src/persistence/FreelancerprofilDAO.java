package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Freelancerprofil;
import model.Nutzer;
import util.exception.ValidateArgsException;

/**
 * Class is a DAO for the table 'freelancerprofil'
 * 
 * @author domin
 *
 */
public class FreelancerprofilDAO {
	private DataSource datasource = DataSource.getInstance();

	/**
	 * @param freelancer
	 * @return the generated ID of the new {@link model.Freelancerprofil}
	 * @throws SQLException
	 */
	public int addFreelancerprofil(Freelancerprofil freelancer) throws SQLException {
		int nutzerId = freelancer.getNutzer().getNID();
		int fid = -1;

		try (Connection connect = datasource.getConnection()) {
			String sql = "INSERT INTO Freelancerprofil values (default, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				preparedStatement.setInt(1, nutzerId);
				int gid = new AbschlussDAO().getAbschluss(freelancer.getAbschluss());
				preparedStatement.setInt(2, gid);
				int eid = new ExpertiseDAO().getExpertise(freelancer.getFachgebiet());
				preparedStatement.setInt(3, eid);
				preparedStatement.setString(4, freelancer.getBeschreibung());
				Gson gson = new GsonBuilder().create();
				String skillsJSON = gson.toJson(freelancer.getSkills());
				preparedStatement.setString(5, skillsJSON);
				preparedStatement.setString(6, freelancer.getLebenslauf());

				preparedStatement.executeUpdate();
			}

			sql = "SELECT LAST_INSERT_ID()";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						fid = resultSet.getInt("last_insert_id()");
					}
				}
			}

			sql = "INSERT INTO SprachenzuordnungFP values (?, ?)";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				List<String> sprachen = freelancer.getSprachen();
				for (int i = 0; i < sprachen.size(); i++) {
					int sid = new SpracheDAO().getSID(sprachen.get(i));

					preparedStatement.setInt(1, fid);
					preparedStatement.setInt(2, sid);

					preparedStatement.executeUpdate();
				}
			}
		}
		return fid;
	}

	/**
	 * @param nid
	 * @return {@link model.Freelancerprofil} with given {@link model.Nutzer}
	 * @throws SQLException
	 */
	public Freelancerprofil getFreelancerprofilByNutzer(int nid) throws SQLException {
		String sql = "SELECT FID, NID, graduation.graduation, expertise.expertise, description, skills, career "
				+ "FROM freelancerprofil " + "INNER JOIN graduation ON freelancerprofil.GID=graduation.GID "
				+ "INNER JOIN expertise ON freelancerprofil.EID = expertise.EID WHERE NID = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, nid);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				List<Freelancerprofil> resultList = getFreelancerprofilFromResultSet(resultSet);
				if (resultList.size() > 0) {
					return resultList.get(0);
				} else {
					return null;
				}
			}
		}
	}

	/**
	 * @param fid
	 * @return a {@link model.Freelancerprofil} with given ID
	 * @throws SQLException
	 */
	public Freelancerprofil getFreelancerprofil(int fid) throws SQLException {
		String sql = "SELECT FID, NID, graduation.graduation, expertise.expertise, description, skills, career "
				+ "FROM freelancerprofil  INNER JOIN graduation ON freelancerprofil.GID=graduation.GID "
				+ "INNER JOIN expertise ON freelancerprofil.EID = expertise.EID WHERE FID = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, fid);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				List<Freelancerprofil> tempList = getFreelancerprofilFromResultSet(resultSet);
				if (tempList.size() > 0) {
					return tempList.get(0);
				} else {
					return null;
				}
			}
		}
	}

	/**
	 * @return a List of all {@link model.Freelancerprofil Freelancerprofile} in the
	 *         database
	 * @throws SQLException
	 */
	public List<Freelancerprofil> getAllFreelancer() throws SQLException {
		String sql = "SELECT FID, NID, graduation.graduation, expertise.expertise, description, skills, career "
				+ "FROM Freelancerprofil  " + "INNER JOIN graduation ON freelancerprofil.GID=graduation.GID "
				+ "INNER JOIN expertise ON freelancerprofil.EID=expertise.EID";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return getFreelancerprofilFromResultSet(resultSet);
			}
		}
	}

	/**
	 * @param fid
	 * @throws SQLException
	 */
	public void deleteFreelancerprofil(int fid) throws SQLException {
		String sql = "DELETE FROM freelancerprofil WHERE fid = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, fid);

			preparedStatement.executeUpdate();
		}
	}

	/**
	 * changes an existing freelancerprofil in the database
	 * 
	 * @param freelancerprofil
	 * @throws SQLException
	 */
	public void changeFreelancerprofil(Freelancerprofil freelancerprofil) throws SQLException {
		List<String> sprachen = freelancerprofil.getSprachen();

		try (Connection connect = datasource.getConnection()) {
			String sql = "DELETE FROM sprachenzuordnungFP WHERE FID = ?";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				preparedStatement.setInt(1, freelancerprofil.getFID());

				preparedStatement.executeUpdate();
			}

			sql = "UPDATE freelancerprofil SET NID = ?, GID = ?, EID = ?, description = ?, skills = ?, "
					+ "career = ? WHERE FID = ?";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				int nid = freelancerprofil.getNutzer().getNID();
				preparedStatement.setInt(1, nid);
				int gid = new AbschlussDAO().getAbschluss(freelancerprofil.getAbschluss());
				preparedStatement.setInt(2, gid);
				int eid = new ExpertiseDAO().getExpertise(freelancerprofil.getFachgebiet());
				preparedStatement.setInt(3, eid);
				preparedStatement.setString(4, freelancerprofil.getBeschreibung());
				Gson gson = new GsonBuilder().create();
				String skillsJSON = gson.toJson(freelancerprofil.getSkills());
				preparedStatement.setString(5, skillsJSON);
				preparedStatement.setString(6, freelancerprofil.getLebenslauf());
				preparedStatement.setInt(7, freelancerprofil.getFID());

				preparedStatement.executeUpdate();

				for (int i = 0; i < sprachen.size(); i++) {
					int sid = new SpracheDAO().getSID(sprachen.get(i));

					String sql2 = "INSERT INTO SprachenzuordnungFP values (?, ?)";
					try (PreparedStatement preparedStatement2 = connect.prepareStatement(sql2)) {
						preparedStatement2.setInt(1, freelancerprofil.getFID());
						preparedStatement2.setInt(2, sid);

						preparedStatement2.executeUpdate();
					}
				}
			}
		}

	}

	/**
	 * processes fetched data from database to a {@link model.Freelancerprofil
	 * Freelancerprofil}
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private List<Freelancerprofil> getFreelancerprofilFromResultSet(ResultSet resultSet) throws SQLException {
		List<Freelancerprofil> result = new LinkedList<>();
		while (resultSet.next()) {
			int freelancerId = resultSet.getInt("FID");
			int nutzerId = resultSet.getInt("NID");
			Nutzer nutzer = new NutzerDAO().getNutzer(nutzerId);
			String graduation = resultSet.getString("graduation.graduation");
			String expertise = resultSet.getString("expertise.expertise");
			String description = resultSet.getString("description");
			String skillsJSON = resultSet.getString("skills");
			Gson gson = new GsonBuilder().create();
			String[] skills = gson.fromJson(skillsJSON, String[].class);
			String career = resultSet.getString("career");
			List<String> sprachen = getLanguageInFreelancerprofil(freelancerId);

			try {
				Freelancerprofil tempFreelancer = new Freelancerprofil(freelancerId, graduation, expertise, description,
						skills, career, sprachen, nutzer);
				result.add(tempFreelancer);
			} catch (ValidateArgsException e) {
				throw new SQLException("Datenbank ist inkonsistent!", e);
			}
		}
		return result;
	}

	private List<String> getLanguageInFreelancerprofil(int fid) throws SQLException {
		List<String> result = new LinkedList<>();
		String sql = "SELECT SID FROM SprachenzuordnungFP WHERE FID = ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setInt(1, fid);
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

	public List<Freelancerprofil> searchForAbschluss(String aAbschluss, String aExpertise) throws SQLException {
		List<Freelancerprofil> result = new LinkedList<>();
		String expertise = aExpertise.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![")
				.replace("*", "%");
		int hierarchy = new AbschlussDAO().getHierarchy(aAbschluss);
		String sql = "SELECT freelancerprofil.FID FROM freelancerprofil "
				+ "INNER JOIN expertise ON freelancerprofil.EID = expertise.EID "
				+ "INNER JOIN graduation ON freelancerprofil.GID = graduation.GID "
				+ "WHERE expertise.expertise LIKE ? AND graduation.hierarchy >= ?";

		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setString(1, expertise);
			preparedStatement.setInt(2, hierarchy);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					int fid = resultSet.getInt("freelancerprofil.FID");
					Freelancerprofil tempFreelancer = new FreelancerprofilDAO().getFreelancerprofil(fid);
					if (tempFreelancer != null) {
						result.add(tempFreelancer);
					}
				}
			}
		}
		return result;
	}

	public List<Freelancerprofil> searchForName(String aName) throws SQLException {
		List<Freelancerprofil> result = new LinkedList<>();
		List<String> nameTypes = new LinkedList<>();
		nameTypes.add("firstName");
		nameTypes.add("lastName");
		String fullName = aName.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![").replace("*",
				"%");
		String splitName[] = fullName.split(" ");
		for (String nameType : nameTypes) {
			for (String name : splitName) {
				String sql = "SELECT freelancerprofil.FID FROM freelancerprofil "
						+ "INNER JOIN nutzer ON freelancerprofil.NID = nutzer.NID WHERE nutzer." + nameType + " LIKE ?";
				try (Connection connect = datasource.getConnection();
						PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
					preparedStatement.setString(1, name);
					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						while (resultSet.next()) {
							int fid = resultSet.getInt("freelancerprofil.FID");
							Freelancerprofil tempFreelancer = new FreelancerprofilDAO().getFreelancerprofil(fid);
							if (tempFreelancer != null) {
								result.add(tempFreelancer);
							}
						}
					}
				}
			}
		}
		return result;
	}

	public List<Freelancerprofil> searchForSprache(List<String> aSprachen) throws SQLException {
		List<Freelancerprofil> result = new LinkedList<>();
		HashMap<Integer, Integer> tempList = new HashMap<>();

		for (String sprache : aSprachen) {
			String filteredSprache = sprache.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![")
					.replace("*", "%");
			String sql = "SELECT sprachenzuordnungFP.FID FROM sprachenzuordnungFP "
					+ "INNER JOIN sprachen ON sprachenzuordnungFP.SID = sprachen.SID "
					+ "WHERE sprachen.sprache LIKE ?";
			try (Connection connect = datasource.getConnection();
					PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				preparedStatement.setString(1, filteredSprache);
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						int fid = resultSet.getInt("sprachenzuordnungFP.FID");

						int prio;
						if (!tempList.containsKey(fid)) {
							prio = 1;
						} else {
							prio = tempList.get(fid) + 1;
						}
						tempList.put(fid, prio);
					}
				}
			}
		}
		for (Entry<Integer, Integer> entry : tempList.entrySet()) {
			if (entry.getValue() == aSprachen.size()) {
				Freelancerprofil tempFreelancer = new FreelancerprofilDAO().getFreelancerprofil(entry.getKey());
				if (tempFreelancer != null) {
					result.add(tempFreelancer);
				}
			}
		}
		return result;
	}
}
