package net.danh.storage.Commands;

import net.danh.storage.Manager.Files;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Sell.SellItems;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("Storage")) {
            if (args.length == 0) {
                if (sender.hasPermission("Storage.admin")) {
                    for (String user : Files.getlanguagefile().getStringList("Help_Admin")) {
                        sender.sendMessage(Files.colorize(user));
                    }
                }
                for (String user : Files.getlanguagefile().getStringList("Help_User")) {
                    sender.sendMessage(Files.colorize(user));
                }
                return true;
            }
            if (args.length == 3) {
                if (sender instanceof Player) {
                    if (args[0].equalsIgnoreCase("sell")) {
                        if (Integer.parseInt(args[2]) > 0) {
                            SellItems(((Player) sender).getPlayer(), args[1], Integer.parseInt(args[2]));
                        }
                        return true;
                    }
                }
            }
            if (args.length == 5) {
                if (args[0].equalsIgnoreCase("storage")) {
                    if (args[1].equalsIgnoreCase("set")) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            setStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                        }
                    }
                    if (args[1].equalsIgnoreCase("add")) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            addStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                        }
                    }
                    if (args[1].equalsIgnoreCase("remove")) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            removeStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                        }
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("maxstorage")) {
                    if (args[1].equalsIgnoreCase("set")) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            setMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                        }
                    }
                    if (args[1].equalsIgnoreCase("add")) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            addMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                        }
                    }
                    if (args[1].equalsIgnoreCase("remove")) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            removeMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
