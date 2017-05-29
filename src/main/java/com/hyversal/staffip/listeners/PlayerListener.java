package com.hyversal.staffip.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.scheduler.BukkitRunnable;

import com.hyversal.staffip.IPManager;
import com.hyversal.staffip.StaffIPPlugin;

import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener
{
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e)
	{
		Player player = e.getPlayer();
		IPManager ipManager = StaffIPPlugin.get().getIpManager();

		if (player.hasPermission("hyversal.staff"))
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					String loggedIp = ipManager.getLoggedIP(player.getUniqueId());
					String currentIp = e.getAddress().getHostAddress();

					System.out.println(currentIp);

					if (loggedIp != null)
					{
						if (!loggedIp.equals(currentIp))
						{
							e.disallow(Result.KICK_OTHER, ChatColor.RED + "Your IP is different to your previous IP. \n\n" + ChatColor.RED + "Please contact an admin to have your IP reset.");
						}
					}
					else
					{
						ipManager.setLoggedIP(player.getUniqueId(), currentIp);
					}
				}
			}.runTaskAsynchronously(StaffIPPlugin.get());
		}
	}
}
