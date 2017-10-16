package persistence;

import java.sql.SQLException;

import org.junit.Test;

public class SpracheDAOTest {

	@Test
	public void testGetSID() {
		try {
			System.out.println(new SpracheDAO().getSID("Deutsch"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetSprache() {
		try {
			System.out.println(new SpracheDAO().getSprache(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetAllSprachen() {
		try {
			System.out.println(new SpracheDAO().getAllSprachen());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
