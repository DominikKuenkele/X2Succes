/**
 * 
 */
package controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author domin
 *
 */
public abstract class ViewRahmen {

	protected void closeStage(Stage stage) {
		// close stage
		stage.close();
	}

	/**
	 * Method opens a new stage
	 * 
	 * @param datei
	 */
	protected void openStage(String datei) {
		try {
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

	/**
	 * Method opens a new subscene in the parentNode and sets the title
	 * 
	 * @param parentNode
	 * @param datei
	 * @param title
	 * @param name
	 */
	protected void openSubScene(AnchorPane parentNode, String datei, Label title, String name) {
		try {
			// load new pane
			Pane subPane = FXMLLoader.load(getClass().getResource("/view/" + datei));
			// clear the existing pane
			parentNode.getChildren().clear();
			// add new pane
			parentNode.getChildren().add(subPane);
			// change title for this scene
			title.setText(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
