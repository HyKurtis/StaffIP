package com.hyversal.staffip.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseAPI
{
	private HikariDataSource dataSource;

	public DatabaseAPI(String jdbc, String username, String password)
	{
		dataSource = new HikariDataSource(getConfig(jdbc, username, password));
	}

	public boolean isValid()
	{
		return dataSource != null && !dataSource.isClosed();
	}

	public Connection getNewConnection() throws SQLException
	{
		return dataSource.getConnection();
	}
	
	public void close()
	{
		if(dataSource != null)
		{
			dataSource.close();
		}
	}

	public ExecuteResult executeWithKeys(String script, boolean returnGeneratedKeys, Object... args) throws SQLException
	{
		try (Connection con = getNewConnection())
		{
			try (PreparedStatement ps = con.prepareStatement(script, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS))
			{
				for (int i = 0; i < args.length; i++)
				{
					ps.setObject(i + 1, args[i]);
				}

				int nRowsChanged = ps.executeUpdate();
				List<Integer> generatedKeys = null;

				if (returnGeneratedKeys)
				{
					try (ResultSet keys = ps.getGeneratedKeys())
					{
						generatedKeys = new ArrayList<>();

						while (keys.next())
						{
							generatedKeys.add(keys.getInt(1));
						}
					}
				}

				return new ExecuteResult(nRowsChanged, generatedKeys);
			}
		}
		catch (SQLException e)
		{
			throw e;
		}
	}

	public List<QueryResult> query(String script, Object... args) throws SQLException
	{
		try (Connection con = getNewConnection())
		{
			try (PreparedStatement ps = con.prepareStatement(script))
			{
				for (int i = 0; i < args.length; i++)
				{
					ps.setObject(i + 1, args[i]);
				}

				return createResults(ps.executeQuery());
			}
		}
	}

	private static List<QueryResult> createResults(ResultSet set) throws SQLException
	{
		List<QueryResult> results = new ArrayList<>();

		while (set.next())
		{
			results.add(new QueryResult(set));
		}

		return results;
	}

	public HikariConfig getConfig(String jdbc, String username, String password)
	{
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbc);
		config.setUsername(username);
		config.setPassword(password);
		config.setConnectionTestQuery("SELECT 1");
		config.setConnectionTimeout(30000);
		config.setIdleTimeout(10000);
		config.setMaximumPoolSize(30);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("useServerPrepStmts", "true");
		config.setConnectionTestQuery("SELECT 1");

		return config;
	}
}
