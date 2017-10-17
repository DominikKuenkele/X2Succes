package application;

import java.sql.SQLException;

import model.Freelancerprofil;
import model.Nutzer;
import model.Profil;
import model.Unternehmensprofil;
import persistence.FreelancerprofilDAO;
import persistence.NutzerDAO;
import persistence.UnternehmensprofilDAO;
import util.exception.DBException;

/**
 * This class holds the current profiles and nutzer. Furthermore it represents
 * an interface between model/persistence and view/controller. It is a
 * Singleton-Class.
 * 
 * @author domin
 *
 */
public class Verwaltung extends Subject {
	/**
	 * Holds the current logged in user.
	 */
	private Nutzer currentNutzer;

	/**
	 * Holds the current selected {@link model.Unternehmensprofil
	 * Unternehmensprofil}.
	 */
	private Unternehmensprofil currentUnternehmen;

	/**
	 * Holds the current selected {@link model.Freelancerprofil Freelancerprofil}.
	 */
	private Freelancerprofil currentFreelancer;

	/**
	 * Holds the instance of this Singleton-Class.
	 */
	private static Verwaltung instance;

	/**
	 * private Constructor prevents instantiation of this class
	 */
	private Verwaltung() {
	}

	/**
	 * @return the instance of this Singleton-Class
	 */
	public static Verwaltung getInstance() {
		if (instance == null) {
			instance = new Verwaltung();
		}
		return instance;
	}

	/**
	 * @return the current {@link model.Nutzer}
	 */
	public Nutzer getCurrentNutzer() {
		return this.currentNutzer;
	}

	/**
	 * @return the current profile
	 */
	public Profil getCurrentProfil() {
		switch (this.currentNutzer.getStatus()) {
		case F:
			return this.currentFreelancer;
		case U:
			return this.currentUnternehmen;
		default:
			return null;
		}
	}

	/**
	 * Validates userdata. If correct, fetch current {@link model.Nutzer Nutzer} and
	 * {@link model.Unternehmensprofil
	 * Unternehmensprofil}/{@link model.Freelancerprofil Freelancerprofil} from
	 * database
	 * 
	 * @param eMail
	 * @param password
	 * @return true, if login was successful
	 * @throws DBException
	 */
	public boolean login(final String eMail, final String password) throws DBException {
		boolean result = false;
		try {
			Nutzer nutzer = new NutzerDAO().getNutzer(eMail);
			final boolean validation = nutzer.validatePassword(password);

			if (validation == true) {
				setCurrentNutzer(nutzer);
				switch (nutzer.getStatus()) {
				case F:
					Freelancerprofil f = new FreelancerprofilDAO().getFreelancerprofilByNutzer(nutzer.getNID());
					setCurrentFreelancer(f);
					break;
				case U:
					Unternehmensprofil u = new UnternehmensprofilDAO().getUnternehmensprofilByNutzer(nutzer.getNID());
					setCurrentUnternehmensprofil(u);
					break;
				default:
					// do nothing
				}
				result = true;
			}
		} catch (SQLException e) {
			throw new DBException(
					"Auf die Datenbank kann im Moment nicht zugegriffen werden. Versuchen Sie es später erneut!");
		}
		return result;
	}

	/**
	 * Deletes instance from this Singleton-Class
	 */
	public void logout() {
		instance = null;
	}

	/**
	 * sets the current {@link model.Nutzer Nutzer}
	 * 
	 * @param aNutzer
	 */
	public void setCurrentNutzer(final Nutzer aNutzer) {
		this.currentNutzer = aNutzer;
		notifyAllObservers(this.currentNutzer);
	}

	/**
	 * sets the current {@link model.Freelancerprofil Freelancerprofil}
	 * 
	 * @param aFreelancerprofil
	 */
	public void setCurrentFreelancer(final Freelancerprofil aFreelancerprofil) {
		this.currentFreelancer = aFreelancerprofil;
		notifyFreelancerObeserver(this.currentFreelancer);
	}

	/**
	 * sets the current {@link model.Unternehmensprofil Unternehmensprofil}
	 * 
	 * @param aFreelancerprofil
	 */
	public void setCurrentUnternehmensprofil(final Unternehmensprofil aUnternehmensprofil) {
		this.currentUnternehmen = aUnternehmensprofil;
		notifyUnternehmerObeserver(this.currentUnternehmen);
	}
}
