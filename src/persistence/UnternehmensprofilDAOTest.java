/**
 * 
 */
package persistence;

import java.time.LocalDate;

import org.junit.Test;

import application.Verwaltung;
import model.Adresse;
import model.Nutzer;
import model.Unternehmensprofil;
import util.exception.ValidateConstrArgsException;

/**
 * @author domin
 *
 */
public class UnternehmensprofilDAOTest {

	/**
	 * @throws java.lang.Exception
	 */
	// @Before
	// public void setUp() throws Exception {
	// Verwaltung v = new Verwaltung();
	// v.createUnternehmen("Test", "GmbH", "8746", "kjhsk", "gslkfjg", "23",
	// LocalDate.of(1244, 12, 4), 50, "sd", "df",
	// "df", "www.fg.vv", "sdg", "fs");
	// }
	//
	// /**
	// * Test method for {@link
	// persistence.UnternehmensprofilDAO#addUnternehmensprofil(model.Unternehmensprofil)}.
	// */
	// @Test
	// public void testAddUnternehmensprofil() {
	// fail("Not yet implemented");
	// }
	//
	// /**
	// * Test method for {@link
	// persistence.UnternehmensprofilDAO#getUnternehmensprofil(int)}.
	// */
	// @Test
	// public void testGetUnternehmensprofil() {
	// fail("Not yet implemented");
	// }
	//
	// /**
	// * Test method for {@link
	// persistence.UnternehmensprofilDAO#getAllUnternehmen()}.
	// */
	// @Test
	// public void testGetAllUnternehmen() {
	// fail("Not yet implemented");
	// }
	//
	// /**
	// * Test method for {@link
	// persistence.UnternehmensprofilDAO#deleteUnternehmensprofil(int)}.
	// */
	// @Test
	// public void testDeleteUnternehmensprofil() {
	// fail("Not yet implemented");
	// }

	/**
	 * Test method for
	 * {@link persistence.UnternehmensprofilDAO#changeUnternehmen(model.Unternehmensprofil)}.
	 */
	@Test
	public void testChangeUnternehmen() {
		Verwaltung v = new Verwaltung();
		v.login("olaf.muelle@hsdf.de", "1234");
		Nutzer nutzer = v.getCurrentNutzer();
		Unternehmensprofil unternehmen;
		try {
			unternehmen = new Unternehmensprofil("Test", "GmbH", new Adresse("8746", "kjhsk", "gslkfjg", "23"),
					LocalDate.of(1244, 12, 4), 50, "sd", "df", "df", "www.fg.vv", "sdg", "fs", nutzer);
			unternehmen.setId(1);
			new UnternehmensprofilDAO().changeUnternehmen(unternehmen);
		} catch (ValidateConstrArgsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}