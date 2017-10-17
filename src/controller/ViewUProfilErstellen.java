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
import util.exception.ValidateConstrArgsException;

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

	void switchScene(String fxmlname) {
		try {
			Stage prevStage = (Stage) UAnlegenButton.getScene().getWindow();
			Stage stage = new Stage();
			stage.setTitle("X2Success");
			Pane myPane = FXMLLoader.load(getClass().getResource(fxmlname));
			Scene scene = new Scene(myPane);
			stage.setScene(scene);

			prevStage.close();

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void Unternehmenanlegen(ActionEvent event) {
		verwaltung = Verwaltung.getInstance();

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
			Unternehmensprofil unternehmensprofil = new Unternehmensprofil(name, form,
					new Adresse(plz, stadt, strasse, hausnummer), gruendung, mitarbeiter, beschreibung, branche,
					website, ceoFirstName, ceoLastName, verwaltung.getCurrentNutzer());
			unternehmensprofil.saveToDatabase();
			verwaltung.setCurrentUnternehmensprofil(unternehmensprofil);

			switchScene("/view/URahmen.fxml");
		} catch (ValidateConstrArgsException | DBException e) {
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
			ObservableList<String> branche = FXCollections.observableArrayList(new BrancheDAO().getAllBranchen());
			UBranche.setValue(branche.get(0));
			UBranche.setItems(branche);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
