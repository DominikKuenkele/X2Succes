/**
 * 
 */
package persistence;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author domin
 *
 */
public class AbschlussDAOTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link persistence.AbschlussDAO#getAbschluss(int)}.
	 */
	@Test
	public void testGetAbschlussInt() {
		try {
			System.out.println(new AbschlussDAO().getAbschluss(6));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.AbschlussDAO#getAbschluss(java.lang.String)}.
	 */
	@Test
	public void testGetAbschlussString() {
		try {
			System.out.println(new AbschlussDAO().getAbschluss("Master"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link persistence.AbschlussDAO#getAllAbschluss()}.
	 */
	@Test
	public void testGetAllAbschluss() {
		try {
			System.out.println(new AbschlussDAO().getAllAbschluss());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link persistence.AbschlussDAO#getHierarchy(java.lang.String)}.
	 */
	@Test
	public void testGetHierarchy() {
		try {
			System.out.println(new AbschlussDAO().getHierarchy("Master"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
