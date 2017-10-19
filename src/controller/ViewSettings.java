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

	@FXML
	void changePW(ActionEvent event) {
		Verwaltung verwaltung = Verwaltung.getInstance();
		if (newpassword.getText().equals(newpassword2.getText())) {
			try {
				Nutzer nutzer = verwaltung.getCurrentNutzer();
				nutzer.changePassword(oldpassword.getText(), newpassword.getText());

				oldpassword.clear();
				newpassword.clear();
				newpassword2.clear();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info");
				alert.setHeaderText("Änderung erfolgreich");
				alert.setContentText("Password geändert!");
				alert.showAndWait();
			} catch (UserInputException | ValidateArgsException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Änderung fehlgeschlagen");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Fehler");
			alert.setHeaderText(null);
			alert.setContentText("Die neuen Passwörter sind nicht identisch");

			alert.showAndWait();
		}

	}

	@FXML
	void changeUserData(ActionEvent event) {
		Verwaltung verwaltung = Verwaltung.getInstance();

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
			Nutzer nutzer = verwaltung.getCurrentNutzer();
			nutzer.setFirstName(newFirstName);
			nutzer.setLastName(newLastName);
			nutzer.seteMail(newEmail);
			nutzer.setSex(newSex);
			nutzer.setBirthdate(newBirthdate);
			nutzer.setAddress(new Adresse(newPlz, newCity, newStreet, newNumber));
			nutzer.saveToDatabase();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Info");
			alert.setHeaderText("Nutzerdaten geändert");
			alert.setContentText("Die Nutzerdaten wurden erfolgreich geändert!");
			alert.showAndWait();
		} catch (ValidateArgsException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Änderung fehlgeschlagen");
			alert.setContentText(e.getMessage());
			alert.showAndWait();

			e.printStackTrace();
		} catch (DBException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Änderung fehlgeschlagen");
			alert.setContentText(
					"Auf die Datenbank kann im Moment nicht zugegriffen werden. Versuchen Sie es später erneut.");
			alert.showAndWait();

			e.printStackTrace();
		} catch (UserInputException e) {
			// Cannot occure
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Verwaltung v = Verwaltung.getInstance();
		Nutzer nutzer = v.getCurrentNutzer();// Daten ausfüllen
		if (nutzer != null) {
			newprenom.setText(nutzer.getFirstName());
			newname.setText(nutzer.getLastName());

			try {
				ObservableList<String> sexList = FXCollections.observableArrayList(new SexDAO().getAllSex());
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
