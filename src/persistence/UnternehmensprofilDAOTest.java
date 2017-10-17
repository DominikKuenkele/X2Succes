/**
 * 
 */
package persistence;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import model.Adresse;
import model.Freelancerprofil;
import model.Unternehmensprofil;
import util.exception.ValidateArgsException;

/**
 * @author domin
 *
 */
public class UnternehmensprofilDAOTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Ignore
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * Test method for
	 * {@link application.Verwaltung#sucheJobangebote(String name, String abschluss, String branche, int minMitarbeiter, int maxMitarbeiter, int minGehalt)}.
	 */
	@Test
	public void testSucheFreelancer() {
		List<String> sprachen = new LinkedList<>();
		sprachen.add("*");
		try {
			List<Entry<Freelancerprofil, Integer>> list;
			list = Unternehmensprofil.sucheFreelancer("Dominik Künkele", "Ausbildung", "*", sprachen);
			for (Entry<Freelancerprofil, Integer> entry : list) {
				System.out.println(entry);
			}
			System.out.println(list.size() + " results");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// for (int i = 0; i < map.size(); i++) {
		// System.out.println(map.entrySet());
		// }
	}

	/**
	 * Test method for
	 * {@link persistence.UnternehmensprofilDAO#addUnternehmensprofil(model.Unternehmensprofil)}.
	 */
	@Ignore
	@Test
	public void testAddUnternehmensprofil() {
		String[] name = { "BurgerKing", "Daimler", "Apple", "Microsoft", "Opel" };
		try {
			for (int i = 0; i < 5; i++) {
				Unternehmensprofil u;
				u = new Unternehmensprofil(name[i], "GmbH", new Adresse("8746", "kjhsk", "gslkfjg", "23"),
						LocalDate.of(1244, 12, 4), 50, "df", "branch", "www.fg.vv", "sdg", "fs",
						new NutzerDAO().getNutzer(22));

				new UnternehmensprofilDAO().addUnternehmensprofil(u);
			}
		} catch (ValidateArgsException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.UnternehmensprofilDAO#getUnternehmensprofil(int)}.
	 */
	@Test
	public void testGetUnternehmensprofil() {
		try {
			System.out.println(new UnternehmensprofilDAO().getUnternehmensprofil(7));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.UnternehmensprofilDAO#getAllUnternehmen()}.
	 */
	@Test
	public void testGetAllUnternehmen() {
		try {
			System.out.println(new UnternehmensprofilDAO().getAllUnternehmen());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.UnternehmensprofilDAO#deleteUnternehmensprofil(int)}.
	 */
	@Ignore
	@Test
	public void testDeleteUnternehmensprofil() {
		try {
			new UnternehmensprofilDAO().deleteUnternehmensprofil(8);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.UnternehmensprofilDAO#changeUnternehmen(model.Unternehmensprofil)}.
	 */
	@Ignore
	@Test
	public void testChangeUnternehmen() {
		try {
			Unternehmensprofil u = new UnternehmensprofilDAO().getUnternehmensprofil(9);

			Unternehmensprofil u2 = new Unternehmensprofil(u.getUid(), u.getName(), u.getLegalForm(), u.getAddress(),
					u.getFounding(), 50000, u.getDescription(), u.getBranche(), u.getWebsite(), "Olaf",
					u.getCeoLastName(), u.getNutzer());
			new UnternehmensprofilDAO().changeUnternehmen(u2);
		} catch (ValidateArgsException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.UnternehmensprofilDAO#getUnternehmensprofilByNutzer(int)}.
	 */
	@Test
	public void testGetUnternehmensprofilBYNutzer() {
		try {
			System.out.println(new UnternehmensprofilDAO().getUnternehmensprofilByNutzer(22));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
