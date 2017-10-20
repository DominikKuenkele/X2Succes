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
 * Singleton-Class opens the connection to a database
 * 
 * @author domin
 *
 */
public class DataSource {

	private static DataSource datasource;
	private ComboPooledDataSource cpds;

	private DataSource() {
		Properties prop = new Properties();

		// fetch the user and password from config.properties
		try (InputStream input = new FileInputStream("config.properties")) {
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try {
			// create connectionpool
			cpds = new ComboPooledDataSource();
			// loads the jdbc driver
			cpds.setDriverClass("com.mysql.jdbc.Driver");
			// set url
			cpds.setJdbcUrl(
					"jdbc:mysql://" + prop.getProperty("database") + "/x2succes?autoReconnect=true&useSSL=false");
			// set user
			cpds.setUser(prop.getProperty("dbuser"));
			// set password
			cpds.setPassword(prop.getProperty("dbpassword"));

			cpds.setMinPoolSize(3);
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(20);
			cpds.setMaxStatements(180);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return the instance o this singleton class
	 */
	public static DataSource getInstance() {
		if (datasource == null) {
			datasource = new DataSource();
			return datasource;
		} else {
			return datasource;
		}
	}

	/**
	 * @return the connection of a connectionpool
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		return this.cpds.getConnection();
	}
}
