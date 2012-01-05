package uk.co.speshkittyonline.gameMode;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.player.SpoutPlayer;

public class EasyGameMode extends JavaPlugin
{
	protected final Permission permSelf = new Permission("gamemode.self"), permOther = new Permission("gamemode.other"), permBukkit = new Permission("bukkit.command.gamemode");
	protected static ArrayList<String> help = new ArrayList<String>();
	
	@Override
	public void onDisable() 
	{
		log("No longer hijacking /gamemode");
		help.clear();
	}

	@Override
	public void onEnable() 
	{
		log("Hijacking /gamemode");
		help.add("/gamemode [player name] <0|1>");
		help.add("Changes a players gamemode between survival (0) and creative (1)");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof SpoutPlayer) //Is the sender a player
		{
			SpoutPlayer user = (SpoutPlayer) sender;
			if(args.length < 2) // /gamemode, /gamemode 1
			{
				if(user.hasPermission(permSelf) || user.hasPermission(permBukkit) || user.isOp())
				{
					if(args.length == 0)
					{
						if(user.getGameMode() == GameMode.CREATIVE)
						{
							user.setGameMode(GameMode.SURVIVAL);
							user.sendMessage(ChatColor.RED + "Your gamemode has been set to " + ChatColor.DARK_RED + "survival.");
						}
						else if(user.getGameMode() == GameMode.SURVIVAL)
						{
							user.setGameMode(GameMode.CREATIVE);
							user.sendMessage(ChatColor.RED + "Your gamemode has been set to " + ChatColor.DARK_RED + "creative.");
						}
					}
					else
					{
						GameMode newGameMode = GameMode.getByValue(Integer.parseInt(args[0]));
						if(newGameMode == null) { user.sendMessage(ChatColor.RED + "That gamemode does not exist!"); }
						else
						{
							user.setGameMode(newGameMode);
							user.sendMessage(ChatColor.RED + "Your gamemode was set to " + ChatColor.DARK_RED + newGameMode.toString() + ChatColor.RED + ".");
						}
					}
				}
				else { user.sendMessage(ChatColor.RED + "You don't have permission to change your own gamemode."); }
			}
			else if(args.length == 2)
			{
				if(getServer().getPlayer(args[0]) == null) //Check the player exists
				{
					user.sendMessage("That player could not be found.");
				}
				Player target = getServer().getPlayer(args[0]);
				if(user != target)
				{
					if(user.hasPermission(permBukkit) || user.hasPermission(permOther) || user.isOp())
					{
						if(GameMode.getByValue(Integer.parseInt(args[1])) == GameMode.SURVIVAL) //Set mode to survival
						{
							if(target.getGameMode() == GameMode.SURVIVAL)
							{
								user.sendMessage("That player is already in survival mode.");
							}
							else
							{
								target.setGameMode(GameMode.SURVIVAL);
								user.sendMessage(target.getName() + "'s gamemode was set to survival.");
								target.sendMessage("Your gamemode has been set to survival.");
							}
						}
						else if(GameMode.getByValue(Integer.parseInt(args[1])) == GameMode.CREATIVE) // Set mode to creative
						{
							if(target.getGameMode() == GameMode.CREATIVE)
							{
								user.sendMessage("That player is already in creative.");
							}
							else
							{
								target.setGameMode(GameMode.CREATIVE);
								user.sendMessage(target.getName() + "'s gamemode was set to creative.");
								target.sendMessage("Your gamemode has been set to creative.");
							}
						}
						else
						{
							user.sendMessage("That game mode was not recognised.");
							DoHelp(sender);
						}
					}
					else
					{
						user.sendMessage("You don't have permission to edit other people's permissions.");
					}
				}
				else
				{
					user.chat("/gamemode" + args[1]);
				}
			}
			else
			{
				DoHelp(sender);
			}
		}
		else //Assume console.
		{
			if(args.length < 2) //Not enough
			{
				sender.sendMessage("Not enough arguments.");
				DoHelp(sender);
			}
			else if(args.length > 2) //Too many
			{
				sender.sendMessage("Too many arguments.");
				DoHelp(sender);
			}
			else //Just right
			{
				SpoutPlayer target = (SpoutPlayer) getServer().getPlayer(args[0]);
				GameMode newGameMode = GameMode.getByValue(Integer.parseInt(args[1]));
				if(newGameMode == null) { sender.sendMessage("That gamemode does not exist"); }
				else
				{
					target.setGameMode(newGameMode);
					target.sendMessage("Your gamemode has been changed.");
				}
			}
		}
		return true;
	}
	
	protected void log(String message) { log(Level.INFO, message); }
	protected void log(Level level, String message) { getServer().getLogger().log(level, message); }
	
	private void DoHelp(CommandSender sender)
	{
		for (int i=0;i<help.size();i++)
		{
			sender.sendMessage(help.get(i));
		}
	}

}
