package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Adresse;
import model.Nutzer;
import model.Status;
import persistence.SexDAO;
import util.exception.DBException;
import util.exception.UserInputException;
import util.exception.ValidateConstrArgsException;

public class ViewRegistrierung implements Initializable {

	private ObservableList<String> GenderList = FXCollections.observableArrayList("M‰nnlich", "Weiblich", "Anderes");

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
	private TextField UserStraﬂe;

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

	private void nutzerAnlegen(Status status) throws UserInputException, DBException, ValidateConstrArgsException {
		if (UserPW.getText().equals(UserPW2.getText())) {
			String vorname = UserVorname.getText();
			String nachname = UserNachname.getText();
			String geschlecht = UserGeschlecht.getValue();
			String stadt = UserStadt.getText();
			String plz = UserPlz.getText();
			String strasse = UserStraﬂe.getText();
			String hausnummer = UserNr.getText();
			LocalDate birthdate = UserDatum.getValue();
			String eMail = UserMail.getText();
			String passwort = UserPW.getText();

			Nutzer nutzer;
			nutzer = new Nutzer(vorname, nachname, geschlecht, birthdate, eMail, passwort,
					new Adresse(plz, stadt, strasse, hausnummer), status);
			nutzer.setAndHashPassword(passwort);
			nutzer.saveToDatabase();
			verwaltung.setCurrentNutzer(nutzer);
		}
	}

	@FXML
	void addCompany(ActionEvent event) throws IOException {
		try {
			nutzerAnlegen(Status.U);
			changescene("/view/UProfilErstellen.fxml");
		} catch (UserInputException | DBException | ValidateConstrArgsException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Registrierung fehlgeschlagen");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void addFreelancer(ActionEvent event) throws IOException {
		try {
			nutzerAnlegen(Status.F);
			changescene("/view/FProfilErstellen.fxml");
		} catch (UserInputException | DBException | ValidateConstrArgsException e) {
			Alert alert = new Alert(AlertType.ERROR); // Statt .Error geht auch .Warning etc
			alert.setTitle("Error"); // Fenstername
			alert.setHeaderText("Registrierung fehlgeschlagen");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		verwaltung = Verwaltung.getInstance();

		try {
			ObservableList<String> sexList = FXCollections.observableArrayList(new SexDAO().getAllSex());
			UserGeschlecht.setItems(sexList);
			UserGeschlecht.setValue(sexList.get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
