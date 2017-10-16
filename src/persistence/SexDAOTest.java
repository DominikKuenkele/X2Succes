package persistence;

import java.sql.SQLException;

import org.junit.Test;

public class SexDAOTest {

	@Test
	public void testGetSexInt() {
		try {
			System.out.println(new SexDAO().getSex(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetSexString() {
		try {
			System.out.println(new SexDAO().getSex("Männlich"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetAllSex() {
		try {
			System.out.println(new SexDAO().getAllSex());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
