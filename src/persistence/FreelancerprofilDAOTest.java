/**
 * 
 */
package persistence;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import model.Freelancerprofil;
import util.exception.ValidateArgsException;

/**
 * @author domin
 *
 */
public class FreelancerprofilDAOTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Ignore
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link persistence.FreelancerprofilDAO#addFreelancerprofil(model.Freelancerprofil)}.
	 */
	@Ignore
	@Test
	public void testAddFreelancerprofil() {
		try {
			String[] skills = { "d", "d", "g" };
			List<String> sprachen = new LinkedList<>();
			sprachen.add("Englisch");
			Freelancerprofil f = new Freelancerprofil("Ausbildung", "Chemie", "beschreibung", skills, "lebenslauf",
					sprachen, new NutzerDAO().getNutzer(22));
			System.out.println(new FreelancerprofilDAO().addFreelancerprofil(f));
		} catch (ValidateArgsException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.FreelancerprofilDAO#getFreelancerprofil(int)}.
	 */
	@Test
	public void testGetFreelancerprofil() {
		Freelancerprofil f;
		try {
			f = new FreelancerprofilDAO().getFreelancerprofil(25);
			System.out.println(f);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.FreelancerprofilDAO#getFreelancerprofilByNutzer(int)}.
	 */
	@Test
	public void testGetFreelancerprofilByNutzer() {
		try {
			Freelancerprofil f = new FreelancerprofilDAO().getFreelancerprofilByNutzer(22);
			System.out.println(f);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link persistence.FreelancerprofilDAO#getAllFreelancer()}.
	 */
	@Test
	public void testGetAllFreelancer() {
		try {
			System.out.println(new FreelancerprofilDAO().getAllFreelancer());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.FreelancerprofilDAO#deleteFreelancerprofil(int)}.
	 */
	@Ignore
	@Test
	public void testDeleteFreelancerprofil() {
		try {
			new FreelancerprofilDAO().deleteFreelancerprofil(29);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.FreelancerprofilDAO#changeFreelancerprofil(model.Freelancerprofil)}.
	 */
	@Ignore
	@Test
	public void testChangeFreelancerprofil() {

		List<String> sprachen = new LinkedList<>();
		sprachen.add("Deutsch");
		try {
			Freelancerprofil f = new FreelancerprofilDAO().getFreelancerprofil(27);
			Freelancerprofil f2 = new Freelancerprofil(f.getFID(), f.getAbschluss(), f.getFachgebiet(),
					f.getBeschreibung(), f.getSkills(), "career", sprachen, f.getNutzer());
			new FreelancerprofilDAO().changeFreelancerprofil(f2);
		} catch (ValidateArgsException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.FreelancerprofilDAO#searchForAbschluss(String, String)}.
	 */
	@Ignore
	@Test
	public void testSearchForAbschluss() {
		List<Freelancerprofil> list;
		try {
			list = new FreelancerprofilDAO().searchForAbschluss("Doktor", "*");
			for (Freelancerprofil j : list) {
				System.out.println(j);
			}
			System.out.println(list.size() + " results");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link persistence.FreelancerprofilDAO#searchForGehalt(int)}.
	 */
	@Ignore
	@Test
	public void testSearchForName() {
		List<Freelancerprofil> list;
		try {
			list = new FreelancerprofilDAO().searchForName("K*");
			for (Freelancerprofil j : list) {
				System.out.println(j);
			}
			System.out.println(list.size() + " results");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link persistence.FreelancerprofilDAO#searchForGehalt(int)}.
	 */
	@Ignore
	@Test
	public void testSearchForSprache() {
		List<Freelancerprofil> list;
		List<String> sprachen = new LinkedList<>();
		sprachen.add("Englisch");
		sprachen.add("Französisch");

		try {
			list = new FreelancerprofilDAO().searchForSprache(sprachen);
			for (Freelancerprofil j : list) {
				System.out.println(j);
			}
			System.out.println(list.size() + " results");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
