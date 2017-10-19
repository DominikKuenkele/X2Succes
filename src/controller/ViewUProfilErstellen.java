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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Adresse;
import model.Unternehmensprofil;
import persistence.BrancheDAO;
import util.exception.DBException;
import util.exception.ValidateArgsException;

/**
 * Controller class for the view 'UProfilErstellen.fxml'
 * 
 * @author domin
 */
public class ViewUProfilErstellen implements Initializable {

	private Verwaltung verwaltung;

	@FXML
	private TextField cCeoname;

	@FXML
	private TextField cCeoprenom;

	@FXML
	private TextField cWebsite;

	@FXML
	private TextField UName;

	@FXML
	private TextField UForm;

	@FXML
	private TextField UStadt;

	@FXML
	private TextField UPlz;

	@FXML
	private TextField UStraﬂe;

	@FXML
	private TextField UNr;

	@FXML
	private DatePicker UDatum;

	@FXML
	private ChoiceBox<String> UBranche;

	@FXML
	private TextField UMitarbeiter;

	@FXML
	private TextArea UBeschreibung;

	@FXML
	private Button UAnlegenButton;

	/**
	 * method switches the stage
	 * 
	 * @param fxmlname
	 */
	void switchScene(String fxmlname) {
		try {
			// close current stage
			Stage prevStage = (Stage) UAnlegenButton.getScene().getWindow();
			prevStage.close();

			// open new stage
			Stage stage = new Stage();
			stage.setTitle("X2Success");
			Pane myPane = FXMLLoader.load(getClass().getResource(fxmlname));
			Scene scene = new Scene(myPane);
			stage.setScene(scene);

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * saves the user data to database and continues to next stage
	 * 
	 * @param event
	 */
	@FXML
	void Unternehmenanlegen(ActionEvent event) {
		verwaltung = Verwaltung.getInstance();

		// get data from form
		String name = UName.getText();
		String form = UForm.getText();
		String plz = UPlz.getText();
		String stadt = UStadt.getText();
		String strasse = UStraﬂe.getText();
		String hausnummer = UNr.getText();
		LocalDate gruendung = UDatum.getValue();
		int mitarbeiter = Integer.parseInt(UMitarbeiter.getText());
		String branche = UBranche.getValue();
		String beschreibung = UBeschreibung.getText();
		String ceoFirstName = cCeoprenom.getText();
		String ceoLastName = cCeoname.getText();
		String website = cWebsite.getText();
		try {
			// store data in object
			Unternehmensprofil unternehmensprofil = new Unternehmensprofil(name, form,
					new Adresse(plz, stadt, strasse, hausnummer), gruendung, mitarbeiter, beschreibung, branche,
					website, ceoFirstName, ceoLastName, verwaltung.getCurrentNutzer());
			// save data to database
			unternehmensprofil.saveToDatabase();
			// set current Unternehmensprofil
			verwaltung.setCurrentUnternehmensprofil(unternehmensprofil);

			switchScene("/view/URahmen.fxml");
		} catch (ValidateArgsException | DBException e) {
			// show message, if creation failed
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Registrierung fehlgeschlagen");
			alert.setContentText(e.getMessage());
			alert.showAndWait();

			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		verwaltung = Verwaltung.getInstance();
		try {
			// fetch all branchen from database
			ObservableList<String> branche = FXCollections.observableArrayList(new BrancheDAO().getAllBranchen());
			// fill choicebox
			UBranche.setValue(branche.get(0));
			UBranche.setItems(branche);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
