package controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.Freelancerprofil;
import model.Nutzer;

/**
 * Controller class for the view 'UFreelancerrofil.fxml'
 * 
 * @author domin
 */
public class ViewUFreelancerprofil {

	@FXML
	private Label username;

	@FXML
	private ImageView Freelancerpicture;

	@FXML
	private Label skills;

	@FXML
	private Label contactmail;

	@FXML
	private ImageView star;

	@FXML
	private TextArea description;

	@FXML
	private TextArea career;

	@FXML
	private Label degree;

	@FXML
	private Label languages;

	private Freelancerprofil freelancerprofil;

	/**
	 * @param event
	 */
	@FXML
	void addfavorite(MouseEvent event) {
		// not yet implemented

		// // Pfad ändern ist glaub falsch?!
		// if (star.getOpacity() == 1) {
		// star.setImage(new Image("url=@Icons/stern_voll.png"));
		// star.setOpacity(0.99);
		// // Favorit speichern
		//
		// } else {
		// star.setImage(new Image("url=@Icons/stern_leer.png"));
		// star.setOpacity(1);
		// // Favorit löschen
		// }
	}

	/**
	 * opens mailprogram
	 * 
	 * @param event
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@FXML
	void mailTo(MouseEvent event) throws URISyntaxException, IOException {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.MAIL)) {
				URI mailto = new URI("mailto:" + freelancerprofil.getNutzer().geteMail());
				desktop.mail(mailto);
			}
		}
	}

	/**
	 * fills the form of the scene with a given {@link model.Freelancerprofil
	 * Freelancerprofil}
	 */
	public void fillForm() {
		// fetch Nutzer from Freelancerprofil
		Nutzer n = freelancerprofil.getNutzer();

		// fill form
		username.setText(n.getFirstName() + " " + n.getLastName());
		description.setText(freelancerprofil.getBeschreibung());
		degree.setText(freelancerprofil.getAbschluss() + " in " + freelancerprofil.getFachgebiet());
		career.setText(freelancerprofil.getLebenslauf());
		// display all languages, separated with commas
		List<String> sprachen = freelancerprofil.getSprachen();
		Iterator<String> it = sprachen.iterator();
		for (String sprache : sprachen) {
			languages.setText(languages.getText() + it.next());
			if (it.hasNext()) {
				languages.setText(languages.getText() + ", ");
			}
		}
		// display all skills, separated with new lines
		String[] skillsArray = freelancerprofil.getSkills();
		for (String skill : skillsArray) {
			skills.setText(skills.getText() + skill + "\n");
		}

		contactmail.setText(n.geteMail());
	}

	/**
	 * fills the form and sets the {@link model.Freelancerprofil Freelancerprofil}
	 * 
	 * @param aFreelancerprofil
	 */
	public void setFreelancer(Freelancerprofil aFreelancerprofil) {
		this.freelancerprofil = aFreelancerprofil;
		fillForm();
	}

}
