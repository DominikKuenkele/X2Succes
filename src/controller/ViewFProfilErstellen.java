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
import util.exception.ValidateArgsException;

/**
 * Controller class for the view 'FProfilErstellen.fxml'
 * 
 * @author domin
 */
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

	/**
	 * saves the user data to database and continues to next stage
	 * 
	 * @param event
	 */
	@FXML
	void continuetoDashboard(ActionEvent event) {
		Verwaltung verwaltung = Verwaltung.getInstance();

		// gather data from form
		String abschluss = degree1.getValue();
		String expertise = topic1.getValue();
		String beschreibung = selfDescription.getText();
		String[] skills = tAskills.getText().split("\n");
		String lebenslauf = cv.getText();

		// gather languages if not empty
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

		// remove duplicates
		List<String> sprachen = new LinkedList<>();
		for (String sprache : sprachenTemp) {
			if (!sprachen.contains(sprache)) {
				sprachen.add(sprache);
			}
		}

		try {
			// save gathered data into object
			Freelancerprofil f = new Freelancerprofil(abschluss, expertise, beschreibung, skills, lebenslauf, sprachen,
					Verwaltung.getInstance().getCurrentNutzer());
			// saves object to database
			f.saveToDatabase();
			// set current Freelancerprofil to created Freelancerprofil
			verwaltung.setCurrentFreelancer(f);
			// open new stage
			switchScene("/view/FRahmen.fxml");
		} catch (ValidateArgsException | DBException e) {
			// show message, if register failed
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Registrierung fehlgeschlagen");
			alert.setContentText(e.getMessage());
			alert.showAndWait();

			e.printStackTrace();
		}
	}

	/**
	 * method switches the stage
	 * 
	 * @param fxmlname
	 */
	void switchScene(String fxmlname) {
		try {
			// close current stage
			Stage prevStage = (Stage) continuebutton.getScene().getWindow();
			prevStage.close();

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			// fetch all languages from database
			ObservableList<String> sprachen = FXCollections.observableArrayList(new SpracheDAO().getAllSprachen());
			// add empty language to list, that user can choose no language
			sprachen.add(0, "");
			// fill language choice boxes with fetched data
			language1.setValue(sprachen.get(0));
			language1.setItems((ObservableList<String>) sprachen);
			language2.setValue(sprachen.get(0));
			language2.setItems((ObservableList<String>) sprachen);
			language3.setValue(sprachen.get(0));
			language3.setItems((ObservableList<String>) sprachen);
			language4.setValue(sprachen.get(0));
			language4.setItems((ObservableList<String>) sprachen);

			// fetch all expertises from database
			ObservableList<String> expertises = FXCollections
					.observableArrayList(new ExpertiseDAO().getAllExpertises());
			// fill expertise choice boxes with fetched data
			topic1.setValue(expertises.get(0));
			topic1.setItems(expertises);

			// fetch all graduations from database
			ObservableList<String> graduation = FXCollections.observableArrayList(new AbschlussDAO().getAllAbschluss());
			// fill graduation choice boxes with fetched data
			degree1.setValue(graduation.get(0));
			degree1.setItems(graduation);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
