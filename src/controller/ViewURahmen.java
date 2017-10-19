package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.UnternehmerObserver;
import application.Verwaltung;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Nutzer;
import model.Unternehmensprofil;

/**
 * Controller class for the view 'URahmen.fxml'
 * 
 * @author domin
 */
public class ViewURahmen extends ViewRahmen implements UnternehmerObserver, Initializable {

	@FXML
	private Label labelName;
	@FXML
	private AnchorPane pane;
	@FXML
	private Label titel;
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

	private Verwaltung verwaltung;

	/**
	 * opens Home-Subscene
	 * 
	 * @param event
	 */
	@FXML
	public void openHome(MouseEvent event) {
		openSubScene(pane, "UDashboard.fxml", titel, "Dashboard");
	}

	/**
	 * opens Profil-Subscene
	 * 
	 * @param event
	 */
	@FXML
	public void openProfil(MouseEvent event) {
		openSubScene(pane, "UProfil.fxml", titel, "Profil bearbeiten");
	}

	/**
	 * opens Search-Subscene
	 * 
	 * @param event
	 */
	@FXML
	public void openSearch(MouseEvent event) {
		openSubScene(pane, "USuche.fxml", titel, "Suche");
	}

	/**
	 * opens Settings-Subscene
	 * 
	 * @param event
	 */
	@FXML
	public void openSettings(MouseEvent event) {
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

	/**
	 * opens NewOffer-Subscene
	 * 
	 * @param event
	 */
	@FXML
	public void createnewoffer(MouseEvent event) {
		openSubScene(pane, "UJobangebotErstellen.fxml", titel, "Jobangebot erstellen");
	}

	@Override
	public void updateUnternehmer(Unternehmensprofil aUnternehmer) {
		// updates all references of Unternehmensprofil in this stage
		// there are no references yet
	}

	@Override
	public void updateNutzer(Nutzer aNutzer) {
		// updates all references of Nutzer in this stage
		// update name label
		labelName.setText(aNutzer.getFirstName() + " " + aNutzer.getLastName());
	}

	@Override
	public void initialize(URL aLocation, ResourceBundle aResources) {
		verwaltung = Verwaltung.getInstance();

		// register as Observer to realize changes of Unternehmensprofil/Nutzer-Object
		// in Verwaltungs-Object
		verwaltung.registerAsUnternehmer(this);
		// get current Nutzer
		Nutzer nutzer = verwaltung.getCurrentNutzer();
		// fill name label
		labelName.setText(nutzer.getFirstName() + " " + nutzer.getLastName());
		openSubScene(pane, "UDashboard.fxml", titel, "Dashboard");
	}
}
