package io.github.alterioncorp.test.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.derby.jdbc.EmbeddedDriver;

/**
 * Utility class for managing in-memory Apache Derby databases.
 *
 * <p>Provides convenience methods for creating, destroying, and connecting to
 * embedded Derby databases backed by memory (no disk persistence). Suitable for
 * use in unit and integration tests that require an isolated, throwaway database.
 */
public class DerbyEmbeddedUtils {

	static {
		EmbeddedDriver.class.getName();
	}

	/** Not instantiable. */
	private DerbyEmbeddedUtils() {}

	/**
	 * Creates a new in-memory Derby database with the given name.
	 *
	 * @param dbName the name of the database to create
	 * @throws SQLException if the database cannot be created
	 */
	public static void createDatabase(String dbName) throws SQLException {
		DriverManager.getConnection(getJdbcUrl(dbName) + ";create=true").close();
	}

	/**
	 * Drops an existing in-memory Derby database with the given name.
	 *
	 * @param dbName the name of the database to destroy
	 * @throws SQLException if an unexpected SQL error occurs
	 */
	public static void dropDatabase(String dbName) throws SQLException {
		try {
            DriverManager.getConnection(getJdbcUrl(dbName) + ";drop=true").close();
        }
		catch (SQLNonTransientConnectionException e) {
			// this is expected
        }
	}

	/**
	 * Opens a JDBC connection to an existing in-memory Derby database.
	 *
	 * @param dbName the name of the database to connect to
	 * @return an open {@link Connection} to the specified database
	 * @throws SQLException if the connection cannot be established
	 */
	public static Connection openConnection(String dbName) throws SQLException {
		return DriverManager.getConnection(getJdbcUrl(dbName));
	}

	private static String getJdbcUrl(String dbName) {
		return String.format("jdbc:derby:memory:%s", dbName);
	}

	/**
	 * Creates a {@link DataSource} for an existing in-memory Derby database.
	 *
	 * @param dbName the name of the database
	 * @return a configured {@link DataSource} backed by the specified in-memory database
	 */
	public static DataSource createDataSource(String dbName) {
		EmbeddedDataSource dataSource = new EmbeddedDataSource();
		dataSource.setDatabaseName("memory:" + dbName);
		return dataSource;
	}
}
