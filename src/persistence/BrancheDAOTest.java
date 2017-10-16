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
public class BrancheDAOTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link persistence.BrancheDAO#getBranche(int)}.
	 */
	@Test
	public void testGetBrancheInt() {
		try {
			System.out.println(new BrancheDAO().getBranche(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link persistence.BrancheDAO#getBID(java.lang.String)}.
	 */
	@Test
	public void testGetBrancheString() {
		try {
			System.out.println(new BrancheDAO().getBID("Chemie"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link persistence.BrancheDAO#getAllBranchen()}.
	 */
	@Test
	public void testGetAllBranchen() {
		try {
			System.out.println(new BrancheDAO().getAllBranchen());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
