package controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.Verwaltung;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Adresse;
import model.Unternehmensprofil;
import persistence.BrancheDAO;
import util.exception.DBException;
import util.exception.ValidateArgsException;

/**
 * Controller class for the view 'UProfil.fxml'
 * 
 * @author domin
 */
public class ViewUProfil implements Initializable {

	@FXML
	private TextField cCeoprenom;

	@FXML
	private TextField cCeoname;

	@FXML
	private TextField cForm;

	@FXML
	private TextField cWebsite;

	@FXML
	private TextField cName;

	@FXML
	private DatePicker cDate;

	@FXML
	private TextField cEmployees;

	@FXML
	private TextField cCity;

	@FXML
	private TextField cStreet;

	@FXML
	private TextField cPlz;

	@FXML
	private TextField cNumber;

	@FXML
	private ChoiceBox<String> cBranche;

	@FXML
	private TextArea cDescription;

	@FXML
	private Button changeCompany;

	private Verwaltung verwaltung;

	/**
	 * changes data from existing {@link model.Unternehmensprofil
	 * Unternehmensprofil}
	 * 
	 * @param event
	 */
	@FXML
	void changeUnternehmen(ActionEvent event) {
		try {
			// fetch data from form
			String name = cName.getText();
			String form = cForm.getText();
			LocalDate founding = cDate.getValue();
			int employees = Integer.parseInt(cEmployees.getText());
			String city = cCity.getText();
			String plz = cPlz.getText();
			String street = cStreet.getText();
			String number = cNumber.getText();
			String branche = cBranche.getValue();
			String description = cDescription.getText();
			String website = cWebsite.getText();
			String ceoFirstName = cCeoprenom.getText();
			String ceoLastName = cCeoname.getText();

			try {
				// get the current Unternehmensprofil with its id
				Unternehmensprofil unternehmensprofil = (Unternehmensprofil) verwaltung.getCurrentProfil();
				// change Unternehmensprofil-Object data
				unternehmensprofil.setName(name);
				unternehmensprofil.setLegalForm(form);
				unternehmensprofil.setAddress(new Adresse(plz, city, street, number));
				unternehmensprofil.setFounding(founding);
				unternehmensprofil.setEmployees(employees);
				unternehmensprofil.setDescription(description);
				unternehmensprofil.setBranche(branche);
				unternehmensprofil.setWebsite(website);
				unternehmensprofil.setCeoFirstName(ceoFirstName);
				unternehmensprofil.setCeoLastName(ceoLastName);
				unternehmensprofil.saveToDatabase();

				// show message, if change was successful
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info");
				alert.setHeaderText("Unternehmensprofil ge�ndert");
				alert.setContentText("Das Unternehmensprofil wurde erfolgreich ge�ndert!");
				alert.showAndWait();

			} catch (ValidateArgsException e) {
				// show message, if change failed because of false user input
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("�nderung fehlgeschlagen");
				alert.setContentText(e.getMessage());
				alert.showAndWait();

				e.printStackTrace();
			} catch (DBException e) {
				// show message, if change failed because of failed database acces
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("�nderung fehlgeschlagen");
				alert.setContentText(
						"Auf die Datenbank kann im Moment nicht zugegriffen werden. Versuchen Sie es sp�ter erneut.");
				alert.showAndWait();

				e.printStackTrace();
			}
		} catch (NumberFormatException e) {
			// show message, if change failed because of false user input
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("�nderung fehlgeschlagen");
			alert.setContentText("Das ist keine Zahl!");
			alert.showAndWait();
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// fetch the current Unternehmensprofil
		verwaltung = Verwaltung.getInstance();
		Unternehmensprofil u = (Unternehmensprofil) verwaltung.getCurrentProfil();

		try {
			// Fetch all languages from database
			ObservableList<String> branche = FXCollections.observableArrayList(new BrancheDAO().getAllBranchen());
			// fill choicebox
			cBranche.setItems(branche);
			if (!u.getBranche().equals("")) {
				cBranche.setValue(u.getBranche());
			} else {
				cBranche.setValue(branche.get(0));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// fill form
		cName.setText(u.getName());
		cForm.setText(u.getLegalForm());
		cDate.setValue(u.getFounding());
		cEmployees.setText(Integer.toString(u.getEmployees()));
		cCity.setText(u.getAddress().getCity());
		cStreet.setText(u.getAddress().getStreet());
		cPlz.setText(u.getAddress().getPlz());
		cNumber.setText(u.getAddress().getNumber());
		cDescription.setText(u.getDescription());
		cCeoprenom.setText(u.getCeoFirstName());
		cCeoname.setText(u.getCeoLastName());
		cWebsite.setText(u.getWebsite());
	}
}
