package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
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
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Freelancerprofil;
import persistence.AbschlussDAO;
import persistence.ExpertiseDAO;
import persistence.SpracheDAO;
import util.exception.DBException;
import util.exception.ValidateConstrArgsException;

public class ViewFProfilErstellen implements Initializable {

	@FXML
	private ChoiceBox<String> degree1;

	@FXML
	private ChoiceBox<String> topic1;

	@FXML
	private TextArea cv;

	@FXML
	private TextArea tAskills;

	@FXML
	private TextArea selfDescription;

	@FXML
	private Button continuebutton;

	@FXML
	private ChoiceBox<String> language1;

	@FXML
	private ChoiceBox<String> language2;

	@FXML
	private ChoiceBox<String> language3;

	@FXML
	private ChoiceBox<String> language4;

	@FXML
	void continuetoDashboard(ActionEvent event) {
		Verwaltung verwaltung = Verwaltung.getInstance();

		String abschluss = degree1.getValue();
		String expertise = topic1.getValue();
		String beschreibung = selfDescription.getText();
		String[] skills = tAskills.getText().split("\n");
		String lebenslauf = cv.getText();
		List<String> sprachenTemp = new LinkedList<>();
		if (!language1.getValue().equals("")) {
			sprachenTemp.add(language1.getValue());
		}
		if (!language2.getValue().equals("")) {
			sprachenTemp.add(language2.getValue());
		}
		if (!language3.getValue().equals("")) {
			sprachenTemp.add(language3.getValue());
		}
		if (!language4.getValue().equals("")) {
			sprachenTemp.add(language4.getValue());
		}

		List<String> sprachen = new LinkedList<>();
		for (String sprache : sprachenTemp) {
			if (!sprachen.contains(sprache)) {
				sprachen.add(sprache);
			}
		}

		try {
			Freelancerprofil f = new Freelancerprofil(abschluss, expertise, beschreibung, skills, lebenslauf, sprachen,
					Verwaltung.getInstance().getCurrentNutzer());
			f.saveToDatabase();
			verwaltung.setCurrentFreelancer(f);
			switchScene("/view/FRahmen.fxml");
		} catch (ValidateConstrArgsException | DBException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Registrierung fehlgeschlagen");
			alert.setContentText(e.getMessage());
			alert.showAndWait();

			e.printStackTrace();
		}
	}

	void switchScene(String fxmlname) {
		try {
			Stage prevStage = (Stage) continuebutton.getScene().getWindow();
			Stage stage = new Stage();
			stage.setTitle("X2Success");
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			ObservableList<String> sprachen = FXCollections.observableArrayList(new SpracheDAO().getAllSprachen());
			sprachen.add(0, "");
			language1.setValue(sprachen.get(0)); // Anfangswert
			language1.setItems((ObservableList<String>) sprachen); // Name der Liste
			language2.setValue(sprachen.get(0)); // Anfangswert
			language2.setItems((ObservableList<String>) sprachen); // Name der Liste
			language3.setValue(sprachen.get(0)); // Anfangswert
			language3.setItems((ObservableList<String>) sprachen); // Name der Liste
			language4.setValue(sprachen.get(0)); // Anfangswert
			language4.setItems((ObservableList<String>) sprachen); // Name der Liste

			ObservableList<String> expertises = FXCollections
					.observableArrayList(new ExpertiseDAO().getAllExpertises());
			topic1.setValue(expertises.get(0)); // Anfangswert
			topic1.setItems(expertises); // Name der Liste

			ObservableList<String> graduation = FXCollections.observableArrayList(new AbschlussDAO().getAllAbschluss());
			degree1.setValue(graduation.get(0)); // Anfangswert
			degree1.setItems(graduation); // Name der Liste

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
