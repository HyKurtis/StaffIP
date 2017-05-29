package com.hyversal.staffip;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.hyversal.staffip.sql.ExecuteResult;
import com.hyversal.staffip.sql.QueryResult;

public class IPManager
{
	public String getLoggedIP(UUID uuid)
	{
		try
		{
			List<QueryResult> results = StaffIPPlugin.get().getDatabaseAPI().query("SELECT ip FROM staffIps WHERE uuid=?", uuid.toString());

			if (!results.isEmpty())
			{
				QueryResult result = results.get(0);
				String ip = result.get("ip");

				return ip;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void setLoggedIP(UUID uuid, String ip)
	{
		try
		{
			StaffIPPlugin.get().getDatabaseAPI().executeWithKeys("INSERT INTO staffIps (uuid,ip) VALUES (?,?)", false, uuid.toString(), ip);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public boolean removeLoggedIP(UUID uuid)
	{
		try
		{
			ExecuteResult result = StaffIPPlugin.get().getDatabaseAPI().executeWithKeys("DELETE FROM staffIps WHERE uuid=?", false, uuid.toString());
			return result.getNumRowsChanged() > 0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
