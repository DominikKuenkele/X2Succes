/**
 * 
 */
package persistence;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import model.Jobangebot;
import util.exception.ValidateArgsException;

/**
 * @author domin
 *
 */
public class JobangebotDAOTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link persistence.JobangebotDAO#addJobangebot(model.Jobangebot)}.
	 */
	@Ignore
	@Test
	public void testAddJobangebot() {
		List<String> sprachen = new LinkedList<>();
		sprachen.add("Englisch");
		Jobangebot[] j = new Jobangebot[5];
		try {
			for (int i = 0; i < j.length; i++) {
				j[i] = new Jobangebot("Ausbildung", "Banken", sprachen, "jobtitel", "beschreibung",
						LocalDate.of(2019, 10, 5), 100, 20, new UnternehmensprofilDAO().getUnternehmensprofil(i + 7));

				new JobangebotDAO().addJobangebot(j[i]);
				// j.setId(jid);
				// System.out.println(j);
			}
		} catch (ValidateArgsException | SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test method for {@link persistence.JobangebotDAO#getJobangebot(int)}.
	 */
	@Test
	public void testGetJobangebot() {
		try {
			System.out.println(new JobangebotDAO().getJobangebot(2));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link persistence.JobangebotDAO#getAllJobangebote()}.
	 */
	@Test
	public void testGetAllJobangebote() {
		List<Jobangebot> j;
		try {
			j = new JobangebotDAO().getAllJobangebote();
			for (Jobangebot s : j) {
				System.out.println(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link persistence.JobangebotDAO#deleteJobangebot(int)}.
	 */
	@Ignore
	@Test
	public void testDeleteJobangebot() {
		try {
			new JobangebotDAO().deleteJobangebot(10);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.JobangebotDAO#changeJobangebot(model.Jobangebot)}.
	 */
	@Ignore
	@Test
	public void testChangeJobangebot() {
		List<String> sprachen = new LinkedList<>();
		sprachen.add("Deutsch");
		try {
			Jobangebot j = new JobangebotDAO().getJobangebot(11);
			Jobangebot j2 = new Jobangebot(j.getJID(), "Master", "Chemie", j.getSprachen(), j.getJobTitel(),
					j.getBeschreibung(), j.getFrist(), 5000, j.getWochenstunden(), j.getUnternehmensprofil());
			new JobangebotDAO().changeJobangebot(j2);
		} catch (ValidateArgsException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.JobangebotDAO#searchForName(java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testSearchForName() {
		List<Jobangebot> list;
		try {
			list = new JobangebotDAO().searchForName("*le");
			for (Jobangebot j : list) {
				System.out.println(j);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link persistence.JobangebotDAO#searchForBranche(java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testSearchForBranche() {
		List<Jobangebot> list;
		try {
			list = new JobangebotDAO().searchForBranche("A*");
			for (Jobangebot j : list) {
				System.out.println(j);
			}
			System.out.println(list.size() + " results");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link persistence.JobangebotDAO#searchForAbschluss(java.lang.String, java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testSearchForAbschluss() {
		List<Jobangebot> list;
		try {
			list = new JobangebotDAO().searchForAbschluss("Ausbildung", "*");
			for (Jobangebot j : list) {
				System.out.println(j);
			}
			System.out.println(list.size() + " results");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link persistence.JobangebotDAO#searchForGehalt(int)}.
	 */
	@Ignore
	@Test
	public void testSearchForGehalt() {
		List<Jobangebot> list;
		try {
			list = new JobangebotDAO().searchForGehalt(100);
			for (Jobangebot j : list) {
				System.out.println(j);
			}
			System.out.println(list.size() + " results");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.JobangebotDAO#searchForMitarbeiter(int, int)}.
	 */
	@Ignore
	@Test
	public void testSearchForMitarbeiter() {
		List<Jobangebot> list;
		try {
			list = new JobangebotDAO().searchForMitarbeiter(100, 400);
			for (Jobangebot j : list) {
				System.out.println(j);
			}
			System.out.println(list.size() + " results");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
