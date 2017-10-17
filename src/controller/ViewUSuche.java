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

	@FXML
	void searchoffers(ActionEvent event) throws IOException {
		String fName;
		if (searchfreelancername.getText().equals("")) {
			fName = "*";
		} else {
			fName = searchfreelancername.getText();
		}

		String expertise = searchtopic.getValue();

		String graduation = searchdegree.getValue();

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

		List<String> sprachen = new LinkedList<>();
		for (String sprache : sprachenTemp) {
			if (!sprachen.contains(sprache)) {
				sprachen.add(sprache);
			}
		}
		if (sprachen.isEmpty()) {
			sprachen.add("*");
		}

		List<Entry<Freelancerprofil, Integer>> searchList;

		try {
			GridPane searchGrid = new GridPane();

			searchList = Unternehmensprofil.sucheFreelancer(fName, graduation, expertise, sprachen);

			fA = new FreelancerprofilAnzeige[searchList.size()];

			int index = 0;
			for (Entry<Freelancerprofil, Integer> entry : searchList) {
				fA[index] = new FreelancerprofilAnzeige();
				fA[index].setFreelancerprofil(entry.getKey());
				fA[index].setOnMouseClicked(this);
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
			ObservableList<String> sprachen = FXCollections.observableArrayList(new SpracheDAO().getAllSprachen());
			sprachen.add(0, "");
			searchlanguage1.setValue(sprachen.get(0));
			searchlanguage1.setItems((ObservableList<String>) sprachen);
			searchlanguage2.setValue(sprachen.get(0));
			searchlanguage2.setItems((ObservableList<String>) sprachen);
			searchlanguage3.setValue(sprachen.get(0));
			searchlanguage3.setItems((ObservableList<String>) sprachen);
			searchlanguage4.setValue(sprachen.get(0));
			searchlanguage4.setItems((ObservableList<String>) sprachen);

			ObservableList<String> expertises = FXCollections
					.observableArrayList(new ExpertiseDAO().getAllExpertises());
			expertises.add(0, "");
			searchtopic.setValue(expertises.get(0));
			searchtopic.setItems(expertises);

			ObservableList<String> graduation = FXCollections.observableArrayList(new AbschlussDAO().getAllAbschluss());
			searchdegree.setItems(graduation);
			searchdegree.setValue(graduation.get(0));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(MouseEvent aArg0) {
		Object source = aArg0.getSource();

		if (Arrays.asList(fA).contains(source)) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UFreelancerprofil.fxml"));
				Pane myPane = loader.load();
				ViewUFreelancerprofil controller = loader.getController();
				controller.setFreelancer(((FreelancerprofilAnzeige) source).getFreelancerprofil());

				Stage stage = new Stage();
				stage.setTitle("X2Success");

				Scene scene = new Scene(myPane);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
