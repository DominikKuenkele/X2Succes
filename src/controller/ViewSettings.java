package controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.Verwaltung;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Adresse;
import model.Nutzer;
import persistence.SexDAO;
import util.exception.DBException;
import util.exception.UserInputException;
import util.exception.ValidateArgsException;

/**
 * Controller class for the view 'Settings.fxml'
 * 
 * @author domin
 */
public class ViewSettings implements Initializable {

	@FXML
	private TextField newprenom;

	@FXML
	private TextField newname;

	@FXML
	private TextField city;

	@FXML
	private TextField street;

	@FXML
	private TextField plz;

	@FXML
	private TextField streetnumber;

	@FXML
	private Button changeUserDataButton;

	@FXML
	private TextField email;

	@FXML
	private PasswordField oldpassword;

	@FXML
	private PasswordField newpassword;

	@FXML
	private PasswordField newpassword2;

	@FXML
	private Button changepwbutton;

	@FXML
	private DatePicker birthdate;

	@FXML
	private ChoiceBox<String> newgender;

	private Verwaltung verwaltung;

	/**
	 * @param event
	 */
	@FXML
	void changePW(ActionEvent event) {
		// checks if repeated password equals the password
		if (newpassword.getText().equals(newpassword2.getText())) {
			try {
				// get current Nutzer
				Nutzer nutzer = verwaltung.getCurrentNutzer();
				// change password
				nutzer.changePassword(oldpassword.getText(), newpassword.getText());

				// clear textfields
				oldpassword.clear();
				newpassword.clear();
				newpassword2.clear();

				// show message, if change was succesful
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info");
				alert.setHeaderText("�nderung erfolgreich");
				alert.setContentText("Password ge�ndert!");
				alert.showAndWait();
			} catch (UserInputException | ValidateArgsException e) {
				// show message, if change failed
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("�nderung fehlgeschlagen");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		} else {
			// show message, if repeated password not equals the password
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Fehler");
			alert.setHeaderText("�nderung fehlgeschlagen");
			alert.setContentText("Die neuen Passw�rter sind nicht identisch");
			alert.showAndWait();
		}
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	void changeUserData(ActionEvent event) {
		// fetch data from form
		String newFirstName = newprenom.getText();
		String newLastName = newname.getText();
		String newEmail = email.getText();
		String newSex = newgender.getValue();
		String newPlz = plz.getText();
		String newCity = city.getText();
		String newStreet = street.getText();
		String newNumber = streetnumber.getText();
		LocalDate newBirthdate = birthdate.getValue();

		try {
			// store fetched data in object
			Nutzer nutzer = verwaltung.getCurrentNutzer();
			nutzer.setFirstName(newFirstName);
			nutzer.setLastName(newLastName);
			nutzer.seteMail(newEmail);
			nutzer.setSex(newSex);
			nutzer.setBirthdate(newBirthdate);
			nutzer.setAddress(new Adresse(newPlz, newCity, newStreet, newNumber));
			// save object to database
			nutzer.saveToDatabase();

			// show message, if change was successful
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Info");
			alert.setHeaderText("Nutzerdaten ge�ndert");
			alert.setContentText("Die Nutzerdaten wurden erfolgreich ge�ndert!");
			alert.showAndWait();
		} catch (ValidateArgsException e) {
			// show message, if change failed because of false user input
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("�nderung fehlgeschlagen");
			alert.setContentText(e.getMessage());
			alert.showAndWait();

			e.printStackTrace();
		} catch (DBException e) {
			// show message, if change failed because of failed database acces
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("�nderung fehlgeschlagen");
			alert.setContentText(
					"Auf die Datenbank kann im Moment nicht zugegriffen werden. Versuchen Sie es sp�ter erneut.");
			alert.showAndWait();

			e.printStackTrace();
		} catch (UserInputException e) {
			// Cannot occure
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		verwaltung = Verwaltung.getInstance();
		// get current Nutzer
		Nutzer nutzer = verwaltung.getCurrentNutzer();
		if (nutzer != null) {
			// fill form with data from Nutzer
			newprenom.setText(nutzer.getFirstName());
			newname.setText(nutzer.getLastName());

			try {
				// fetch all sexes from database
				ObservableList<String> sexList = FXCollections.observableArrayList(new SexDAO().getAllSex());
				// fill choicebox
				newgender.setItems(sexList);
				newgender.setValue(nutzer.getSex());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			city.setText(nutzer.getAddress().getCity());
			street.setText(nutzer.getAddress().getStreet());
			plz.setText(nutzer.getAddress().getPlz());
			streetnumber.setText(nutzer.getAddress().getNumber());
			email.setText(nutzer.geteMail());
			birthdate.setValue(nutzer.getBirthdate());
		}
	}
}
