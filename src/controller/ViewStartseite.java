package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller class for the view 'Startseite.fxml'
 * 
 * @author domin
 */
public class ViewStartseite {

	@FXML
	private Button LoginButton;

	@FXML
	private Button Regisitrierungsbutton;

	void changescene(String fxmlname) throws IOException {
		// close current stage
		Stage stage2 = (Stage) LoginButton.getScene().getWindow();
		stage2.close();

		Stage stage = new Stage();
		stage.setTitle("X2Success");
		Pane myPane = null;
		myPane = FXMLLoader.load(getClass().getResource(fxmlname));
		Scene scene = new Scene(myPane);
		stage.setScene(scene);
		stage.show();

	}

	/**
	 * open Login-Stage
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Login(ActionEvent event) throws IOException {
		changescene("/view/Einloggen.fxml");
	}

	/**
	 * open Register-Stage
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Registrierung(ActionEvent event) throws IOException {
		changescene("/view/Registrierung.fxml");
	}

}
