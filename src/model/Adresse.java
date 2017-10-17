package model;

import util.Validate;
import util.exception.ValidateArgsException;

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
	 * @throws ValidateArgsException
	 */
	public Adresse(final String plz, final String city, final String street, final String number)
			throws ValidateArgsException {
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

	private void validateState() throws ValidateArgsException {
		String message = "";
		try {
			Validate.checkForAlpha(street);
		} catch (IllegalArgumentException e) {
			message = message + "Stra�e: " + e.getMessage();
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
			throw new ValidateArgsException(e.getMessage());
		}
		if (message != "") {
			throw new ValidateArgsException(message);
		}
	}
}
