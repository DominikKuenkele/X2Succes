package model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import persistence.FreelancerprofilDAO;
import persistence.UnternehmensprofilDAO;
import util.Validate;
import util.exception.DBException;
import util.exception.ValidateConstrArgsException;

/**
 * @author domin
 *
 */
public class Unternehmensprofil implements Profil {
	private int uid = -1;
	private String name;
	private String legalForm;
	private Adresse address;
	private LocalDate founding;
	private int employees;
	private String description;
	private String branche;
	private String website;
	private String ceoFirstName;
	private String ceoLastName;
	private Nutzer nutzer;

	/**
	 * @param name
	 * @param legalForm
	 * @param address
	 * @param founding
	 * @param employees
	 * @param description
	 * @param branche
	 * @param website
	 * @param ceoFirstName
	 * @param ceoLastName
	 * @param nutzer
	 * @throws ValidateConstrArgsException
	 */
	public Unternehmensprofil(String name, String legalForm, Adresse address, LocalDate founding, int employees,
			String description, String branche, String website, String ceoFirstName, String ceoLastName, Nutzer nutzer)
			throws ValidateConstrArgsException {
		this.name = name;
		this.legalForm = legalForm;
		this.address = address;
		this.founding = founding;
		this.employees = employees;
		this.description = description;
		this.branche = branche;
		this.website = website;
		this.ceoFirstName = ceoFirstName;
		this.ceoLastName = ceoLastName;
		this.nutzer = nutzer;

		validateState();
	}

	/**
	 * @param name
	 * @param legalForm
	 * @param address
	 * @param founding
	 * @param employees
	 * @param description
	 * @param branche
	 * @param website
	 * @param ceoFirstName
	 * @param ceoLastName
	 * @param nutzer
	 * @throws ValidateConstrArgsException
	 */
	public Unternehmensprofil(int uid, String name, String legalForm, Adresse address, LocalDate founding,
			int employees, String description, String branche, String website, String ceoFirstName, String ceoLastName,
			Nutzer nutzer) throws ValidateConstrArgsException {
		this(name, legalForm, address, founding, employees, description, branche, website, ceoFirstName, ceoLastName,
				nutzer);

		this.uid = uid;

		validateState();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the legalForm
	 */
	public String getLegalForm() {
		return legalForm;
	}

	/**
	 * @return the address
	 */
	public Adresse getAddress() {
		return address;
	}

	/**
	 * @return the founding
	 */
	public LocalDate getFounding() {
		return founding;
	}

	/**
	 * @return the employees
	 */
	public int getEmployees() {
		return employees;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the branche
	 */
	public String getBranche() {
		return branche;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @return the ceoFirstName
	 */
	public String getCeoFirstName() {
		return ceoFirstName;
	}

	/**
	 * @return the ceoLastName
	 */
	public String getCeoLastName() {
		return ceoLastName;
	}

	/**
	 * @return the nutzer
	 */
	public Nutzer getNutzer() {
		return nutzer;
	}

	/**
	 * @return the uid
	 */
	public int getUid() {
		return this.uid;
	}

	/**
	 * @param aName
	 *            the name to set
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * @param aLegalForm
	 *            the legalForm to set
	 */
	public void setLegalForm(String aLegalForm) {
		this.legalForm = aLegalForm;
	}

	/**
	 * @param aAddress
	 *            the address to set
	 */
	public void setAddress(Adresse aAddress) {
		this.address = aAddress;
	}

	/**
	 * @param aFounding
	 *            the founding to set
	 */
	public void setFounding(LocalDate aFounding) {
		this.founding = aFounding;
	}

	/**
	 * @param aEmployees
	 *            the employees to set
	 */
	public void setEmployees(int aEmployees) {
		this.employees = aEmployees;
	}

	/**
	 * @param aDescription
	 *            the description to set
	 */
	public void setDescription(String aDescription) {
		this.description = aDescription;
	}

	/**
	 * @param aBranche
	 *            the branche to set
	 */
	public void setBranche(String aBranche) {
		this.branche = aBranche;
	}

	/**
	 * @param aWebsite
	 *            the website to set
	 */
	public void setWebsite(String aWebsite) {
		this.website = aWebsite;
	}

	/**
	 * @param aCeoFirstName
	 *            the ceoFirstName to set
	 */
	public void setCeoFirstName(String aCeoFirstName) {
		this.ceoFirstName = aCeoFirstName;
	}

	/**
	 * @param aCeoLastName
	 *            the ceoLastName to set
	 */
	public void setCeoLastName(String aCeoLastName) {
		this.ceoLastName = aCeoLastName;
	}

	/**
	 * @param aNutzer
	 *            the nutzer to set
	 */
	public void setNutzer(Nutzer aNutzer) {
		this.nutzer = aNutzer;
	}

	private void validateState() throws ValidateConstrArgsException {
		String message = "";

		try {
			Validate.checkForContent(name);
		} catch (IllegalArgumentException e) {
			message = message + "\nName: " + e.getMessage();
		}
		try {
			Validate.checkForContent(description);
		} catch (IllegalArgumentException e) {
			message = message + "\nBeschreibung: " + e.getMessage();
		}
		try {
			Validate.checkForAlpha(ceoFirstName);
		} catch (IllegalArgumentException e) {
			message = message + "\nCEO Vorname: " + e.getMessage();
		}
		try {
			Validate.checkForAlpha(ceoFirstName);
		} catch (IllegalArgumentException e) {
			message = message + "\nCEO-Nachname: " + e.getMessage();
		}

		if (message != "") {
			throw new ValidateConstrArgsException(message);
		}
	}

	public void saveToDatabase() throws DBException {
		try {
			final UnternehmensprofilDAO unternehmensprofilDao = new UnternehmensprofilDAO();

			if (uid == -1) {
				this.uid = unternehmensprofilDao.addUnternehmensprofil(this);
			} else {
				unternehmensprofilDao.changeUnternehmen(this);
			}
		} catch (SQLException e) {
			throw new DBException(
					"Auf die Datenbank kann im Moment nicht zugegriffen werden. Versuchen Sie es später erneut!");
		}
	}

	/**
	 * Searches for {@link model.Freelancerprofil Freelancerprofile} in database
	 * with given parameters
	 * 
	 * @param name
	 * @param abschluss
	 * @param expertise
	 * @param sprachen
	 * @return a List of {@link model.Freelancerprofil Freelancerprofile} with
	 *         search-Priority
	 * @throws SQLException
	 */
	public static List<Entry<Freelancerprofil, Integer>> sucheFreelancer(String name, String abschluss,
			String expertise, List<String> sprachen) throws SQLException {
		FreelancerprofilDAO freelancerprofilDao = new FreelancerprofilDAO();

		List<List<Freelancerprofil>> searchList = new LinkedList<>();
		searchList.add(freelancerprofilDao.searchForName(name));
		searchList.add(freelancerprofilDao.searchForAbschluss(abschluss, expertise));
		searchList.add(freelancerprofilDao.searchForSprache(sprachen));

		Set<Entry<Freelancerprofil, Integer>> prioList = prioritizeFreelancerprofile(searchList);
		List<Map.Entry<Freelancerprofil, Integer>> list = new LinkedList<>(prioList);
		Collections.sort(list, new Comparator<Map.Entry<Freelancerprofil, Integer>>() {
			@Override
			public int compare(Map.Entry<Freelancerprofil, Integer> e1, Map.Entry<Freelancerprofil, Integer> e2) {
				return (e2.getValue()).compareTo(e1.getValue());
			}
		});

		return list;
	}

	/**
	 * Sorts a list of {@link model.Freelancerprofil Freelancerprofile} by their
	 * priority
	 * 
	 * @param searchList
	 * @return sorted Set
	 */
	private static Set<Entry<Freelancerprofil, Integer>> prioritizeFreelancerprofile(
			List<List<Freelancerprofil>> searchList) {
		HashMap<Freelancerprofil, Integer> prioList = new HashMap<>();
		for (List<Freelancerprofil> sL : searchList) {
			for (Freelancerprofil freelancerprofil : sL) {
				int prio;
				if (!prioList.containsKey(freelancerprofil)) {
					prio = 1;
				} else {
					prio = prioList.get(freelancerprofil) + 1;
				}
				prioList.put(freelancerprofil, prio);
			}
		}
		return prioList.entrySet();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Unternehmensprofil [uid=" + this.uid + ", name=" + this.name + ", legalForm=" + this.legalForm
				+ ", address=" + this.address + ", founding=" + this.founding + ", employees=" + this.employees
				+ ", description=" + this.description + ", branche=" + this.branche + ", website=" + this.website
				+ ", ceoFirstName=" + this.ceoFirstName + ", ceoLastName=" + this.ceoLastName + ", nutzer="
				+ this.nutzer + "]";
	}
}
