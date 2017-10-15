package util;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * Class is a Utility-class for the Validation of parameters.
 * 
 * @author Dominik K�nkele
 * @date 22.09.2017
 */
public final class Validate {

	/**
	 * Private constructor prevents instantiation of this Utility-class.
	 */
	private Validate() {
	}

	/**
	 * Validats a URL
	 * 
	 * @param url
	 * @throws IllegalArgumentException
	 */
	public static void checkForUrl(final String url) throws IllegalArgumentException {
		final UrlValidator urlvalidator = new UrlValidator();
		if (!urlvalidator.isValid(url)) {
			throw new IllegalArgumentException("Url ist nicht zul�ssig!");
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
			throw new IllegalArgumentException("Das ist keine eMail-Adresse!");
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
		if (zahl <= 0) {
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
		if (!text.matches("[A-Za-z������� ]+")) {
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
		if (!text.matches("[A-Za-z������� 0-9]+")) {
			throw new IllegalArgumentException("Text \"" + text + "\" darf nur Buchstaben oder Zahlen enthalten!");
		}
	}
}
