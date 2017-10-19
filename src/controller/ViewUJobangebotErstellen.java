package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.ResourceBundle;

import application.Verwaltung;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Jobangebot;
import model.Unternehmensprofil;
import persistence.AbschlussDAO;
import persistence.ExpertiseDAO;
import util.exception.DBException;
import util.exception.ValidateArgsException;

/**
 * Controller class for the view 'UJobangebotErstellen.fxml'
 * 
 * @author domin
 */
public class ViewUJobangebotErstellen implements Initializable {

	@FXML
	private ImageView addofferbutton;

	@FXML
	private TextField jobtitel;

	@FXML
	private TextArea jobdescription;

	@FXML
	private TextField worktime;

	@FXML
	private TextField salary;

	@FXML
	private ChoiceBox<String> necessarydegree;

	@FXML
	private ChoiceBox<String> topic;

	@FXML
	private Button createofferb;

	@FXML
	private Button seeofferb;

	@FXML
	private TextField contactname;

	@FXML
	private TextField contactemail;

	@FXML
	private TextField contactphone;

	private Verwaltung verwaltung;

	/**
	 * gathers data from form
	 * 
	 * @return a {@link model.Jobangebot Jobangebot} with gathered data
	 * @throws ValidateArgsException
	 */
	private Jobangebot gatherData() throws ValidateArgsException {
		Jobangebot jobangebot;

		String jobTitle = jobtitel.getText();
		String description = jobdescription.getText();
		int weeklyworktime;
		int monthlysalary;
		weeklyworktime = Integer.parseInt(worktime.getText());
		monthlysalary = Integer.parseInt(salary.getText());
		String degree = necessarydegree.getValue();
		String topic1 = topic.getValue();

		// not yet used
		String cname = contactname.getText();
		// not yet used
		String cmail = contactemail.getText();

		// frist not yet used
		jobangebot = new Jobangebot(degree, topic1, new LinkedList<String>(), jobTitle, description,
				LocalDate.of(5050, 12, 31), monthlysalary, weeklyworktime,
				(Unternehmensprofil) verwaltung.getCurrentProfil());
		return jobangebot;
	}

	/**
	 * @param event
	 */
	@FXML
	void createoffer(MouseEvent event) {
		verwaltung = Verwaltung.getInstance();

		try {
			try {
				Jobangebot jobangebot = gatherData();
				// saves gathered data to database
				jobangebot.saveToDatabase();

				// show message, if creation was successful
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info");
				alert.setHeaderText("Jobangebot erstellt");
				alert.setContentText("Das Jobangebot wurde erfolgreich erstellt!");
				alert.showAndWait();
			} catch (ValidateArgsException | DBException e) {
				// show message, if creation failed
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Registrierung fehlgeschlagen");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		} catch (NumberFormatException e) {
			// show message, if creation failed
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ungültige Eingabe");
			alert.setContentText("Das ist keine Zahl!");
			alert.showAndWait();
		}
	}

	void changescene(String fxmlname) throws IOException {
		// close current stage
		Stage stage2 = (Stage) seeofferb.getScene().getWindow();
		stage2.close();

		// open new stage
		Stage stage = new Stage();
		stage.setTitle("X2Success");
		Pane myPane = null;
		myPane = FXMLLoader.load(getClass().getResource(fxmlname));
		Scene scene = new Scene(myPane);
		stage.setScene(scene);
		stage.show();

	}

	/**
	 * open preview of new {@link model.Jobangebot Jobangebot}
	 * 
	 * @param event
	 */
	@FXML
	void seeoffer(MouseEvent event) throws IOException {
		try {
			// gather data form from
			Jobangebot jobangebot = gatherData();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FJobangebot.fxml"));
			Pane myPane = loader.load();
			ViewFJobangebot controller = loader.getController();
			// transfer Jobangebot to controller
			controller.setJobangebot(jobangebot);

			// open new stage
			Stage stage = new Stage();
			stage.setTitle("X2Success");

			Scene scene = new Scene(myPane);
			stage.setScene(scene);
			stage.show();
		} catch (ValidateArgsException e) {
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
			// fetch all expertises from database
			ObservableList<String> expertises = FXCollections
					.observableArrayList(new ExpertiseDAO().getAllExpertises());
			// fill choicebox
			topic.setItems(expertises);
			topic.setValue(expertises.get(0));

			// fetch all graduations from database
			ObservableList<String> graduation = FXCollections.observableArrayList(new AbschlussDAO().getAllAbschluss());
			// fill choicebox
			necessarydegree.setItems(graduation);
			necessarydegree.setValue(graduation.get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
