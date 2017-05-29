package com.hyversal.staffip.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.hyversal.staffip.IPManager;
import com.hyversal.staffip.StaffIPPlugin;

import net.md_5.bungee.api.ChatColor;

public class ResetIPCommand implements CommandExecutor
{
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender.hasPermission("staffip.reset"))
		{
			IPManager ipManager = StaffIPPlugin.get().getIpManager();

			if (args.length > 0)
			{
				String name = args[0];
				OfflinePlayer target = Bukkit.getOfflinePlayer(name);

				if (target != null)
				{
					boolean result = ipManager.removeLoggedIP(target.getUniqueId());

					if (result)
					{
						sender.sendMessage(ChatColor.YELLOW + "Successfully reset " + target.getName() + "'s IP address.");
					}
					else
					{
						sender.sendMessage(ChatColor.RED + target.getName() + " does not have a logged IP address.");
					}
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "Player not found.");
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Usage: /resetip [name]");
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "No permission.");
		}
		return true;
	}
}
