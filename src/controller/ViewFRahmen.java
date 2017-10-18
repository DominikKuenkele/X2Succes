package controller;

import java.io.IOException;

import application.FreelancerObserver;
import application.Verwaltung;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Freelancerprofil;
import model.Nutzer;

public class ViewFRahmen implements FreelancerObserver {

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

	private void openStage(String datei) {
		try {
			Stage prevStage = (Stage) labelName.getScene().getWindow();
			prevStage.close();

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

	private void openSubScene(String datei, String name) {
		try {
			Pane myPane = FXMLLoader.load(getClass().getResource("/view/" + datei));
			pane.getChildren().clear();
			pane.getChildren().add(myPane);
			titel.setText(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initialize() {
		Verwaltung verwaltung = Verwaltung.getInstance();
		verwaltung.registerAsFreelancer(this);
		Nutzer nutzer = verwaltung.getCurrentNutzer();
		labelName.setText(nutzer.getFirstName() + " " + nutzer.getLastName());
	}

	@FXML
	void openHome(MouseEvent event) {
		openSubScene("FDashboard.fxml", "Dashboard");
	}

	@FXML
	void openProfil(MouseEvent event) {
		openSubScene("FProfil.fxml", "Profil bearbeiten");
	}

	@FXML
	void openSearch(MouseEvent event) {
		openSubScene("FSuche.fxml", "Suche");
	}

	@FXML
	void openSettings(MouseEvent event) {
		openSubScene("Settings.fxml", "Einstellungen");
	}

	@FXML
	public void openSignOut(MouseEvent event) {
		Verwaltung verwaltung = Verwaltung.getInstance();
		verwaltung.logout();
		openStage("Startseite.fxml");
	}

	@Override
	public void updateFreelancer(Freelancerprofil aFreelancer) {

	}

	@Override
	public void updateNutzer(Nutzer aNutzer) {
		labelName.setText(aNutzer.getFirstName() + " " + aNutzer.getLastName());
	}

}
