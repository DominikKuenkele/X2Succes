package util;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import persistence.AbschlussDAO;
import persistence.BrancheDAO;
import persistence.ExpertiseDAO;
import persistence.SexDAO;
import persistence.SpracheDAO;
import util.exception.ValidateArgsException;

/**
 * Class is a Utility-class for the Validation of parameters.
 * 
 * @author Dominik Künkele
 */
public final class Validate {

	/**
	 * Private constructor prevents instantiation of this Utility-class.
	 */
	private Validate() {
	}

	/**
	 * Method validates a String, if it contains only letters and spaces.
	 * 
	 * @param password
	 * @throws IllegalArgumentException
	 */
	public static void checkForPassword(final String password) throws IllegalArgumentException {
		final int MIN_PASSWORD_LENGTH = 8;
		if (password.length() < MIN_PASSWORD_LENGTH) {
			throw new IllegalArgumentException(
					"Das Passwort muss mindestens " + MIN_PASSWORD_LENGTH + " Zeichen haben!");
		}
	}

	/**
	 * Method validates a String, if it contains only letters and spaces.
	 * 
	 * @param text
	 * @throws IllegalArgumentException
	 */
	public static void checkForName(final String text) throws IllegalArgumentException {
		checkForContent(text);
		if (!text.matches("[-A-Za-zäÄöÖüÜß ]+")) {
			throw new IllegalArgumentException("Text \"" + text + "\" darf nur Buchstaben enthalten!");
		}
	}

	/**
	 * Method validates, if String is a Branche
	 * 
	 * @param branche
	 * @throws ValidateArgsException
	 */
	public static void validateBranche(String branche) throws ValidateArgsException {
		try {
			List<String> branchenList = new BrancheDAO().getAllBranchen();
			if (!branchenList.contains(branche)) {
				throw new ValidateArgsException("Diese Branche darf nicht ausgewählt werden");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method validates, if String is a sex
	 * 
	 * @param sex
	 * @throws ValidateArgsException
	 */
	public static void validateSex(String sex) throws ValidateArgsException {
		try {
			List<String> sexList = new SexDAO().getAllSex();
			if (!sexList.contains(sex)) {
				throw new ValidateArgsException("Dieses Geschlecht darf nicht ausgewählt werden");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method validates, if String is a language
	 * 
	 * @param sprachen
	 * @throws ValidateArgsException
	 */
	public static void validateSprachen(List<String> sprachen) throws ValidateArgsException {
		try {
			String message = "";

			List<String> sprachenList = new SpracheDAO().getAllSprachen();
			for (String sprache : sprachen) {
				if (!sprachenList.contains(sprache)) {
					message = message + "\nDiese Sprache (" + sprache + ")darf nicht ausgewählt werden";
				}
			}
			if (!message.equals("")) {
				throw new ValidateArgsException(message);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method validates, if String is a graduation
	 * 
	 * @param abschluss
	 * @throws ValidateArgsException
	 */
	public static void validateAbschluss(String abschluss) throws ValidateArgsException {
		try {
			List<String> abschlussList = new AbschlussDAO().getAllAbschluss();
			if (!abschlussList.contains(abschluss)) {
				throw new ValidateArgsException("Dieser Abschluss darf nicht ausgewählt werden");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method validates, if String is an expertise
	 * 
	 * @param fachgebiet
	 * @throws ValidateArgsException
	 */
	public static void validateFachgebiet(String fachgebiet) throws ValidateArgsException {
		try {
			List<String> fachgebietList = new ExpertiseDAO().getAllExpertises();
			if (!fachgebietList.contains(fachgebiet)) {
				throw new ValidateArgsException("Dieses Fachgebiet darf nicht ausgewählt werden");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Validates an URL
	 * 
	 * @param url
	 * @throws IllegalArgumentException
	 */
	public static void checkForUrl(final String url) throws IllegalArgumentException {
		final String URL_PATTERN = "[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
		Pattern pattern = Pattern.compile(URL_PATTERN);
		Matcher matcher = pattern.matcher(url);

		if (!matcher.matches()) {
			throw new IllegalArgumentException("Url ist nicht zulässig!");
		}
	}

	/**
	 * Validates an e-Mail
	 * 
	 * @param eMail
	 * @throws IllegalArgumentException
	 */
	public static void checkForEMail(final String eMail) throws IllegalArgumentException {
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(eMail);

		if (!matcher.matches()) {
			throw new IllegalArgumentException("E-Mail-Adresse ungültig!");
		}
	}

	/**
	 * Compares two Integer-Values
	 * 
	 * @param value1
	 * @param value2
	 * @throws IllegalArgumentException
	 */
	public static void checkForGreaterValue(final int value1, final int value2) throws IllegalArgumentException {
		if (value1 < value2) {
			throw new IllegalArgumentException("Die Zahl ist zu klein!");
		}
	}

	/**
	 * Validates, if Date is in the future
	 * 
	 * @param date
	 * @throws IllegalArgumentException
	 */
	public static void checkForDateInFuture(final LocalDate date) throws IllegalArgumentException {
		if (date.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Das Datum muss in der Zukunft liegen!");
		}
	}

	/**
	 * Validates, if Date is in the past
	 * 
	 * @param date
	 * @throws IllegalArgumentException
	 */
	public static void checkForDateInPast(final LocalDate date) throws IllegalArgumentException {
		if (date.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("Das Datum muss in der Vergangenheit liegen!");
		}
	}

	/**
	 * Validates, if String has content
	 * 
	 * @param text
	 * @throws IllegalArgumentException
	 */
	public static void checkForContent(final String text) throws IllegalArgumentException {
		final String EMPTY_STRING = "";
		if ((text == null) || (text.trim().equals(EMPTY_STRING))) {
			throw new IllegalArgumentException("Text darf nicht leer sein!");
		}
	}

	/**
	 * Validates, if String is a PLZ
	 * 
	 * @param plz
	 */
	public static void checkForPLZ(final String plz) {
		final String PLZ_PATTERN = "[1-9][0-9]{4,4}";
		Pattern pattern = Pattern.compile(PLZ_PATTERN);
		Matcher matcher = pattern.matcher(plz);

		if (!matcher.matches()) {
			throw new IllegalArgumentException("Das ist keine PLZ!");
		}
	}

	/**
	 * Validates, if String is house number
	 * 
	 * @param nr
	 */
	public static void checkForHausnummer(final String nr) {
		final String HNR_PATTERN = "[1-9][0-9]{0,3}";
		Pattern pattern = Pattern.compile(HNR_PATTERN);
		Matcher matcher = pattern.matcher(nr);

		if (!matcher.matches()) {
			throw new IllegalArgumentException("Das ist keine Hausnummer!");
		}
	}

	/**
	 * Method validates an Integer, if it is positive.
	 * 
	 * @param zahl
	 * @throws IllegalArgumentException
	 */
	public static void checkForPositive(final int zahl) throws IllegalArgumentException {
		if (zahl < 0) {
			throw new IllegalArgumentException("Zahl muss positiv sein!");
		}
	}

	/**
	 * Method validates a String, if it contains only letters and spaces.
	 * 
	 * @param text
	 * @throws IllegalArgumentException
	 */
	public static void checkForAlpha(final String text) throws IllegalArgumentException {
		checkForContent(text);
		if (!text.matches("[A-Za-zäÄöÖüÜß ]+")) {
			throw new IllegalArgumentException("Text \"" + text + "\" darf nur Buchstaben enthalten!");
		}
	}

	/**
	 * Method validates a String, if it contains only letters, spaces and numbers.
	 * 
	 * @param text
	 * @throws IllegalArgumentException
	 */
	public static void checkForAlphaNumeric(final String text) throws IllegalArgumentException {
		checkForContent(text);
		if (!text.matches("[A-Za-zäÄöÖüÜß 0-9]+")) {
			throw new IllegalArgumentException("Text \"" + text + "\" darf nur Buchstaben oder Zahlen enthalten!");
		}
	}
}
