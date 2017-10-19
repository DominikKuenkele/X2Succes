package model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import persistence.JobangebotDAO;
import util.Validate;
import util.exception.DBException;
import util.exception.ValidateArgsException;

/**
 * @author domin
 *
 */
public class Jobangebot {
	private int jid = -1;
	private String abschluss;
	private String fachgebiet;
	private List<String> sprachen;
	private String jobTitel;
	private String beschreibung;
	private LocalDate frist;
	private int gehalt;
	private int wochenstunden;
	private Unternehmensprofil unternehmensprofil;

	/**
	 * @param abschluss
	 * @param fachgebiet
	 * @param sprachen
	 * @param jobTitel
	 * @param beschreibung
	 * @param frist
	 * @param gehalt
	 * @param wochenstunden
	 * @param unternehmensprofil
	 * @throws ValidateArgsException
	 */
	public Jobangebot(String abschluss, String fachgebiet, List<String> sprachen, String jobTitel, String beschreibung,
			LocalDate frist, int gehalt, int wochenstunden, Unternehmensprofil unternehmensprofil)
			throws ValidateArgsException {
		this.abschluss = abschluss;
		this.fachgebiet = fachgebiet;
		this.sprachen = sprachen;
		this.jobTitel = jobTitel;
		this.beschreibung = beschreibung;
		this.frist = frist;
		this.gehalt = gehalt;
		this.wochenstunden = wochenstunden;
		this.unternehmensprofil = unternehmensprofil;

		validateState();
	}

	/**
	 * @param jid
	 * @param abschluss
	 * @param fachgebiet
	 * @param sprachen
	 * @param jobTitel
	 * @param beschreibung
	 * @param frist
	 * @param gehalt
	 * @param wochenstunden
	 * @param unternehmensprofil
	 * @throws ValidateArgsException
	 */
	public Jobangebot(int jid, String abschluss, String fachgebiet, List<String> sprachen, String jobTitel,
			String beschreibung, LocalDate frist, int gehalt, int wochenstunden, Unternehmensprofil unternehmensprofil)
			throws ValidateArgsException {
		this(abschluss, fachgebiet, sprachen, jobTitel, beschreibung, frist, gehalt, wochenstunden, unternehmensprofil);
		this.jid = jid;

		validateState();
	}

	/**
	 * @return the JID
	 */
	public int getJID() {
		return jid;
	}

	/**
	 * @return the abschluss
	 */
	public String getAbschluss() {
		return abschluss;
	}

	/**
	 * @return the fachgebiet
	 */
	public String getFachgebiet() {
		return this.fachgebiet;
	}

	/**
	 * @return the sprachen
	 */
	public List<String> getSprachen() {
		return Collections.unmodifiableList(sprachen);
	}

	/**
	 * @return the beschreibung
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	/**
	 * @return the frist
	 */
	public LocalDate getFrist() {
		return frist;
	}

	/**
	 * @return the gehalt
	 */
	public int getGehalt() {
		return gehalt;
	}

	/**
	 * @return the jobTitel
	 */
	public String getJobTitel() {
		return this.jobTitel;
	}

	/**
	 * @return the wochenstunden
	 */
	public int getWochenstunden() {
		return wochenstunden;
	}

	/**
	 * @return the unternehmensprofil
	 */
	public Unternehmensprofil getUnternehmensprofil() {
		return this.unternehmensprofil;
	}

	/**
	 * @param aAbschluss
	 *            the abschluss to set
	 * @throws ValidateArgsException
	 */
	public void setAbschluss(String aAbschluss) throws ValidateArgsException {
		try {
			Validate.validateAbschluss(aAbschluss);
		} catch (IllegalArgumentException e) {
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
	 * @param aJobTitel
	 *            the jobTitel to set
	 * @throws ValidateArgsException
	 */
	public void setJobTitel(String aJobTitel) throws ValidateArgsException {
		try {
			Validate.checkForContent(aJobTitel);
		} catch (IllegalArgumentException e) {
			throw new ValidateArgsException("\nJobtitel: " + e.getMessage());
		}
		this.jobTitel = aJobTitel;
	}

	/**
	 * @param aBeschreibung
	 *            the beschreibung to set
	 */
	public void setBeschreibung(String aBeschreibung) {
		this.beschreibung = aBeschreibung;
	}

	/**
	 * @param aFrist
	 *            the frist to set
	 * @throws ValidateArgsException
	 */
	public void setFrist(LocalDate aFrist) throws ValidateArgsException {
		// try {
		// Validate.checkForDateInFuture(aFrist);
		// } catch (IllegalArgumentException e) {
		// throw new ValidateArgsException("\nFrist: " + e.getMessage());
		// }
		this.frist = aFrist;
	}

	/**
	 * @param aGehalt
	 *            the gehalt to set
	 * @throws ValidateArgsException
	 */
	public void setGehalt(int aGehalt) throws ValidateArgsException {
		try {
			Validate.checkForPositive(aGehalt);
		} catch (IllegalArgumentException e) {
			throw new ValidateArgsException("\nGehalt: " + e.getMessage());
		}
		this.gehalt = aGehalt;
	}

	/**
	 * @param aWochenstunden
	 *            the wochenstunden to set
	 * @throws ValidateArgsException
	 */
	public void setWochenstunden(int aWochenstunden) throws ValidateArgsException {
		try {
			Validate.checkForPositive(aWochenstunden);
		} catch (IllegalArgumentException e) {
			throw new ValidateArgsException("\nWochenstunden: " + e.getMessage());
		}
		this.wochenstunden = aWochenstunden;
	}

	/**
	 * @param aUnternehmensproflil
	 *            the unternehmensprofil to set
	 */
	public void setUnternehmensproflil(Unternehmensprofil aUnternehmensproflil) {
		this.unternehmensprofil = aUnternehmensproflil;
	}

	private void validateState() throws ValidateArgsException {
		String message = "";

		try {
			Validate.validateAbschluss(abschluss);
		} catch (IllegalArgumentException e) {
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
			Validate.checkForContent(jobTitel);
		} catch (IllegalArgumentException e) {
			message = message + "\nJobtitel: " + e.getMessage();
		}
		// try {
		// Validate.checkForDateInFuture(frist);
		// } catch (IllegalArgumentException e) {
		// message = message + "\nFrist: " + e.getMessage();
		// }
		try {
			Validate.checkForPositive(gehalt);
		} catch (IllegalArgumentException e) {
			message = message + "\nGehalt: " + e.getMessage();
		}
		try {
			Validate.checkForPositive(wochenstunden);
		} catch (IllegalArgumentException e) {
			message = message + "\nWochenstunden: " + e.getMessage();
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
			final JobangebotDAO jobangebotDAO = new JobangebotDAO();

			if (jid == -1) {
				this.jid = jobangebotDAO.addJobangebot(this);
			} else {
				jobangebotDAO.changeJobangebot(this);
			}
		} catch (SQLException e) {
			throw new DBException(
					"Auf die Datenbank kann im Moment nicht zugegriffen werden. Versuchen Sie es später erneut!");
		}
	}

	@Override
	public boolean equals(Object object) {
		boolean result = false;
		if (object != null && object instanceof Jobangebot) {
			if (((Jobangebot) object).getJID() == jid) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public int hashCode() {
		return Objects.hash(jid);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Jobangebot [jid=" + this.jid + ", abschluss=" + this.abschluss + ", fachgebiet=" + this.fachgebiet
				+ ", sprachen=" + this.sprachen + ", jobTitel=" + this.jobTitel + ", beschreibung=" + this.beschreibung
				+ ", frist=" + this.frist + ", gehalt=" + this.gehalt + ", wochenstunden=" + this.wochenstunden
				+ ", unternehmensprofil=" + this.unternehmensprofil + "]";
	}
}
