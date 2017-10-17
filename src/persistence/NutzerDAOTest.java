package persistence;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.Ignore;
import org.junit.Test;

import model.Adresse;
import model.Nutzer;
import model.Status;
import util.exception.DuplicateEntryException;
import util.exception.ValidateConstrArgsException;

/**
 * @author domin
 *
 */
public class NutzerDAOTest {

	/**
	 * Test method for {@link persistence.NutzerDAO#addNutzer(model.Nutzer)}.
	 */
	@Ignore
	@Test
	public void testAddNutzer() {
		try {
			Nutzer n = new Nutzer("Manuel", "Schmidt", "Männlich", LocalDate.of(2000, 6, 3), "manuel.kuenkele@live.de",
					"pass", new Adresse("77772", "Stuttgart", "Strasse", "20"), Status.U);
			new NutzerDAO().addNutzer(n);
		} catch (ValidateConstrArgsException | DuplicateEntryException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link persistence.NutzerDAO#getNutzer(String)}.
	 */
	@Test
	public void testGetNutzerString() {
		try {
			System.out.println(new NutzerDAO().getNutzer("dominik.kuenkele@outlook.com"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link persistence.NutzerDAO#getNutzer(int)}.
	 */
	@Test
	public void testGetNutzerInt() {
		try {
			System.out.println(new NutzerDAO().getNutzer(22));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link persistence.NutzerDAO#getAllNutzer()}.
	 */
	@Test
	public void testGetAllNutzer() {
		try {
			System.out.println(new NutzerDAO().getAllNutzer());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link persistence.NutzerDAO#changeNutzer(model.Nutzer)}.
	 */
	@Ignore
	@Test
	public void testChangeNutzer() {
		Nutzer test;
		try {
			test = new Nutzer(35, "Manuel", "Schmidt", "Weiblich", LocalDate.of(2000, 6, 3), "manuel.kuenkele@live.de",
					"pass", new Adresse("77772", "Stuttgart", "Strasse", "20"), Status.U);
			new NutzerDAO().changeNutzer(test);
		} catch (ValidateConstrArgsException | SQLException e) {
			e.printStackTrace();
		}

		// fail("Not yet implemented");
	}

	/**
	 * Test method for {@link persistence.NutzerDAO#deleteNutzer(String)}.
	 */
	@Test
	public void testDeleteNutzer() {
		try {
			new NutzerDAO().deleteNutzer(35);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
