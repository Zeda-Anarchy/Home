/*
 * This file is part of EzHomes.
 *
 * EzHomes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EzHomes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EzHomes.  If not, see <https://www.gnu.org/licenses/>.
 */

package lol.hyper.ezhomes.commands;

import lol.hyper.ezhomes.EzHomes;
import lol.hyper.ezhomes.tools.HomeManagement;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandDeleteHome implements TabExecutor {

    private final EzHomes ezHomes;
    private final HomeManagement homeManagement;
    private final BukkitAudiences audiences;

    public CommandDeleteHome(EzHomes ezHomes) {
        this.ezHomes = ezHomes;
        this.homeManagement = ezHomes.homeManagement;
        this.audiences = ezHomes.getAdventure();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            audiences.sender(sender).sendMessage(ezHomes.getMessage("errors.must-be-player"));
            return true;
        }

        if (!sender.hasPermission("zahomes.delhome")) {
            audiences.sender(sender).sendMessage(ezHomes.getMessage("no-perms"));
            return true;
        }

        Player player = (Player) sender;

        if (ezHomes.config.getBoolean("blocks-out-of-spawn-to-use.enabled")) {
            int range = ezHomes.config.getInt("blocks-out-of-spawn-to-use.range");
            int playerX = player.getLocation().getBlockX();
            int playerZ = player.getLocation().getBlockZ();
            if (Math.abs(playerX) > range || Math.abs(playerZ) > range) {
                audiences.player(player).sendMessage(ezHomes.getMessage("commands.delhome.out-of-range", range));
                return true;
            }
        }

        List<String> playerHomes = homeManagement.getPlayerHomes(player.getUniqueId());
        if (playerHomes.isEmpty()) {
            audiences.player(player).sendMessage(ezHomes.getMessage("errors.no-homes"));
            return true;
        }

        int argsLength = args.length;
        switch (argsLength) {
            case 0: {
                audiences.player(player).sendMessage(ezHomes.getMessage("errors.specify-home-name"));
                return true;
            }
            case 1: {
                String homeName = args[0];
                if (playerHomes.contains(homeName)) {
                    homeManagement.deleteHome(player.getUniqueId(), homeName);
                    audiences.player(player).sendMessage(ezHomes.getMessage("commands.delhome.home-deleted"));
                } else {
                    audiences.player(player).sendMessage(ezHomes.getMessage("errors.home-does-not-exist"));
                }
                return true;
            }
            default: {
                audiences.player(player).sendMessage(ezHomes.getMessage("commands.delhome.invalid-syntax"));
                break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        Player player = (Player) sender;
        return homeManagement.getPlayerHomes(player.getUniqueId());
    }
}