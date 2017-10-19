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
 * Controller class for the view 'FProfil.fxml'
 * 
 * @author domin
 */
public class ViewFProfil implements Initializable {

	@FXML
	private ChoiceBox<String> degree1;

	@FXML
	private ChoiceBox<String> topic1;

	@FXML
	private TextArea cv;

	@FXML
	private ChoiceBox<String> language1;

	@FXML
	private ChoiceBox<String> language2;

	@FXML
	private ChoiceBox<String> language3;

	@FXML
	private ChoiceBox<String> language4;

	@FXML
	private TextArea tAskills;

	@FXML
	private TextArea selfDescription;

	@FXML
	private Button changefreelancerbutton;

	@FXML
	private Button showfreelancerbutton;

	private Verwaltung verwaltung;

	/**
	 * gathers data from form
	 * 
	 * @return a {@link model.Freelancerprofil Freelancerprofil} with gathered data
	 * @throws ValidateArgsException
	 */
	private Freelancerprofil gatherData() throws ValidateArgsException {
		Freelancerprofil freelancerprofil;

		String abschluss = degree1.getValue();
		String expertise = topic1.getValue();
		String beschreibung = selfDescription.getText();
		String[] skills = tAskills.getText().split("\n");
		String lebenslauf = cv.getText();
		// Fetch language from choicebox if not empty
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

		// Remove duplicates
		List<String> sprachen = new LinkedList<>();
		for (String sprache : sprachenTemp) {
			if (!sprachen.contains(sprache)) {
				sprachen.add(sprache);
			}
		}

		// store data in Freelancerprofil-Object
		freelancerprofil = new Freelancerprofil(abschluss, expertise, beschreibung, skills, lebenslauf, sprachen,
				verwaltung.getCurrentNutzer());

		return freelancerprofil;
	}

	/**
	 * changes data from existing {@link model.Freelancerprofil Freelancerprofil}
	 * 
	 * @param event
	 */
	@FXML
	void changeFreelancer(ActionEvent event) {
		try {
			// get the current Freelancerprofil with its id
			Freelancerprofil freelancerprofil = (Freelancerprofil) verwaltung.getCurrentProfil();
			// get data from form
			Freelancerprofil tempFreelancerprofil = gatherData();
			// change Freelancerprofil-Object data
			freelancerprofil.setAbschluss(tempFreelancerprofil.getAbschluss());
			freelancerprofil.setFachgebiet(tempFreelancerprofil.getFachgebiet());
			freelancerprofil.setBeschreibung(tempFreelancerprofil.getBeschreibung());
			freelancerprofil.setSkills(tempFreelancerprofil.getSkills());
			freelancerprofil.setLebenslauf(tempFreelancerprofil.getLebenslauf());
			freelancerprofil.setSprachen(tempFreelancerprofil.getSprachen());
			freelancerprofil.saveToDatabase();

			// show message, if change was successful
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Info");
			alert.setHeaderText("Freelancerprofil geändert");
			alert.setContentText("Das Freelancerprofil wurde erfolgreich geändert!");
			alert.showAndWait();
		} catch (ValidateArgsException e) {
			// show message, if change failed because of false user input
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Änderung fehlgeschlagen");
			alert.setContentText(e.getMessage());
			alert.showAndWait();

			e.printStackTrace();
		} catch (DBException e) {
			// show message, if change failed because of failed database acces
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Änderung fehlgeschlagen");
			alert.setContentText(
					"Auf die Datenbank kann im Moment nicht zugegriffen werden. Versuchen Sie es später erneut.");
			alert.showAndWait();

			e.printStackTrace();
		}
	}

	/**
	 * Method changes the stage
	 * 
	 * @param fxmlname
	 * @throws IOException
	 */
	void changeScene(String fxmlname) throws IOException {
		Stage stage = new Stage();
		stage.setTitle("X2Success");
		Pane myPane = FXMLLoader.load(getClass().getResource(fxmlname));
		Scene scene = new Scene(myPane);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * opens a preview of a Freelancerprofil-GUI with changed data
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void showFreelancer(ActionEvent event) throws IOException {
		try {
			// gather and store data from form
			Freelancerprofil freelancerprofil = gatherData();

			// open new stage and transfer the changed Freelancerprofil to its controllerS
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UFreelancerprofil.fxml"));
			Pane myPane = loader.load();
			ViewUFreelancerprofil controller = loader.getController();
			controller.setFreelancer(freelancerprofil);

			Stage stage = new Stage();
			stage.setTitle("X2Success");

			Scene scene = new Scene(myPane);
			stage.setScene(scene);
			stage.show();
		} catch (ValidateArgsException e) {
			// show message, if change failed because of false user input
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Registrierung fehlgeschlagen");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// fetch the current Freelancerprofil
		verwaltung = Verwaltung.getInstance();
		Freelancerprofil f = (Freelancerprofil) verwaltung.getCurrentProfil();

		try {
			// Fetch all languages from database
			ObservableList<String> sprachen = FXCollections.observableArrayList(new SpracheDAO().getAllSprachen());
			sprachen.add(0, "");
			List<String> sprachenFP = f.getSprachen();

			// fill all items of choiceboxes with all languages from database
			// set the values to languages from user
			language1.setItems((ObservableList<String>) sprachen);
			if (sprachenFP.size() > 0) {
				language1.setValue(sprachenFP.get(0));
			} else {
				language1.setValue(sprachen.get(0));
			}

			language2.setItems((ObservableList<String>) sprachen);
			if (sprachenFP.size() > 1) {
				language2.setValue(sprachenFP.get(1));
			} else {
				language2.setValue(sprachen.get(0));
			}

			language3.setItems((ObservableList<String>) sprachen);
			if (sprachenFP.size() > 2) {
				language3.setValue(sprachenFP.get(2));
			} else {
				language3.setValue(sprachen.get(0));
			}

			language4.setItems((ObservableList<String>) sprachen);
			if (sprachenFP.size() > 3) {
				language4.setValue(sprachenFP.get(3));
			} else {
				language4.setValue(sprachen.get(0));
			}

			// Fetch all expertises from database
			ObservableList<String> expertises = FXCollections
					.observableArrayList(new ExpertiseDAO().getAllExpertises());
			// fill all items of choiceboxes with all expertises from database
			// set the values to expertise from user
			topic1.setItems(expertises);
			if (!f.getFachgebiet().equals("")) {
				topic1.setValue(f.getFachgebiet());
			} else {
				topic1.setValue(expertises.get(0));
			}

			// Fetch all graduations from database
			ObservableList<String> graduation = FXCollections.observableArrayList(new AbschlussDAO().getAllAbschluss());
			// fill all items of choiceboxes with all graduations from database
			// set the values to graduation from user
			degree1.setItems(graduation);
			if (!f.getAbschluss().equals("")) {
				degree1.setValue(f.getAbschluss());
			} else {
				degree1.setValue(graduation.get(0));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// fill textfield with current cv from the Freelancerprofil
		cv.setText(f.getLebenslauf());

		// fill textfield with current skills from the Freelancerprofil
		String skillsArray[] = f.getSkills();
		for (String skill : skillsArray) {
			tAskills.setText(skill + "\n");
		}

		// fill textfield with current description from the Freelancerprofil
		selfDescription.setText(f.getBeschreibung());
	}
}
