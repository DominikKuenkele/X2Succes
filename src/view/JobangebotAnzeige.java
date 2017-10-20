/**
 * 
 */
package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import model.Jobangebot;

/**
 * Class is a view to show a preview of a {@link model.Jobangebot Jobangebot}
 * 
 * @author domin
 *
 */
public class JobangebotAnzeige extends AnchorPane {

	private ImageView imgImageView;
	private Label nameLabel;
	private Label gruendungLabel;
	private Label mitarbeiterLabel;
	private Label jobTitelLabel;
	private Label anforderungenLabel;
	private Label wochenstundenLabel;
	private Label gehaltLabel;

	private final int SIZE_IMAGE = 70;
	// location of image
	private final int LOC_X_IMG = 20;
	private final int LOC_Y_IMG = 20;
	// location of namelabel
	private final int LOC_X_NAME = LOC_X_IMG + SIZE_IMAGE + 5;
	private final int LOC_Y_NAME = LOC_Y_IMG;
	private final int LOC_X_DESC = LOC_X_IMG + 15;
	private final int LOC_Y_DESC = LOC_Y_IMG + SIZE_IMAGE + 30;

	// distance between objects
	private final int DIST_Y = 25;

	// fonts
	private final String EMPH_FONT = "System Bold";
	private final int EMPH_SIZE = 14;

	// text modulars
	private final String GRUENDUNG_TEXT = "Gründung: ";
	private final String MITARBEITER_TEXT = "Mitarbeiter: ";
	private final String ANFORDERUNG_TEXT = "Anforderung: ";
	private final String WOCHENSTUNDEN_EINHEIT = " Stunden/Woche";
	private final String GEHALT_EINHEIT = " EURO/Monat";

	private Jobangebot jobangebot;

	/**
	 * Constructor builds the view
	 */
	public JobangebotAnzeige() {
		super();
		// size of view
		setMinWidth(220);
		setMinHeight(260);
		try {
			imgImageView = new ImageView();
			imgImageView.setFitHeight(SIZE_IMAGE);
			imgImageView.setFitWidth(SIZE_IMAGE);
			imgImageView.setLayoutX(LOC_X_IMG);
			imgImageView.setLayoutY(LOC_Y_IMG);
			imgImageView.setPickOnBounds(true);
			imgImageView.setPreserveRatio(true);
			InputStream inputStream = new FileInputStream("src/view/Icons/company_icon.png");
			Image img = new Image(inputStream);
			imgImageView.setImage(img);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}

		// namelabel
		nameLabel = new Label("BurgerKing");
		nameLabel.setLayoutX(LOC_X_NAME);
		nameLabel.setLayoutY(LOC_Y_NAME);
		nameLabel.setFont(new Font(EMPH_FONT, EMPH_SIZE));

		// foundinglabel
		gruendungLabel = new Label(GRUENDUNG_TEXT);
		gruendungLabel.setLayoutX(LOC_X_NAME);
		gruendungLabel.setLayoutY(nameLabel.getLayoutY() + DIST_Y);

		// employeeslabel
		mitarbeiterLabel = new Label(MITARBEITER_TEXT);
		mitarbeiterLabel.setLayoutX(LOC_X_NAME);
		mitarbeiterLabel.setLayoutY(gruendungLabel.getLayoutY() + DIST_Y);

		// jobtitlelabel
		jobTitelLabel = new Label();
		jobTitelLabel.setLayoutX(LOC_X_DESC);
		jobTitelLabel.setLayoutY(LOC_Y_DESC);
		jobTitelLabel.setFont(new Font(EMPH_FONT, EMPH_SIZE));

		// graduationslabel
		anforderungenLabel = new Label(ANFORDERUNG_TEXT);
		anforderungenLabel.setLayoutX(LOC_X_DESC);
		anforderungenLabel.setLayoutY(jobTitelLabel.getLayoutY() + DIST_Y);

		// weeklyhourslabel
		wochenstundenLabel = new Label(WOCHENSTUNDEN_EINHEIT);
		wochenstundenLabel.setLayoutX(LOC_X_DESC);
		wochenstundenLabel.setLayoutY(anforderungenLabel.getLayoutY() + DIST_Y);

		// salarylabel
		gehaltLabel = new Label(GEHALT_EINHEIT);
		gehaltLabel.setLayoutX(LOC_X_DESC);
		gehaltLabel.setLayoutY(wochenstundenLabel.getLayoutY() + DIST_Y);

		setCursor(Cursor.HAND);

		getChildren().add(imgImageView);
		getChildren().add(nameLabel);
		getChildren().add(gruendungLabel);
		getChildren().add(mitarbeiterLabel);
		getChildren().add(anforderungenLabel);
		getChildren().add(jobTitelLabel);
		getChildren().add(wochenstundenLabel);
		getChildren().add(gehaltLabel);

	}

	/**
	 * @return the jobangebot
	 */
	public Jobangebot getJobangebot() {
		return this.jobangebot;
	}

	/**
	 * sets the Jobangebot for this view
	 * 
	 * @param jobangebot
	 */
	public void setJobangebot(Jobangebot jobangebot) {
		setName(jobangebot.getUnternehmensprofil().getName());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.LL.yyyy");
		String formattedDate = jobangebot.getUnternehmensprofil().getFounding().format(formatter);
		setGruendung(formattedDate);
		setMitarbieter(Integer.toString(jobangebot.getUnternehmensprofil().getEmployees()));
		setJobTitel(jobangebot.getJobTitel());
		setAnforderungen(jobangebot.getAbschluss());
		setWochenstunden(Integer.toString(jobangebot.getWochenstunden()));
		setGehalt(Integer.toString(jobangebot.getGehalt()));
		this.jobangebot = jobangebot;
	}

	private void setName(String name) {
		nameLabel.setText(name);
	}

	private void setGruendung(String gruendung) {
		gruendungLabel.setText(GRUENDUNG_TEXT + gruendung);
	}

	private void setMitarbieter(String mitarbeiter) {
		mitarbeiterLabel.setText(MITARBEITER_TEXT + mitarbeiter);
	}

	private void setJobTitel(String beschreibung) {
		jobTitelLabel.setText(beschreibung);
	}

	private void setAnforderungen(String anforderungen) {
		anforderungenLabel.setText(ANFORDERUNG_TEXT + anforderungen);
	}

	private void setWochenstunden(String wochenstunden) {
		wochenstundenLabel.setText(wochenstunden + WOCHENSTUNDEN_EINHEIT);
	}

	private void setGehalt(String gehalt) {
		gehaltLabel.setText(gehalt + GEHALT_EINHEIT);
	}
}
