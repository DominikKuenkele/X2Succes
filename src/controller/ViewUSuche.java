package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Freelancerprofil;
import model.Unternehmensprofil;
import persistence.AbschlussDAO;
import persistence.ExpertiseDAO;
import persistence.SpracheDAO;
import view.FreelancerprofilAnzeige;

/**
 * Controller class for the view 'USuche.fxml'
 * 
 * @author domin
 */
public class ViewUSuche implements Initializable, EventHandler<MouseEvent> {

	private FreelancerprofilAnzeige[] fA;

	@FXML
	private TextField searchfreelancername;

	@FXML
	private ChoiceBox<String> searchdegree;

	@FXML
	private ChoiceBox<String> searchtopic;

	@FXML
	private Button serachoffers;

	@FXML
	private ChoiceBox<String> searchlanguage1;

	@FXML
	private ChoiceBox<String> searchlanguage2;

	@FXML
	private ChoiceBox<String> searchlanguage3;

	@FXML
	private ChoiceBox<String> searchlanguage4;

	@FXML
	private ScrollPane scrollPane;

	/**
	 * searches and displays {@link model.Freelancerprofil Freelancerprofile}
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void searchoffers(ActionEvent event) throws IOException {
		// fetches name from textfield
		String fName;
		// if textfield is empty, fill it with a *, to search for all names
		if (searchfreelancername.getText().equals("")) {
			fName = "*";
		} else {
			fName = searchfreelancername.getText();
		}

		// fetches expertise employees from choicebox
		String expertise = searchtopic.getValue();

		// fetches graduation employees from choicebox
		String graduation = searchdegree.getValue();

		// fetches languages from choiceboxes
		List<String> sprachenTemp = new LinkedList<>();
		if (!searchlanguage1.getValue().equals("")) {
			sprachenTemp.add(searchlanguage1.getValue());
		}
		if (!searchlanguage2.getValue().equals("")) {
			sprachenTemp.add(searchlanguage2.getValue());
		}
		if (!searchlanguage3.getValue().equals("")) {
			sprachenTemp.add(searchlanguage3.getValue());
		}
		if (!searchlanguage4.getValue().equals("")) {
			sprachenTemp.add(searchlanguage4.getValue());
		}

		// remove duplicates
		List<String> sprachen = new LinkedList<>();
		for (String sprache : sprachenTemp) {
			if (!sprachen.contains(sprache)) {
				sprachen.add(sprache);
			}
		}
		// if list is empty, fill it with a *, to search for all languages
		if (sprachen.isEmpty()) {
			sprachen.add("*");
		}

		List<Entry<Freelancerprofil, Integer>> searchList;

		try {
			GridPane searchGrid = new GridPane();

			// get result list
			searchList = Unternehmensprofil.sucheFreelancer(fName, graduation, expertise, sprachen);
			System.out.println(searchList);
			// display the results
			fA = new FreelancerprofilAnzeige[searchList.size()];
			int index = 0;
			for (Entry<Freelancerprofil, Integer> entry : searchList) {
				fA[index] = new FreelancerprofilAnzeige();
				fA[index].setFreelancerprofil(entry.getKey());
				// add MouseListener
				fA[index].setOnMouseClicked(this);
				// arrange results
				searchGrid.add(fA[index], index % 3, index / 3);
				index++;
			}

			scrollPane.setContent(searchGrid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			// fetch all languages from database
			ObservableList<String> sprachen = FXCollections.observableArrayList(new SpracheDAO().getAllSprachen());
			sprachen.add(0, "");
			// fill choiceboxes with all languages
			searchlanguage1.setValue(sprachen.get(0));
			searchlanguage1.setItems((ObservableList<String>) sprachen);
			searchlanguage2.setValue(sprachen.get(0));
			searchlanguage2.setItems((ObservableList<String>) sprachen);
			searchlanguage3.setValue(sprachen.get(0));
			searchlanguage3.setItems((ObservableList<String>) sprachen);
			searchlanguage4.setValue(sprachen.get(0));
			searchlanguage4.setItems((ObservableList<String>) sprachen);

			// fetch all expertises from database
			ObservableList<String> expertises = FXCollections
					.observableArrayList(new ExpertiseDAO().getAllExpertises());
			expertises.add(0, "");
			// fill choiceboxes with all expertises
			searchtopic.setValue(expertises.get(0));
			searchtopic.setItems(expertises);

			// fetch all graduations from database
			ObservableList<String> graduation = FXCollections.observableArrayList(new AbschlussDAO().getAllAbschluss());
			// fill choiceboxes with all graduations
			searchdegree.setItems(graduation);
			searchdegree.setValue(graduation.get(0));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(MouseEvent aArg0) {
		// get source of event
		Object source = aArg0.getSource();
		// check if event is triggered by search result
		if (Arrays.asList(fA).contains(source)) {
			try {
				// open new stage to show detailed data from Jobangebot
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UFreelancerprofil.fxml"));
				Pane myPane = loader.load();
				ViewUFreelancerprofil controller = loader.getController();
				// transfer this Freelancerprofil to controller
				controller.setFreelancer(((FreelancerprofilAnzeige) source).getFreelancerprofil());

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
