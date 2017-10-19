package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.FreelancerObserver;
import application.Verwaltung;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Freelancerprofil;
import model.Nutzer;

/**
 * Controller class for the view 'FRahmen.fxml'
 * 
 * @author domin
 */
public class ViewFRahmen extends ViewRahmen implements FreelancerObserver, Initializable {

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
		openSubScene(pane, "FDashboard.fxml", titel, "Dashboard");
	}

	/**
	 * opens Home-Subscene
	 * 
	 * @param event
	 */
	@FXML
	void openHome(MouseEvent event) {
		openSubScene(pane, "FDashboard.fxml", titel, "Dashboard");
	}

	/**
	 * opens Profil-Subscene
	 * 
	 * @param event
	 */
	@FXML
	void openProfil(MouseEvent event) {
		openSubScene(pane, "FProfil.fxml", titel, "Profil bearbeiten");
	}

	/**
	 * opens Search-Subscene
	 * 
	 * @param event
	 */
	@FXML
	void openSearch(MouseEvent event) {
		openSubScene(pane, "FSuche.fxml", titel, "Suche");
	}

	/**
	 * opens Settings-Subscene
	 * 
	 * @param event
	 */
	@FXML
	void openSettings(MouseEvent event) {
		openSubScene(pane, "Settings.fxml", titel, "Einstellungen");
	}

	/**
	 * opens Login-Stage
	 * 
	 * @param event
	 */
	@FXML
	public void openSignOut(MouseEvent event) {
		// logs out the user
		verwaltung.logout();
		closeStage((Stage) labelName.getScene().getWindow());
		openStage("Startseite.fxml");
	}

	@Override
	public void updateFreelancer(Freelancerprofil aFreelancer) {
		// updates all references of Freelancer in this stage
		// there are no references yet
	}

	@Override
	public void updateNutzer(Nutzer aNutzer) {
		// updates all references of Nutzer in this stage
		// update name label
		labelName.setText(aNutzer.getFirstName() + " " + aNutzer.getLastName());
	}

}
