package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ViewFProfilErstellen implements Initializable{
	
	//Liste f�r die Choice Boxen. Aus DB ziehen
	ObservableList<String> TestListe = FXCollections.observableArrayList("Inhalt1","Inhalt2","Inhalt3");
	
	//Liste f�r die Choice Boxen. Aus DB ziehen
		ObservableList<String> Sprachen = 
				FXCollections.observableArrayList("Deutsch","Englisch","Franz�sisch","T�rkisch","Spanisch");


    @FXML
    private ChoiceBox degree1;

    @FXML
    private ChoiceBox topic1;

    @FXML
    private TextArea cv;

    @FXML
    private TextArea skills;

    @FXML
    private TextArea selfDescription;

    @FXML
    private Button continuebutton;

    @FXML
    private ChoiceBox  language1;

    @FXML
    private ChoiceBox language2;

    @FXML
    private ChoiceBox language3;

    @FXML
    private ChoiceBox language4;

    @FXML
    void continuetoDashboard(ActionEvent event) {
    	switchScene("/view/FRahmen");
    }
    
	@FXML
	void switchScene(String fxmlname) {
		try {
			Stage prevStage = (Stage) continuebutton.getScene().getWindow();
			Stage stage = new Stage();
			stage.setTitle("X2Success");
			Pane myPane = null;
			myPane = FXMLLoader.load(getClass().getResource(fxmlname));
			Scene scene = new Scene(myPane);
			stage.setScene(scene);

			prevStage.close();

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void initialize(URL arg0, ResourceBundle arg1) {
		degree1.setValue("Inhalt1"); //Anfangswert
    	degree1.setItems(TestListe); //Name der Liste
    	topic1.setValue("Inhalt1"); //Anfangswert
    	topic1.setItems(TestListe); //Name der Liste
    	language1.setValue("Sprache1"); //Anfangswert
    	language1.setItems(Sprachen); //Name der Liste
    	language2.setValue("Sprache2"); //Anfangswert
    	language2.setItems(Sprachen); //Name der Liste
    	language3.setValue("Sprache3"); //Anfangswert
    	language3.setItems(Sprachen); //Name der Liste
    	language4.setValue("Sprache4"); //Anfangswert
    	language4.setItems(Sprachen); //Name der Liste
		
	}

}
