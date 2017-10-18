package model;

import java.sql.SQLException;
import java.time.LocalDate;

import persistence.NutzerDAO;
import util.PassHash;
import util.Validate;
import util.exception.DBException;
import util.exception.DuplicateEntryException;
import util.exception.UserInputException;
import util.exception.ValidateArgsException;

/**
 * @author domin
 *
 */
public class Nutzer {
	private int nid = -1;
	private String firstName;
	private String lastName;
	private String sex;
	private LocalDate birthdate;
	private String eMail;
	private String password;
	private Adresse address;
	private Status status = Status.N;

	/**
	 * @param firstName
	 * @param lastName
	 * @param sex
	 * @param birthdate
	 * @param eMail
	 * @param password
	 * @param address
	 * @param status
	 * @throws ValidateArgsException
	 */
	public Nutzer(String firstName, String lastName, String sex, LocalDate birthdate, String eMail, String password,
			Adresse address, Status status) throws ValidateArgsException {
		this.firstName = firstName;
		this.lastName = lastName;
		this.sex = sex;
		this.birthdate = birthdate;
		this.eMail = eMail;
		this.password = password;
		this.address = address;
		this.status = status;

		validateState();
	}

	/**
	 * @param nid
	 * @param firstName
	 * @param lastName
	 * @param sex
	 * @param birthdate
	 * @param eMail
	 * @param password
	 * @param address
	 * @param status
	 * @throws ValidateArgsException
	 */
	public Nutzer(int nid, String firstName, String lastName, String sex, LocalDate birthdate, String eMail,
			String password, Adresse address, Status status) throws ValidateArgsException {
		this(firstName, lastName, sex, birthdate, eMail, password, address, status);

		this.nid = nid;

		validateState();
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @return the eMail
	 */
	public String geteMail() {
		return eMail;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @return the birthdate
	 */
	public LocalDate getBirthdate() {
		return birthdate;
	}

	/**
	 * @return the address
	 */
	public Adresse getAddress() {
		return address;
	}

	/**
	 * @return the nid
	 */
	public int getNID() {
		return nid;
	}

	/**
	 * @return the nid
	 */
	public int getNid() {
		return this.nid;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @param aFirstName
	 *            the firstName to set
	 * @throws ValidateArgsException
	 */
	public void setFirstName(String aFirstName) throws ValidateArgsException {
		try {
			Validate.checkForName(aFirstName);
		} catch (IllegalArgumentException e) {
			throw new ValidateArgsException("\nVorname: " + e.getMessage());
		}
		this.firstName = aFirstName;
	}

	/**
	 * @param aLastName
	 *            the lastName to set
	 * @throws ValidateArgsException
	 */
	public void setLastName(String aLastName) throws ValidateArgsException {
		try {
			Validate.checkForName(aLastName);
		} catch (IllegalArgumentException e) {
			throw new ValidateArgsException("\nNachname: " + e.getMessage());
		}
		this.lastName = aLastName;
	}

	/**
	 * @param aSex
	 *            the sex to set
	 * @throws ValidateArgsException
	 */
	public void setSex(String aSex) throws ValidateArgsException {
		try {
			Validate.validateSex(aSex);
		} catch (IllegalArgumentException e) {
			throw new ValidateArgsException("\nGeschlecht: " + e.getMessage());
		}
		this.sex = aSex;
	}

	/**
	 * @param aBirthdate
	 *            the birthdate to set
	 * @throws ValidateArgsException
	 */
	public void setBirthdate(LocalDate aBirthdate) throws ValidateArgsException {
		try {
			if (aBirthdate == null) {
				throw new IllegalArgumentException("Das Datum darf nicht leer sein!");
			}
			Validate.checkForDateInPast(aBirthdate);
		} catch (IllegalArgumentException e) {
			throw new ValidateArgsException("\nGeburtstag: " + e.getMessage());
		}
		this.birthdate = aBirthdate;
	}

	/**
	 * @param aEMail
	 *            the eMail to set
	 * @throws ValidateArgsException
	 */
	public void seteMail(String aEMail) throws ValidateArgsException {
		try {
			Validate.checkForEMail(aEMail);
		} catch (IllegalArgumentException e) {
			throw new ValidateArgsException("\nE-Mail: " + e.getMessage());
		}
		this.eMail = aEMail;
	}

	/**
	 * @param aAddress
	 *            the address to set
	 */
	public void setAddress(Adresse aAddress) {
		this.address = aAddress;
	}

	/**
	 * Method sets the password after hashing it
	 * 
	 * @param password
	 */
	public void setAndHashPassword(String password) {
		this.password = PassHash.generateStrongPasswordHash(password);
	}

	/**
	 * @param oldPassword
	 * @param newPassword
	 *            the password to set
	 * @throws UserInputException
	 */
	public void changePassword(String oldPassword, String newPassword) throws UserInputException {
		final boolean validation = PassHash.validatePassword(oldPassword, password);
		if (validation == true) {
			this.password = PassHash.generateStrongPasswordHash(newPassword);
		} else {
			throw new UserInputException("Das alte Passwort stimmt nicht");
		}
	}

	/**
	 * @param password
	 * @return true, if password is correct
	 */
	public boolean validatePassword(String password) {
		return PassHash.validatePassword(password, this.password);
	}

	private void validateState() throws ValidateArgsException {
		String message = "";

		try {
			Validate.checkForName(firstName);
		} catch (IllegalArgumentException e) {
			message = message + "\nVorname: " + e.getMessage();
		}
		try {
			Validate.checkForName(lastName);
		} catch (IllegalArgumentException e) {
			message = message + "\nNachname: " + e.getMessage();
		}
		try {
			Validate.validateSex(sex);
		} catch (IllegalArgumentException e) {
			message = message + "\nGeschlecht: " + e.getMessage();
		}
		try {
			if (birthdate == null) {
				throw new IllegalArgumentException("Das Datum darf nicht leer sein!");
			}
			Validate.checkForDateInPast(birthdate);
		} catch (IllegalArgumentException e) {
			message = message + "\nGeburtstag: " + e.getMessage();
		}
		try {
			Validate.checkForEMail(eMail);
		} catch (IllegalArgumentException e) {
			message = message + "\nE-Mail: " + e.getMessage();
		}

		if (message != "") {
			throw new ValidateArgsException(message);
		}
	}

	/**
	 * Method saves this Object to database and sets the Id returned from database
	 * 
	 * @throws DBException
	 * @throws UserInputException
	 */
	public void saveToDatabase() throws DBException, UserInputException {
		try {
			final NutzerDAO nutzerDao = new NutzerDAO();
			if (nid == -1) {
				try {
					this.nid = nutzerDao.addNutzer(this);
				} catch (DuplicateEntryException e) {
					throw new UserInputException(e.getMessage());
				}
			} else {
				nutzerDao.changeNutzer(this);
			}
		} catch (SQLException e) {
			throw new DBException(
					"Auf die Datenbank kann im Moment nicht zugegriffen werden. Versuchen Sie es später erneut!");
		}
	}

	@Override
	public String toString() {
		return "Nutzer [eMail=" + eMail + ", password=" + password + ", firstName=" + firstName + ", lastName="
				+ lastName + ", sex=" + sex + ", birthdate=" + birthdate + ", address=" + address + ", status=" + status
				+ "]\n";
	}

}
