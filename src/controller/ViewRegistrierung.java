package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.Verwaltung;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.exception.DBException;
import util.exception.UserInputException;

public class ViewRegistrierung implements Initializable {

	ObservableList<String> GenderList = FXCollections.observableArrayList("M�nnlich", "Weiblich", "Anderes");

	private Verwaltung verwaltung;

	@FXML
	private TextField UserVorname;

	@FXML
	private TextField UserNachname;

	@FXML
	private ChoiceBox<String> UserGeschlecht;

	@FXML
	private DatePicker UserDatum;

	@FXML
	private TextField UserStadt;

	@FXML
	private TextField UserStra�e;

	@FXML
	private TextField UserNr;

	@FXML
	private TextField UserPlz;

	@FXML
	private TextField UserMail;

	@FXML
	private PasswordField UserPW;

	@FXML
	private PasswordField UserPW2;

	@FXML
	private Button addfreelancerb;

	@FXML
	private Button addcompanyb;

	void changescene(String fxmlname) throws IOException {

		// schliesst aktuelles Fenster
		Stage stage2 = (Stage) addfreelancerb.getScene().getWindow();
		stage2.close();

		Stage stage = new Stage();
		stage.setTitle("X2Success");
		Pane myPane = null;
		myPane = FXMLLoader.load(getClass().getResource(fxmlname));
		Scene scene = new Scene(myPane);
		stage.setScene(scene);
		stage.show();

	}

	@FXML
	private void NutzerAnlegen() {
		if (UserPW.getText().equals(UserPW2.getText())) {
			String vorname = UserVorname.getText();
			String nachname = UserNachname.getText();
			String geschlecht;
			switch (UserGeschlecht.getValue()) {
			case "M�nnlich":
				geschlecht = "m";
				break;
			case "Weiblich":
				geschlecht = "w";
				break;
			default:
				geschlecht = "a";
			}
			String stadt = UserStadt.getText();
			String plz = UserPlz.getText();
			String strasse = UserStra�e.getText();
			String hausnummer = UserNr.getText();
			LocalDate localDate = UserDatum.getValue();
			String eMail = UserMail.getText();
			String passwort = UserPW.getText();

			// String vorname = "Dominik";
			// String nachname = "K�nkele";
			// String geschlecht = "m";
			// String stadt = "Weinstadt";
			// String plz = "71384";
			// String strasse = "Strasse";
			// String hausnummer = "19";
			// LocalDate localDate = LocalDate.of(1999, 02, 05);
			// String eMail = "dominik.kuenkele@live.test";
			// String passwort = "Meins";

			try {
				verwaltung.register(vorname, nachname, geschlecht, plz, stadt, strasse, hausnummer, localDate, eMail,
						passwort);
			} catch (UserInputException | DBException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void addcompanyb(ActionEvent event) throws IOException {
		NutzerAnlegen();
		changescene("/view/UProfilErstellen.fxml");
	}

	@FXML
	void addfreelancer(ActionEvent event) throws IOException {
		NutzerAnlegen();
		changescene("FProfilErstellen.fxml");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		verwaltung = Verwaltung.getInstance();
		UserGeschlecht.setValue("Geschlecht ausw�hlen"); // Anfangswert
		UserGeschlecht.setItems(GenderList); // Name der Liste
	}

}
