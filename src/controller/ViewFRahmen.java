package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.FreelancerObserver;
import application.Verwaltung;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Freelancerprofil;
import model.Nutzer;

/**
 * Controller class for the view 'FRahmen.fxml'
 * 
 * @author domin
 */
public class ViewFRahmen implements FreelancerObserver, Initializable {

	@FXML
	private Label labelName;
	@FXML
	private ImageView homebutton;
	@FXML
	private ImageView profilbutton;
	@FXML
	private ImageView searchbutton;
	@FXML
	private ImageView settingsbutton;
	@FXML
	private ImageView signoutbutton;
	@FXML
	private ImageView userpicture;
	@FXML
	private ImageView addofferbutton;
	@FXML
	private AnchorPane pane;
	@FXML
	private Label titel;

	private Verwaltung verwaltung;

	/**
	 * Method opens a new stage
	 * 
	 * @param datei
	 */
	private void openStage(String datei) {
		try {
			// close current stage
			Stage prevStage = (Stage) labelName.getScene().getWindow();
			prevStage.close();

			// load new stage
			Stage stage = new Stage();
			stage.setTitle("X2Success");
			Pane myPane = null;
			myPane = FXMLLoader.load(getClass().getResource("/view/" + datei));
			Scene scene = new Scene(myPane);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// opens a new subscene
	private void openSubScene(String datei, String name) {
		try {
			// load new pane
			Pane subPane = FXMLLoader.load(getClass().getResource("/view/" + datei));
			// clear the existing pane
			pane.getChildren().clear();
			// add new pane
			pane.getChildren().add(subPane);
			// change title for this scene
			titel.setText(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		verwaltung = Verwaltung.getInstance();

		// register as Observer to realize changes of Freelancer/Nutzer-Object in
		// Verwaltungs-Object
		verwaltung.registerAsFreelancer(this);
		// get current Nutzer
		Nutzer nutzer = verwaltung.getCurrentNutzer();
		// fill name label
		labelName.setText(nutzer.getFirstName() + " " + nutzer.getLastName());
	}

	/**
	 * opens Home-Subscene
	 * 
	 * @param event
	 */
	@FXML
	void openHome(MouseEvent event) {
		openSubScene("FDashboard.fxml", "Dashboard");
	}

	/**
	 * opens Profil-Subscene
	 * 
	 * @param event
	 */
	@FXML
	void openProfil(MouseEvent event) {
		openSubScene("FProfil.fxml", "Profil bearbeiten");
	}

	/**
	 * opens Search-Subscene
	 * 
	 * @param event
	 */
	@FXML
	void openSearch(MouseEvent event) {
		openSubScene("FSuche.fxml", "Suche");
	}

	/**
	 * opens Settings-Subscene
	 * 
	 * @param event
	 */
	@FXML
	void openSettings(MouseEvent event) {
		openSubScene("Settings.fxml", "Einstellungen");
	}

	/**
	 * opens SignOut-Stage
	 * 
	 * @param event
	 */
	@FXML
	public void openSignOut(MouseEvent event) {
		// logs out the user
		verwaltung.logout();
		openStage("Startseite.fxml");
	}

	/**
	 * updates all references of Freelancer in this stage
	 */
	@Override
	public void updateFreelancer(Freelancerprofil aFreelancer) {
		// there are no references yet
	}

	/**
	 * updates all references of Nutzer in this stage
	 */
	@Override
	public void updateNutzer(Nutzer aNutzer) {
		// update name label
		labelName.setText(aNutzer.getFirstName() + " " + aNutzer.getLastName());
	}

}
