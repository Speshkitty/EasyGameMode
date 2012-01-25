package uk.co.speshkittyonline.easyGameMode;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class EasyGameMode extends JavaPlugin
{
	protected final Permission permSelf = new Permission("gamemode.self"), permOther = new Permission("gamemode.other");
	protected static ArrayList<String> help = new ArrayList<String>();
	
	@Override
	public void onDisable() 
	{
		Log("No longer hijacking /gamemode");
		help.clear();
	}

	@Override
	public void onEnable() 
	{
		Log("Hijacking /gamemode");
		help.add("/gamemode [player name] <0|1>");
		help.add("Changes a players gamemode between survival (0) and creative (1)");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player) //Is the sender a player
		{
			Player user = (Player) sender;
			if(args.length == 0) // /gamemode
			{
				if(user.hasPermission(permSelf) || user.isOp())
				{
					GameMode newGameMode = GameMode.getByValue(1 - user.getGameMode().getValue());
					user.setGameMode(newGameMode);
					user.sendMessage(ChatColor.GREEN + "Your gamemode was set to " + ChatColor.DARK_GREEN + newGameMode.toString() + ChatColor.GREEN + ".");
				}
				else
				{
					user.sendMessage(ChatColor.RED + "You don't have permission to change your gamemode.");
				}
			}
			else if(args.length == 1) // /gamemode <name>, /gamemode <number>
			{
				if(args[0].equals("0") || args[0].equals("1")) // /gamemode <number>
				{
					if(user.hasPermission(permSelf) || user.isOp())
					{
						user.setGameMode(GameMode.getByValue(Integer.parseInt(args[0])));
						user.sendMessage(ChatColor.GREEN + "Your gamemode was set to " + ChatColor.DARK_GREEN + user.getGameMode().toString() + ChatColor.GREEN + ".");
					}
					else { user.sendMessage(ChatColor.RED + "You don't have permission to change your gamemode."); }
				}
				else // /gamemode <name>
				{
					if(user.hasPermission(permOther) || user.isOp())
					{
						Player target = getServer().getPlayer(args[0]);
						if(target == null) { user.sendMessage(ChatColor.RED + "That player does not exist!"); }
						else
						{
							GameMode newGameMode = GameMode.getByValue(1 - target.getGameMode().getValue());
							target.setGameMode(newGameMode);
							target.sendMessage(ChatColor.GREEN + "Your gamemode was set to " + ChatColor.DARK_GREEN + newGameMode.toString() + ChatColor.GREEN + " by " + user.getPlayerListName() + ".");
							user.sendMessage(ChatColor.GREEN + "You set " + target.getPlayerListName() + "'s gamemode to " + ChatColor.DARK_GREEN + newGameMode.toString() + ChatColor.GREEN + ".");
						}
					}
					else { user.sendMessage(ChatColor.RED + "You don't have permission to change other people's gamemode."); }
				}
			}
			else if(args.length == 2)
			{
				if(getServer().getPlayer(args[0]) == user) { user.chat("/gamemode " + args[1]); /* HUR DUR DUR */ }
				else
				{
					if(user.hasPermission(permOther) || user.isOp())
					{
						Player target = getServer().getPlayer(args[0]);
						GameMode newGameMode = GameMode.getByValue(Integer.parseInt(args[1]));
						if(newGameMode == null)
						{
							user.sendMessage(ChatColor.RED + "That gamemode was not recognised.");
							DoHelp(sender);
						}
						else
						{
							target.setGameMode(newGameMode);
							target.sendMessage(ChatColor.GREEN + "Your gamemode was changed to " + ChatColor.DARK_GREEN + newGameMode.toString() + ChatColor.GREEN + " by " + user.getPlayerListName());
							user.sendMessage(ChatColor.GREEN + "You set " + target.getPlayerListName() + "'s gamemode to " + ChatColor.DARK_GREEN + newGameMode.toString() + ChatColor.GREEN + ".");
						}
					}
					else { user.sendMessage(ChatColor.RED + "You don't have permission to change other people's gamemode."); }
				}
			}
			else { DoHelp(sender); }
		}
		else //Assume console.
		{
			if(args.length == 0)
			{
				sender.sendMessage("Not enough arguments.");
				DoHelp(sender);
			}
			else if(args.length == 1)
			{
				Player target = getServer().getPlayer(args[0]);
				if(target == null) { sender.sendMessage("That player could not be found."); }
				else
				{
					GameMode newGameMode = GameMode.getByValue(1 - target.getGameMode().getValue());
					target.setGameMode(newGameMode);
					target.sendMessage(ChatColor.GREEN + "Your gamemode was set to " + ChatColor.DARK_GREEN + newGameMode.toString() + " by the console.");
					sender.sendMessage("You set " + target.getPlayerListName() + "'s gamemode to " + newGameMode.toString() + ".");
				}
			}
			else if(args.length == 2)
			{
				Player target = getServer().getPlayer(args[0]);
				GameMode newGameMode = GameMode.getByValue(Integer.parseInt(args[1]));
				if(newGameMode == null) { sender.sendMessage("That gamemode was not recognised."); }
				else
				{
					target.setGameMode(newGameMode);
					target.sendMessage(ChatColor.GREEN + "Your gamemode was set to " + ChatColor.DARK_GREEN + newGameMode.toString() + " by the console.");
					sender.sendMessage("You set " + target.getPlayerListName() + "'s gamemode to " + newGameMode.toString() + ".");
				}
			}
			else
			{
				sender.sendMessage("Too many arguments.");
				DoHelp(sender);
			}
		}
		return true;
	}
	
	protected void Log(String message) { Log(Level.INFO, message); }
	protected void Log(Level level, String message) { getServer().getLogger().log(level, message); }
	
	private void DoHelp(CommandSender sender)
	{
		for (int i=0;i<help.size();i++)
		{
			sender.sendMessage(help.get(i));
		}
	}

}
