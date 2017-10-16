package persistence;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Class open the connection to a database
 * 
 * @author domin
 *
 */
public class DataSource {

	private static DataSource datasource;
	private ComboPooledDataSource cpds;

	private DataSource() {
		Properties prop = new Properties();

		try (InputStream input = new FileInputStream("config.properties")) {
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try {
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass("com.mysql.jdbc.Driver");// loads the jdbc driver
			cpds.setJdbcUrl(
					"jdbc:mysql://" + prop.getProperty("database") + "/x2succes?autoReconnect=true&useSSL=false");
			cpds.setUser(prop.getProperty("dbuser"));
			cpds.setPassword(prop.getProperty("dbpassword"));

			// the settings below are optional -- c3p0 can work with defaults
			cpds.setMinPoolSize(3);
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(20);
			cpds.setMaxStatements(180);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

	}

	public static DataSource getInstance() {
		if (datasource == null) {
			datasource = new DataSource();
			return datasource;
		} else {
			return datasource;
		}
	}

	public Connection getConnection() throws SQLException {
		return this.cpds.getConnection();
	}

	// protected final Connection getConnection() throws SQLException {
	// Connection connect = null;
	// try {
	// Class.forName("com.mysql.jdbc.Driver");
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	//
	// String connectionURL = "jdbc:mysql://" + prop.getProperty("database")
	// + "/x2succes?autoReconnect=true&useSSL=false";
	// connect = DriverManager.getConnection(connectionURL,
	// prop.getProperty("dbuser"),
	// prop.getProperty("dbpassword"));
	//
	// return connect;
	// }

}
