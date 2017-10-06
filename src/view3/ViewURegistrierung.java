package view3;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.exception.UserInputException;

public class ViewURegistrierung implements Initializable{
	
	ObservableList<String> Branchenliste = FXCollections.observableArrayList("Branche1","Branche2","Branche3");

	private Verwaltung verwaltung;

	boolean v = true;

	public void validieren(String name) { // Pr�fen ob Mussfelder Eintr�ge enthalten, Plz, Hausnummer etc pr�fen?!
		if (name.equals(""))
			v = false;
		if (Ubranche.equals(""))
			v = false;
	}

	@FXML
	private TextField UName;

	@FXML
	private TextField UForm;

	@FXML
	private TextField UStadt;

	@FXML
	private TextField UPlz;

	@FXML
	private TextField UStra�e;

	@FXML
	private TextField UNr;

	@FXML
	private DatePicker UDatum;

	@FXML
	private TextField UMitarbeiter;

	private ChoiceBox<String> Ubranche;


	@FXML
	private TextArea UBeschreibung;

	@FXML
	private Button UAnlegenButton;

	@FXML
	void switchScene(String fxmlname) {
		try {
			Stage prevStage = (Stage) UAnlegenButton.getScene().getWindow();
			Stage stage = new Stage();
			stage.setTitle("Shop Management");
			Pane myPane = null;
			myPane = FXMLLoader.load(getClass().getResource(fxmlname));
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

		String name = UName.getText();
		String form = UForm.getText();
		String stadt = UStadt.getText();
		String plz = UPlz.getText();
		String strasse = UStra�e.getText();
		String hausnummer = UNr.getText();
		LocalDate gruendung = UDatum.getValue();
		int mitarbeiter = Integer.parseInt(UMitarbeiter.getText());
		String beschreibung = UBeschreibung.getText();
		String branche = Ubranche.getSelectionModel().getSelectedItem().toString();
		validieren(name); // TODO Inhalt pr�fen
		if (v = false) {
			Alert alert = new Alert(AlertType.ERROR); // Statt .Error geht auch .Warning etc
			alert.setTitle("Error"); // Fenstername
			alert.setHeaderText("Registrierung fehlgeschlagen");
			alert.setContentText("Bitte f�llen sie alle Pflichtfelder (*) aus");

			alert.showAndWait();
		} else {
			try {
				verwaltung.createUnternehmen(name, form, plz, stadt, strasse, hausnummer, gruendung, mitarbeiter,
						beschreibung, "benefits", branche, "www.test.de", "Vorname", "Nachname");
				switchScene("Unternehmen_Home_Dashboard_nofavs.fxml");
			} catch (UserInputException e) {
				// TODO VALIDATION!
				e.printStackTrace();
			}

			// H�chste UnternehmensID aus DB holen
			// UID = UID+1;
			// Unternehmensprofil XY =new Unternehmensprofil(Name,Beschreibung,Branche,)
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Ubranche.setValue("Branche ausw�hlen");
		Ubranche.setItems(Branchenliste);
		
	}

}