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

	@FXML
	void changeslider(KeyEvent event) {
		salarySlider.setValue(Double.parseDouble(Salary.getText()));
	}

	@FXML
	void dragdone(MouseEvent event) {
		Salary.setText(Double.toString(Math.round(salarySlider.getValue())));
	}

	@FXML
	void searchoffers(ActionEvent event) throws IOException {
		Verwaltung v = Verwaltung.getInstance();

		String cName;
		if (searchcompanyname.getText().equals("")) {
			cName = "*";
		} else {
			cName = searchcompanyname.getText();
		}

		int minEmployees;
		if (minimumemployees.getText().equals("")) {
			minEmployees = 0;
		} else {
			minEmployees = Integer.parseInt(minimumemployees.getText());
		}

		int maxEmployees;
		if (maximumemployees.getText().equals("")) {
			maxEmployees = 10000000;
		} else {
			maxEmployees = Integer.parseInt(maximumemployees.getText());
		}
		String expertise = searchExpertise.getValue();

		String branche;
		if (searchbranche.getValue() == "") {
			branche = "*";
		} else {
			branche = searchbranche.getValue();
		}

		String graduation = searchnecessarydegree.getValue();
		int salary = Integer.parseInt(Salary.getText());

		List<Entry<Jobangebot, Integer>> searchList;

		try {
			GridPane searchGrid = new GridPane();
			Freelancerprofil f = (Freelancerprofil) v.getCurrentProfil();
			searchList = f.sucheJobangebote(cName, graduation, expertise, branche, minEmployees, maxEmployees, salary);
			jA = new JobangebotAnzeige[searchList.size()];

			int index = 0;
			for (Entry<Jobangebot, Integer> entry : searchList) {
				jA[index] = new JobangebotAnzeige();
				jA[index].setJobangebot(entry.getKey());
				jA[index].setOnMouseClicked(this);
				searchGrid.add(jA[index], index % 3, index / 3);
				index++;
			}

			scrollPane.setContent(searchGrid);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Verwaltung v = Verwaltung.getInstance();
		Freelancerprofil f = (Freelancerprofil) v.getCurrentProfil();

		try {
			ObservableList<String> abschlussList = FXCollections
					.observableArrayList(new AbschlussDAO().getAllAbschluss());
			String abschlussFP = f.getAbschluss();
			searchnecessarydegree.setItems(abschlussList);
			searchnecessarydegree.setValue(abschlussFP);

			ObservableList<String> expertises = FXCollections
					.observableArrayList(new ExpertiseDAO().getAllExpertises());
			String expertiseFP = f.getFachgebiet();
			searchExpertise.setItems(expertises);
			searchExpertise.setValue(expertiseFP);

			ObservableList<String> branche = FXCollections.observableArrayList(new BrancheDAO().getAllBranchen());
			branche.add(0, "");
			searchbranche.setItems(branche);
			searchbranche.setValue(branche.get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(MouseEvent aArg0) {
		Object source = aArg0.getSource();
		if (Arrays.asList(jA).contains(source)) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FJobangebot.fxml"));
				Pane myPane = loader.load();
				ViewFJobangebot controller = loader.getController();
				controller.setJobangebot(((JobangebotAnzeige) source).getJobangebot());

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
