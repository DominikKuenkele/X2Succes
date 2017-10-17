package model;

import util.Validate;
import util.exception.ValidateConstrArgsException;

/**
 * @author domin
 *
 */
public class Adresse {
	private String street;
	private String number;
	private String city;
	private String plz;

	/**
	 * @param street
	 * @param number
	 * @param city
	 * @param plz
	 * @throws ValidateConstrArgsException
	 */
	public Adresse(final String plz, final String city, final String street, final String number)
			throws ValidateConstrArgsException {
		this.street = street;
		this.number = number;
		this.city = city;
		this.plz = plz;

		validateState();
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the plz
	 */
	public String getPlz() {
		return plz;
	}

	private void validateState() throws ValidateConstrArgsException {
		String message = "";
		try {
			Validate.checkForAlpha(street);
		} catch (IllegalArgumentException e) {
			message = message + "Straﬂe: " + e.getMessage();
		}
		try {
			Validate.checkForHausnummer(number);
		} catch (IllegalArgumentException e) {
			message = message + "\nHausnummer: " + e.getMessage();
		}
		try {
			Validate.checkForPLZ(plz);
		} catch (IllegalArgumentException e) {
			message = message + "\nPLZ: " + e.getMessage();
		}
		try {
			Validate.checkForAlpha(city);
		} catch (IllegalArgumentException e) {
			message = message + "\nStadt: " + e.getMessage();
		}
		try {
		} catch (IllegalArgumentException e) {
			throw new ValidateConstrArgsException(e.getMessage());
		}
		if (message != "") {
			throw new ValidateConstrArgsException(message);
		}
	}
}
