package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import application.Verwaltung;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Freelancerprofil;
import model.Jobangebot;
import persistence.AbschlussDAO;
import persistence.BrancheDAO;
import persistence.ExpertiseDAO;
import view.JobangebotAnzeige;

/**
 * Controller class for the view 'FSuche.fxml'
 * 
 * @author domin
 */
public class ViewFSuche implements Initializable, EventHandler<MouseEvent> {

	private JobangebotAnzeige[] jA;

	@FXML
	private TextField searchcompanyname;

	@FXML
	private ChoiceBox<String> searchnecessarydegree;

	@FXML
	private ChoiceBox<String> searchExpertise;

	@FXML
	private ChoiceBox<String> searchbranche;

	@FXML
	private TextField minimumemployees;

	@FXML
	private Slider salarySlider;

	@FXML
	private TextField Salary;

	@FXML
	private TextField maximumemployees;

	@FXML
	private Button serachoffers;

	@FXML
	private ScrollPane scrollPane;

	private Verwaltung verwaltung;

	/**
	 * updates salary-slider if value of salary-textfield has changed
	 * 
	 * @param event
	 */
	@FXML
	void changeslider(KeyEvent event) {
		salarySlider.setValue(Double.parseDouble(Salary.getText()));
	}

	/**
	 * updates salary-textfield if value of salary-slider has changed
	 * 
	 * @param event
	 */
	@FXML
	void dragdone(MouseEvent event) {
		Double d = salarySlider.getValue();
		int i = d.intValue();
		Salary.setText(Integer.toString(i));
	}

	/**
	 * searches and displays {@link model.Jobangebot Jobangebote}
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void searchoffers(ActionEvent event) throws IOException {
		// fetches name from textfield
		String cName;
		// if textfield is empty, fill it with a *, to search for all names
		if (searchcompanyname.getText().equals("")) {
			cName = "*";
		} else {
			cName = searchcompanyname.getText();
		}

		// fetches min employees from textfield
		int minEmployees;
		// if textfield is empty, fill it with 0 (minimum value)
		if (minimumemployees.getText().equals("")) {
			minEmployees = 0;
		} else {
			minEmployees = Integer.parseInt(minimumemployees.getText());
		}

		// fetches max employees from textfield
		int maxEmployees;
		// if textfield is empty, fill it with 10000000 (maximum value)
		if (maximumemployees.getText().equals("")) {
			maxEmployees = 10000000;
		} else {
			maxEmployees = Integer.parseInt(maximumemployees.getText());
		}

		// fetches expertise from choicebox
		String expertise = searchExpertise.getValue();
		// if textfield is empty, fill it with a *, to search for all expertise
		if (expertise.equals("")) {
			expertise = "*";
		}
		// fetches branche from choicebox
		String branche;
		// if textfield is empty, fill it with a *, to search for all branchen
		if (searchbranche.getValue().equals("")) {
			branche = "*";
		} else {
			branche = searchbranche.getValue();
		}

		// fetches graduation from choicebox
		String graduation = searchnecessarydegree.getValue();
		// fetches salary from textfield
		int salary = Integer.parseInt(Salary.getText());

		List<Entry<Jobangebot, Integer>> searchList;
		try {
			GridPane searchGrid = new GridPane();
			// get current Freelancerprofil
			// get result list
			searchList = Freelancerprofil.sucheJobangebote(cName, graduation, expertise, branche, minEmployees,
					maxEmployees, salary);

			// show message, if no results were found
			if (searchList.isEmpty()) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info");
				alert.setHeaderText("Keine Suchergebnisse");
				alert.setContentText("Es wurde keine Suchergebnisse zu ihren Suchkriterien gefunden.");
				alert.showAndWait();
			} else {
				// display the results
				jA = new JobangebotAnzeige[searchList.size()];
				int index = 0;
				for (Entry<Jobangebot, Integer> entry : searchList) {
					jA[index] = new JobangebotAnzeige();
					jA[index].setJobangebot(entry.getKey());
					// add MouseListener
					jA[index].setOnMouseClicked(this);
					// arrange results
					searchGrid.add(jA[index], index % 3, index / 3);
					index++;
				}

				scrollPane.setContent(searchGrid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		verwaltung = Verwaltung.getInstance();
		// get current Freelancerprofil
		Freelancerprofil f = (Freelancerprofil) verwaltung.getCurrentProfil();

		// fill form with data from Freelancerprofil
		try {
			// fetch all graduations from database
			ObservableList<String> abschlussList = FXCollections
					.observableArrayList(new AbschlussDAO().getAllAbschluss());
			// fetch graduation from Freelancerprofil
			String abschlussFP = f.getAbschluss();
			// fill choicebox
			searchnecessarydegree.setItems(abschlussList);
			searchnecessarydegree.setValue(abschlussFP);

			// fetch all expertises from database
			ObservableList<String> expertises = FXCollections
					.observableArrayList(new ExpertiseDAO().getAllExpertises());
			expertises.add(0, "");
			// fetch expertise from Freelancerprofil
			String expertiseFP = f.getFachgebiet();
			// fill choicebox
			searchExpertise.setItems(expertises);
			searchExpertise.setValue(expertiseFP);

			// fetch all branchen from database
			ObservableList<String> branche = FXCollections.observableArrayList(new BrancheDAO().getAllBranchen());
			branche.add(0, "");
			// fill choicebox
			searchbranche.setItems(branche);
			searchbranche.setValue(branche.get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(MouseEvent aArg0) {
		// get source of event
		Object source = aArg0.getSource();
		// check if event is triggered by search result
		if (Arrays.asList(jA).contains(source)) {
			try {
				// open new stage to show detailed data from Jobangebot
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FJobangebot.fxml"));
				Pane myPane = loader.load();
				ViewFJobangebot controller = loader.getController();
				// transfer this Jobangebot to controller
				controller.setJobangebot(((JobangebotAnzeige) source).getJobangebot());

				Stage stage = new Stage();
				stage.setTitle("X2Success");

				Scene scene = new Scene(myPane);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
