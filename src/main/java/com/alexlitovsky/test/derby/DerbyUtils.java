package com.alexlitovsky.test.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.derby.jdbc.EmbeddedDriver;

public class DerbyUtils {

	static {
		EmbeddedDriver.class.getName();
	}

	public static final String JDBC_URL_FORMAT = "jdbc:derby:memory:%s";

	public static void createTestDb(String dbName) {
		try {
			DriverManager.getConnection(getJdbcUrl(dbName) + ";create=true").close();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

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

	public static Connection openConnection(String dbName) {
		try {
			return DriverManager.getConnection(getJdbcUrl(dbName));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getJdbcUrl(String dbName) {
		return String.format(JDBC_URL_FORMAT, dbName);
	}

	public static DataSource createDataSource(String dbName) {
		EmbeddedDataSource dataSource = new EmbeddedDataSource();
		dataSource.setDatabaseName("memory:" + dbName);
		return dataSource;
	}
}
