package lol.hyper.ezhomes.commands;

import lol.hyper.ezhomes.HomeManagement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class CommandUpdateHome implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("You must be a player for this command!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "You must specify a home name!");
        } else {
            if (args.length == 1) {
                try {
                    if (HomeManagement.getPlayerHomes(player) != null) {
                        if (HomeManagement.getPlayerHomes(player).contains(args[0])) {
                            HomeManagement.updateHome(player, args[0]);
                            player.sendMessage(ChatColor.GREEN + "Updated home.");
                        } else {
                            player.sendMessage(ChatColor.RED + "That home does not exist.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You don't have any homes!");
                    }
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                    sender.sendMessage(ChatColor.RED + "Unable to create home! Please check your console for more information.");
                    Bukkit.getLogger().severe("Error reading home data for player: " + player.getName());
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid syntax. To update a home, simply do \"/updatehome <home name>\"");
            }
        }
        return true;
    }
}
