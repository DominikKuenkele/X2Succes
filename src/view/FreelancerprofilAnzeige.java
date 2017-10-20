/**
 * 
 */
package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import model.Freelancerprofil;

/**
 * Class is a view to show a preview of a {@link model.Freelancerprofil
 * Freelancerprofil}
 * 
 * @author domin
 *
 */
public class FreelancerprofilAnzeige extends AnchorPane {

	private ImageView imgImageView;
	private Label nameLabel;
	private Label abschlussLabel;
	private Label expertiseLabel;
	private Label skillsTitleLabel;
	private Label skillsLabel;

	private final String EMPH_FONT = "System Bold";
	private final int EMPH_SIZE = 14;

	private final int SIZE_IMAGE = 70;
	// location of image
	private final int LOC_X_IMG = 20;
	private final int LOC_Y_IMG = 20;
	// location of namelabel
	private final int LOC_X_NAME = LOC_X_IMG + SIZE_IMAGE + 5;
	private final int LOC_Y_NAME = LOC_Y_IMG + (SIZE_IMAGE / 2) - EMPH_SIZE / 2;
	private final int LOC_X_DESC = LOC_X_IMG;
	private final int LOC_Y_DESC = LOC_Y_IMG + SIZE_IMAGE + 10;

	// distance between objects
	private final int DIST_Y = 25;
	private final int DIST_Y_SMALL = 18;

	// text modulars
	private final String EXPERTISE_TEXT = "in ";
	private final String SKILLS_TEXT = "Skills";

	private Freelancerprofil freelancerprofil;

	/**
	 * Constructor builds the view
	 */
	public FreelancerprofilAnzeige() {
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
			InputStream inputStream = new FileInputStream("src/view/Icons/dtu-jnUc.jpg");
			Image img = new Image(inputStream);
			imgImageView.setImage(img);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}

		// namelabel
		nameLabel = new Label();
		nameLabel.setLayoutX(LOC_X_NAME);
		nameLabel.setLayoutY(LOC_Y_NAME);
		nameLabel.setFont(new Font(EMPH_FONT, EMPH_SIZE));

		// graduationlabel
		abschlussLabel = new Label();
		abschlussLabel.setLayoutX(LOC_X_DESC);
		abschlussLabel.setLayoutY(LOC_Y_DESC);

		// expertiselabel
		expertiseLabel = new Label(EXPERTISE_TEXT);
		expertiseLabel.setLayoutX(LOC_X_DESC);
		expertiseLabel.setLayoutY(abschlussLabel.getLayoutY() + DIST_Y_SMALL);

		// skillstitlelabel
		skillsTitleLabel = new Label(SKILLS_TEXT);
		skillsTitleLabel.setLayoutX(LOC_X_DESC);
		skillsTitleLabel.setLayoutY(expertiseLabel.getLayoutY() + DIST_Y);
		skillsTitleLabel.setFont(new Font(EMPH_FONT, EMPH_SIZE));

		// skillslabel
		skillsLabel = new Label();
		skillsLabel.setLayoutX(LOC_X_DESC);
		skillsLabel.setLayoutY(skillsTitleLabel.getLayoutY() + DIST_Y_SMALL);

		setCursor(Cursor.HAND);

		getChildren().add(imgImageView);
		getChildren().add(nameLabel);
		getChildren().add(abschlussLabel);
		getChildren().add(expertiseLabel);
		getChildren().add(skillsLabel);
		getChildren().add(skillsTitleLabel);
	}

	/**
	 * @return the freelancerprofil
	 */
	public Freelancerprofil getFreelancerprofil() {
		return this.freelancerprofil;
	}

	/**
	 * sets the Freelancerprofil for this view
	 * 
	 * @param freelancerprofil
	 */
	public void setFreelancerprofil(Freelancerprofil freelancerprofil) {
		setName(freelancerprofil.getNutzer().getFirstName() + " " + freelancerprofil.getNutzer().getLastName());
		setAbschluss(freelancerprofil.getAbschluss());
		setExpertise(freelancerprofil.getFachgebiet());
		setSkills(freelancerprofil.getSkills());
		this.freelancerprofil = freelancerprofil;
	}

	private void setName(String name) {
		nameLabel.setText(name);
	}

	private void setAbschluss(String abschluss) {
		abschlussLabel.setText(abschluss);
	}

	private void setExpertise(String expertise) {
		expertiseLabel.setText(EXPERTISE_TEXT + expertise);
	}

	private void setSkills(String[] skills) {
		for (String skill : skills) {
			skillsLabel.setText(skillsLabel.getText() + skill + "\n");
		}
	}
}
