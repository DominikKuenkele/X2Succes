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
import util.exception.ValidateArgsException;

/**
 * Controller class for the view 'FRegistrierung.fxml'
 * 
 * @author domin
 */
public class ViewRegistrierung implements Initializable {

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

	/**
	 * method switches the stage
	 * 
	 * @param fxmlname
	 */
	void changescene(String fxmlname) throws IOException {
		// close current stage
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

	/**
	 * fetches data from form and saves it as {@link model.Nutzer Nutzer} in
	 * database
	 * 
	 * @param status
	 * @throws UserInputException
	 * @throws DBException
	 * @throws ValidateArgsException
	 */
	private void nutzerAnlegen(Status status) throws UserInputException, DBException, ValidateArgsException {
		// checks if repeated password equals the password
		if (UserPW.getText().equals(UserPW2.getText())) {
			// fetches data from form
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

			// stores data in Nutzer-Object
			Nutzer nutzer = new Nutzer(vorname, nachname, geschlecht, birthdate, eMail, passwort,
					new Adresse(plz, stadt, strasse, hausnummer), status);
			// sets password
			nutzer.setAndHashPassword(passwort);
			// save object to database
			nutzer.saveToDatabase();
			// update current Nutzer
			verwaltung.setCurrentNutzer(nutzer);
		}
	}

	/**
	 * opens stage to create an {@link model.Unternehmensprofil Unternehmensprofil}
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void addCompany(ActionEvent event) throws IOException {
		try {
			nutzerAnlegen(Status.U);
			// open new stage
			changescene("/view/UProfilErstellen.fxml");
		} catch (UserInputException | DBException | ValidateArgsException e) {
			// show message, if registration failed
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Registrierung fehlgeschlagen");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	/**
	 * opens stage to create an {@link model.Freelancerprofil Freelancerprofil}
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void addFreelancer(ActionEvent event) throws IOException {
		try {
			nutzerAnlegen(Status.F);
			// open new stage
			changescene("/view/FProfilErstellen.fxml");
		} catch (UserInputException | DBException | ValidateArgsException e) {
			// show message, if registration failed
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Registrierung fehlgeschlagen");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		verwaltung = Verwaltung.getInstance();

		try {
			// fetch all sexes from database
			ObservableList<String> sexList = FXCollections.observableArrayList(new SexDAO().getAllSex());
			// fill choicebox with fetched data
			UserGeschlecht.setItems(sexList);
			UserGeschlecht.setValue(sexList.get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
