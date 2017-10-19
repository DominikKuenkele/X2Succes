package controller;

import java.io.IOException;

import application.Verwaltung;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.exception.DBException;

/**
 * Controller class for the view 'Einloggen.fxml'
 * 
 * @author domin
 */
public class ViewEinloggen {

	@FXML
	private TextField EmailEingabe;

	@FXML
	private PasswordField PasswortEingabe;

	@FXML
	private Button LoginButton;

	/**
	 * Method opens a new Stage
	 * 
	 * @param fxmlname
	 * @throws IOException
	 */
	private void changescene(final String fxmlname) throws IOException {
		// fetch and close current stage
		final Stage stage2 = (Stage) this.LoginButton.getScene().getWindow();
		stage2.close();

		// open new stage
		final Stage stage = new Stage();
		stage.setTitle("X2Success");
		Pane myPane = FXMLLoader.load(getClass().getResource(fxmlname));
		final Scene scene = new Scene(myPane);
		stage.setScene(scene);
		stage.show();

	}

	@FXML
	void login(final ActionEvent event) throws IOException {
		Verwaltung v = Verwaltung.getInstance();

		// fetching user input
		String eMail = EmailEingabe.getText();
		String password = PasswortEingabe.getText();

		try {
			if (v.login(eMail, password)) {
				// open right stage depending on status of user
				switch (v.getCurrentNutzer().getStatus()) {
				case F:
					changescene("/view/FRahmen.fxml");
					break;
				case U:
					changescene("/view/URahmen.fxml");
					break;
				default:

				}

			} else {
				// error message for failed login
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Login fehlgeschlagen");
				alert.setContentText("E-Mail-Adresse oder Passwort sind falsch!");
				alert.showAndWait();
			}
		} catch (DBException e) {
			// error message for failed acces
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Login fehlgeschlagen");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}
}
