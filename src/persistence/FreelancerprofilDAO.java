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

	private final String TABLE = "freelancerprofil";

	/**
	 * @param freelancer
	 * @return the generated ID of the new {@link model.Freelancerprofil}
	 * @throws SQLException
	 */
	public int addFreelancerprofil(Freelancerprofil freelancer) throws SQLException {
		// fetch userid from freelancerprofil
		int nutzerId = freelancer.getNutzer().getNID();
		int fid = -1;

		// try with connection
		try (Connection connect = datasource.getConnection()) {
			// set the sql query
			String sql = "INSERT INTO Freelancerprofil values (default, ?, ?, ?, ?, ?, ?)";
			// try with statement
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				// set params
				preparedStatement.setInt(1, nutzerId);
				// get the graduation id
				int gid = new AbschlussDAO().getAbschluss(freelancer.getAbschluss());
				preparedStatement.setInt(2, gid);
				// get the expertise id
				int eid = new ExpertiseDAO().getExpertise(freelancer.getFachgebiet());
				preparedStatement.setInt(3, eid);
				preparedStatement.setString(4, freelancer.getBeschreibung());
				// convert array to json
				Gson gson = new GsonBuilder().create();
				String skillsJSON = gson.toJson(freelancer.getSkills());
				preparedStatement.setString(5, skillsJSON);
				preparedStatement.setString(6, freelancer.getLebenslauf());

				preparedStatement.executeUpdate();
			}

			// fetch the id generated by database
			sql = "SELECT LAST_INSERT_ID()";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						fid = resultSet.getInt("last_insert_id()");
					}
				}
			}

			// insert the languages in another table
			sql = "INSERT INTO SprachenzuordnungFP values (?, ?)";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				List<String> sprachen = freelancer.getSprachen();
				// for each language from freelancer
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
		SelectSql statement = new SelectSql();

		String columns[] = { "FID", "NID", "graduation.graduation", "expertise.expertise", "description", "skills",
				"career" };
		String condition = "NID=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(nid);

		statement.select(columns).from(TABLE).innerJoin("graduation", "freelancerprofil.GID=graduation.GID")
				.innerJoin("expertise", "freelancerprofil.EID = expertise.EID").where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		return getFreelancerprofilFromResultSet(result).get(0);
	}

	/**
	 * @param fid
	 * @return a {@link model.Freelancerprofil} with given ID
	 * @throws SQLException
	 */
	public Freelancerprofil getFreelancerprofil(int fid) throws SQLException {
		SelectSql statement = new SelectSql();

		String columns[] = { "FID", "NID", "graduation.graduation", "expertise.expertise", "description", "skills",
				"career" };
		String condition = "FID=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(fid);

		statement.select(columns).from(TABLE).innerJoin("graduation", "freelancerprofil.GID=graduation.GID")
				.innerJoin("expertise", "freelancerprofil.EID = expertise.EID").where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		return getFreelancerprofilFromResultSet(result).get(0);
	}

	/**
	 * @return a List of all {@link model.Freelancerprofil Freelancerprofile} in the
	 *         database
	 * @throws SQLException
	 */
	public List<Freelancerprofil> getAllFreelancer() throws SQLException {
		SelectSql statement = new SelectSql();

		String columns[] = { "FID", "NID", "graduation.graduation", "expertise.expertise", "description", "skills",
				"career" };
		
		statement.select(columns).from(TABLE).innerJoin("graduation", "freelancerprofil.GID=graduation.GID")
				.innerJoin("expertise", "freelancerprofil.EID = expertise.EID");
		ResultSet result = statement.executeQuery();

		return getFreelancerprofilFromResultSet(result);
	}

	/**
	 * @param fid
	 * @throws SQLException
	 */
	public void deleteFreelancerprofil(int fid) throws SQLException {
		// set the sql query
		String sql = "DELETE FROM freelancerprofil WHERE fid = ?";

		// try with connection and prepared statement
		try (Connection connect = datasource.getConnection();
				PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			// set param
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
		// get all languages of freelancer
		List<String> sprachen = freelancerprofil.getSprachen();

		// try with connection
		try (Connection connect = datasource.getConnection()) {
			// delete all current languages in database for this freelancer
			String sql = "DELETE FROM sprachenzuordnungFP WHERE FID = ?";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				preparedStatement.setInt(1, freelancerprofil.getFID());

				preparedStatement.executeUpdate();
			}

			// change existing freelancerprofil
			sql = "UPDATE freelancerprofil SET NID = ?, GID = ?, EID = ?, description = ?, skills = ?, "
					+ "career = ? WHERE FID = ?";
			try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
				int nid = freelancerprofil.getNutzer().getNID();
				preparedStatement.setInt(1, nid);
				// get graduation id
				int gid = new AbschlussDAO().getAbschluss(freelancerprofil.getAbschluss());
				preparedStatement.setInt(2, gid);
				// get expertise id
				int eid = new ExpertiseDAO().getExpertise(freelancerprofil.getFachgebiet());
				preparedStatement.setInt(3, eid);
				preparedStatement.setString(4, freelancerprofil.getBeschreibung());
				// convert array to json
				Gson gson = new GsonBuilder().create();
				String skillsJSON = gson.toJson(freelancerprofil.getSkills());
				preparedStatement.setString(5, skillsJSON);
				preparedStatement.setString(6, freelancerprofil.getLebenslauf());
				preparedStatement.setInt(7, freelancerprofil.getFID());

				preparedStatement.executeUpdate();

				// insert new languages in extra table
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
		// for each row returned from database
		while (resultSet.next()) {
			// get values from resultset
			int freelancerId = resultSet.getInt("FID");
			int nutzerId = resultSet.getInt("NID");
			Nutzer nutzer = new NutzerDAO().getNutzer(nutzerId);
			String graduation = resultSet.getString("graduation.graduation");
			String expertise = resultSet.getString("expertise.expertise");
			String description = resultSet.getString("description");
			String skillsJSON = resultSet.getString("skills");
			// convert json to array
			Gson gson = new GsonBuilder().create();
			String[] skills = gson.fromJson(skillsJSON, String[].class);
			String career = resultSet.getString("career");
			List<String> sprachen = getLanguageInFreelancerprofil(freelancerId);

			try {
				// store fetched data in Freelancerprofil-Object
				Freelancerprofil tempFreelancer = new Freelancerprofil(freelancerId, graduation, expertise, description,
						skills, career, sprachen, nutzer);
				result.add(tempFreelancer);
			} catch (ValidateArgsException e) {
				throw new SQLException("Datenbank ist inkonsistent!", e);
			}
		}
		return result;
	}

	/**
	 * @param fid
	 * @return the languages from extra table
	 * @throws SQLException
	 */
	private List<String> getLanguageInFreelancerprofil(int fid) throws SQLException {
		List<String> res = new LinkedList<>();
		SelectSql statement = new SelectSql();

		String columns = "SID";
		String condition = "FID=?";
		List<Object> conditionWildcards = new LinkedList<>();
		conditionWildcards.add(fid);

		statement.select(columns).from("SprachenzuordnungFP").where(conditionWildcards, condition);
		ResultSet result = statement.executeQuery();

		while (result.next()) {
			int sid = result.getInt("SID");
			String sprache = new SpracheDAO().getSprache(sid);
			res.add(sprache);
		}
		
		return res;
	}

	/**
	 * @param aAbschluss
	 * @param aExpertise
	 * @return a list of {@link model.Freelancerprofil Freelancerprofile} with
	 *         higher graduation in given expertise
	 * @throws SQLException
	 */
	public List<Freelancerprofil> searchForAbschluss(String aAbschluss, String aExpertise) throws SQLException {
		List<Freelancerprofil> result = new LinkedList<>();
		// escape controlchars and replace * with % (wildcard)
		String expertise = aExpertise.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![")
				.replace("*", "%");
		// fetch the hierarchy of given graduation
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
					// get the fids
					int fid = resultSet.getInt("freelancerprofil.FID");
					// get the profil with given id
					Freelancerprofil tempFreelancer = new FreelancerprofilDAO().getFreelancerprofil(fid);
					// add to result if not null
					if (tempFreelancer != null) {
						result.add(tempFreelancer);
					}
				}
			}
		}
		return result;
	}

	/**
	 * @param aName
	 * @return a list of {@link model.Freelancerprofil Freelancerprofile} with given
	 *         name
	 * @throws SQLException
	 */
	public List<Freelancerprofil> searchForName(String aName) throws SQLException {
		List<Freelancerprofil> result = new LinkedList<>();
		// define columns, which shall be seaeched
		List<String> nameTypes = new LinkedList<>();
		nameTypes.add("firstName");
		nameTypes.add("lastName");

		// escape controlchars and replace * with % (wildcard)
		String fullName = aName.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![").replace("*",
				"%");
		// split param to get all single names
		String splitName[] = fullName.split(" ");

		// for each column in database
		for (String nameType : nameTypes) {
			for (String name : splitName) {
				String sql = "SELECT freelancerprofil.FID FROM freelancerprofil "
						+ "INNER JOIN nutzer ON freelancerprofil.NID = nutzer.NID WHERE nutzer." + nameType + " LIKE ?";
				try (Connection connect = datasource.getConnection();
						PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
					preparedStatement.setString(1, name);
					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						while (resultSet.next()) {
							// get the fids
							int fid = resultSet.getInt("freelancerprofil.FID");
							// get the profil with given id
							Freelancerprofil tempFreelancer = new FreelancerprofilDAO().getFreelancerprofil(fid);
							// add to result if not null
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

	/**
	 * @param aSprachen
	 * @return a list of {@link model.Freelancerprofil Freelancerprofile} with given
	 *         languages
	 * @throws SQLException
	 */
	public List<Freelancerprofil> searchForSprache(List<String> aSprachen) throws SQLException {
		List<Freelancerprofil> result = new LinkedList<>();
		HashMap<Integer, Integer> tempList = new HashMap<>();

		// for each language of freelancer
		for (String sprache : aSprachen) {
			// escape controlchars and replace * with % (wildcard)
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
						// get fid from extra table
						int fid = resultSet.getInt("sprachenzuordnungFP.FID");

						int prio;
						// fill hashmap with fid and value how many languages were find for this fid
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
		// remove entries where not all languages were found
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
