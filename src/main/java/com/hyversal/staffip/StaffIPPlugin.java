package com.hyversal.staffip;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.hyversal.staffip.commands.ResetIPCommand;
import com.hyversal.staffip.listeners.PlayerListener;
import com.hyversal.staffip.sql.DatabaseAPI;

public class StaffIPPlugin extends JavaPlugin
{
	private static StaffIPPlugin main;

	private DatabaseAPI databaseAPI;
	private IPManager ipManager;

	@Override
	public void onEnable()
	{
		main = this;

		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();
		
		ipManager = new IPManager();

		registerDatabase();
		registerListeners();
		registerCommands();
	}

	@Override
	public void onDisable()
	{
		if (getDatabaseAPI() != null)
		{
			getDatabaseAPI().close();
		}
	}

	public static StaffIPPlugin get()
	{
		return main;
	}
	
	public DatabaseAPI getDatabaseAPI()
	{
		return databaseAPI;
	}
	
	public IPManager getIPManager()
	{
		return ipManager;
	}

	public void registerListeners()
	{
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}

	public void registerCommands()
	{
		getCommand("resetip").setExecutor(new ResetIPCommand());
	}

	public void registerDatabase()
	{
		FileConfiguration config = getConfig();
		try
		{
			databaseAPI = new DatabaseAPI("jdbc:mysql://" + config.getString("mysql.ip") + "/" + config.getString("mysql.database"), config.getString("mysql.user"), config.getString("mysql.password"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			setEnabled(false);
		}
	}
}
