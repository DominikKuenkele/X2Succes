package persistence;

import java.sql.SQLException;

import org.junit.Test;

public class ExpertiseDAOTest {

	@Test
	public void testGetExpertiseInt() {
		try {
			System.out.println(new ExpertiseDAO().getExpertise(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetExpertiseString() {
		try {
			System.out.println(new ExpertiseDAO().getExpertise("Agrar- & Forstwissenschaft"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetAllExpertises() {
		try {
			System.out.println(new ExpertiseDAO().getAllExpertises());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
