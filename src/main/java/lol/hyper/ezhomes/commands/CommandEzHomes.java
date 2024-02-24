package lol.hyper.ezhomes.commands;

import lol.hyper.ezhomes.EzHomes;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CommandEzHomes implements TabExecutor {

    private final EzHomes ezHomes;
    private final BukkitAudiences audiences;

    public CommandEzHomes (EzHomes ezHomes) {
        this.ezHomes = ezHomes;
        this.audiences = ezHomes.getAdventure();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("zahomes.command")) {
            audiences.sender(sender).sendMessage(ezHomes.getMessage("no-perms"));
            return true;
        }

        if (args.length == 0) {
            audiences.sender(sender).sendMessage(Component.text("zahomes version " + ezHomes.getDescription().getVersion() + ". Created by hyperdefined, modified by Zeda").color(NamedTextColor.GREEN));
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("zahomes.reload")) {
                    ezHomes.loadConfig();
                    audiences.sender(sender).sendMessage(ezHomes.getMessage("config-reloaded"));
                } else {
                    audiences.sender(sender).sendMessage(ezHomes.getMessage("no-perms"));
                }
            } else {
                audiences.sender(sender).sendMessage(Component.text("zahomes version " + ezHomes.getDescription().getVersion() + ". Created by hyperdefined, modified by Zeda").color(NamedTextColor.GREEN));
            }
            return true;
        }
        audiences.sender(sender).sendMessage(Component.text("zahomes version " + ezHomes.getDescription().getVersion() + ". Created by hyperdefined, modified by Zeda").color(NamedTextColor.GREEN));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("reload");
        }
        return null;
    }
}
