package com.alexlitovsky.test.derby;

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
public class DerbyUtils {

	static {
		EmbeddedDriver.class.getName();
	}

	/**
	 * JDBC URL template for an in-memory Derby database.
	 * Use {@link String#format(String, Object...)} with a database name to produce a full URL.
	 */
	public static final String JDBC_URL_FORMAT = "jdbc:derby:memory:%s";

	/**
	 * Creates a new in-memory Derby database with the given name.
	 *
	 * @param dbName the name of the database to create
	 * @throws RuntimeException if the database cannot be created
	 */
	public static void createTestDb(String dbName) {
		try {
			DriverManager.getConnection(getJdbcUrl(dbName) + ";create=true").close();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Drops an existing in-memory Derby database with the given name.
	 *
	 * <p>Derby signals a successful drop by throwing {@link SQLNonTransientConnectionException},
	 * which this method silently swallows. Any other {@link SQLException} is rethrown.
	 *
	 * @param dbName the name of the database to destroy
	 * @throws RuntimeException if an unexpected SQL error occurs
	 */
	public static void destroyTestDb(String dbName) {
		try {
            DriverManager.getConnection(getJdbcUrl(dbName) + ";drop=true").close();
        }
		catch (SQLNonTransientConnectionException e) {
			// somehow this is expected
        }
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Opens a JDBC connection to an existing in-memory Derby database.
	 *
	 * @param dbName the name of the database to connect to
	 * @return an open {@link Connection} to the specified database
	 * @throws RuntimeException if the connection cannot be established
	 */
	public static Connection openConnection(String dbName) {
		try {
			return DriverManager.getConnection(getJdbcUrl(dbName));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the JDBC URL for an in-memory Derby database.
	 *
	 * @param dbName the name of the database
	 * @return the JDBC URL string, e.g. {@code jdbc:derby:memory:mydb}
	 */
	public static String getJdbcUrl(String dbName) {
		return String.format(JDBC_URL_FORMAT, dbName);
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
