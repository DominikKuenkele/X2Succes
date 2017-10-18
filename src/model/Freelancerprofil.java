package model;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import persistence.FreelancerprofilDAO;
import persistence.JobangebotDAO;
import util.Validate;
import util.exception.DBException;
import util.exception.ValidateArgsException;

/**
 * @author domin
 *
 */
public class Freelancerprofil implements Profil {
	private static final int ANZAHL_STAERKEN = 3;

	private int fid = -1;
	private String abschluss;
	private String fachgebiet;
	private String beschreibung;
	private String[] skills = new String[ANZAHL_STAERKEN];
	private String lebenslauf;
	private List<String> sprachen;
	private Nutzer nutzer;

	/**
	 * @param abschluss
	 * @param fachgebiet
	 * @param beschreibung
	 * @param skills
	 * @param lebenslauf
	 * @param sprachen
	 * @param nutzer
	 * @throws ValidateArgsException
	 */
	public Freelancerprofil(final String abschluss, final String fachgebiet, final String beschreibung,
			final String[] skills, final String lebenslauf, final List<String> sprachen, Nutzer nutzer)
			throws ValidateArgsException {
		this.abschluss = abschluss;
		this.fachgebiet = fachgebiet;
		this.beschreibung = beschreibung;
		this.skills = Arrays.copyOf(skills, skills.length);
		this.lebenslauf = lebenslauf;
		this.sprachen = sprachen;
		this.nutzer = nutzer;

		validateState();
	}

	/**
	 * @param fid
	 * @param abschluss
	 * @param fachgebiet
	 * @param beschreibung
	 * @param skills
	 * @param lebenslauf
	 * @param sprachen
	 * @param nutzer
	 * @throws ValidateArgsException
	 */
	public Freelancerprofil(int fid, final String abschluss, final String fachgebiet, final String beschreibung,
			final String[] skills, final String lebenslauf, final List<String> sprachen, Nutzer nutzer)
			throws ValidateArgsException {
		this(abschluss, fachgebiet, beschreibung, skills, lebenslauf, sprachen, nutzer);
		this.fid = fid;
		validateState();
	}

	/**
	 * @return the abschluss
	 */
	public String getAbschluss() {
		return abschluss;
	}

	/**
	 * @return the beschreibung
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	/**
	 * @return the fachgebiet
	 */
	public String getFachgebiet() {
		return this.fachgebiet;
	}

	/**
	 * @return the skills
	 */
	public String[] getSkills() {
		return Arrays.copyOf(skills, skills.length);
	}

	/**
	 * @return the lebenslauf
	 */
	public String getLebenslauf() {
		return lebenslauf;
	}

	/**
	 * @return the sprachen
	 */
	public List<String> getSprachen() {
		return Collections.unmodifiableList(sprachen);
	}

	/**
	 * @return the fid
	 */
	public int getFID() {
		return fid;
	}

	/**
	 * @return the nutzer
	 */
	public Nutzer getNutzer() {
		return nutzer;
	}

	/**
	 * @return the fid
	 */
	public int getFid() {
		return this.fid;
	}

	/**
	 * @param aAbschluss
	 *            the abschluss to set
	 * @throws ValidateArgsException
	 */
	public void setAbschluss(String aAbschluss) throws ValidateArgsException {
		try {
			Validate.validateAbschluss(aAbschluss);
		} catch (ValidateArgsException e) {
			throw new ValidateArgsException("\nAbschluss: " + e.getMessage());
		}
		this.abschluss = aAbschluss;
	}

	/**
	 * @param aFachgebiet
	 *            the fachgebiet to set
	 * @throws ValidateArgsException
	 */
	public void setFachgebiet(String aFachgebiet) throws ValidateArgsException {
		try {
			Validate.validateFachgebiet(aFachgebiet);
		} catch (IllegalArgumentException e) {
			throw new ValidateArgsException("\nFachgebiet: " + e.getMessage());
		}
		this.fachgebiet = aFachgebiet;
	}

	/**
	 * @param aBeschreibung
	 *            the beschreibung to set
	 */
	public void setBeschreibung(String aBeschreibung) {
		this.beschreibung = aBeschreibung;
	}

	/**
	 * @param aSkills
	 *            the skills to set
	 * @throws ValidateArgsException
	 */
	public void setSkills(String... aSkills) throws ValidateArgsException {
		try {
			validateSkills(aSkills);
		} catch (IllegalArgumentException e) {
			throw new ValidateArgsException("\nSkills: " + e.getMessage());
		}
		this.skills = aSkills;
	}

	/**
	 * @param aLebenslauf
	 *            the lebenslauf to set
	 */
	public void setLebenslauf(String aLebenslauf) {
		this.lebenslauf = aLebenslauf;
	}

	/**
	 * @param aSprachen
	 *            the sprachen to set
	 * @throws ValidateArgsException
	 */
	public void setSprachen(List<String> aSprachen) throws ValidateArgsException {
		try {
			Validate.validateSprachen(aSprachen);
		} catch (IllegalArgumentException e) {
			throw new ValidateArgsException("\nSprachen: " + e.getMessage());
		}
		this.sprachen = aSprachen;
	}

	/**
	 * @param aNutzer
	 *            the nutzer to set
	 */
	public void setNutzer(Nutzer aNutzer) {
		this.nutzer = aNutzer;
	}

	private void validateState() throws ValidateArgsException {
		String message = "";

		try {
			Validate.validateAbschluss(abschluss);
		} catch (ValidateArgsException e) {
			message = message + "\nAbschluss: " + e.getMessage();
		}
		try {
			Validate.validateFachgebiet(fachgebiet);
		} catch (IllegalArgumentException e) {
			message = message + "\nFachgebiet: " + e.getMessage();
		}
		try {
			Validate.validateSprachen(sprachen);
		} catch (IllegalArgumentException e) {
			message = message + "\nSprachen: " + e.getMessage();
		}
		try {
			validateSkills(skills);
		} catch (IllegalArgumentException e) {
			message = message + "\nSkills: " + e.getMessage();
		}
		if (message != "") {
			throw new ValidateArgsException(message);
		}
	}

	/**
	 * Method saves this Object to database and sets the Id returned from database
	 * 
	 * @throws DBException
	 */
	public void saveToDatabase() throws DBException {
		try {
			final FreelancerprofilDAO freelancerprofilDao = new FreelancerprofilDAO();

			if (fid == -1) {
				this.fid = freelancerprofilDao.addFreelancerprofil(this);
			} else {
				freelancerprofilDao.changeFreelancerprofil(this);
			}
		} catch (SQLException e) {
			throw new DBException(
					"Auf die Datenbank kann im Moment nicht zugegriffen werden. Versuchen Sie es später erneut!");
		}
	}

	private void validateSkills(final String... skills) {
		if (skills.length > ANZAHL_STAERKEN) {
			throw new IllegalArgumentException("Es dürfen nur drei Skills angegeben werden.");
		}
	}

	/**
	 * Searches for {@link model.Jobangebot Jobangebote} in database with given
	 * parameters
	 * 
	 * @param name
	 * @param abschluss
	 * @param expertise
	 * @param branche
	 * @param minMitarbeiter
	 * @param maxMitarbeiter
	 * @param minGehalt
	 * @return a List of {@link model.Jobangebot Jobangebote} with search-Priority
	 * @throws SQLException
	 */
	public static List<Entry<Jobangebot, Integer>> sucheJobangebote(String name, String abschluss, String expertise,
			String branche, int minMitarbeiter, int maxMitarbeiter, int minGehalt) throws SQLException {
		JobangebotDAO jobangebotDao = new JobangebotDAO();

		List<List<Jobangebot>> searchList = new LinkedList<>();
		searchList.add(jobangebotDao.searchForName(name));
		searchList.add(jobangebotDao.searchForAbschluss(abschluss, expertise));
		searchList.add(jobangebotDao.searchForBranche(branche));
		searchList.add(jobangebotDao.searchForMitarbeiter(minMitarbeiter, maxMitarbeiter));
		searchList.add(jobangebotDao.searchForGehalt(minGehalt));

		Set<Entry<Jobangebot, Integer>> prioList = prioritizeJobangebote(searchList);
		List<Map.Entry<Jobangebot, Integer>> list = new LinkedList<>(prioList);
		Collections.sort(list, new Comparator<Map.Entry<Jobangebot, Integer>>() {
			@Override
			public int compare(Map.Entry<Jobangebot, Integer> e1, Map.Entry<Jobangebot, Integer> e2) {
				return (e2.getValue()).compareTo(e1.getValue());
			}
		});

		return list;
	}

	/**
	 * Sorts a list of {@link model.Jobangebot Jobangebote} by their priority
	 * 
	 * @param searchList
	 * @return sorted Set
	 */
	private static Set<Entry<Jobangebot, Integer>> prioritizeJobangebote(List<List<Jobangebot>> searchList) {
		HashMap<Jobangebot, Integer> prioList = new HashMap<>();
		for (List<Jobangebot> sL : searchList) {
			for (Jobangebot jobangebot : sL) {
				int prio;
				if (!prioList.containsKey(jobangebot)) {
					prio = 1;
				} else {
					prio = prioList.get(jobangebot) + 1;
				}
				prioList.put(jobangebot, prio);
			}
		}
		return prioList.entrySet();
	}

	@Override
	public boolean equals(Object object) {
		boolean result = false;
		if (object != null && object instanceof Freelancerprofil) {
			if (((Freelancerprofil) object).getFID() == fid) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fid);
	}

	@Override
	public String toString() {
		return "Freelancerprofil [fid=" + this.fid + ", abschluss=" + this.abschluss + ", fachgebiet=" + this.fachgebiet
				+ ", beschreibung=" + this.beschreibung + ", skills=" + Arrays.toString(this.skills) + ", lebenslauf="
				+ this.lebenslauf + ", sprachen=" + this.sprachen + ", nutzer=" + this.nutzer + "]";
	}
}
