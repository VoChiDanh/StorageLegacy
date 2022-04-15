package net.danh.storage.Commands;

import net.danh.storage.Manager.Data;
import net.danh.storage.Manager.Files;
import net.danh.storage.Manager.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.*;
import static net.danh.storage.Manager.Items.RemoveItems;
import static net.danh.storage.Manager.Items.SellItems;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("APick") || label.equalsIgnoreCase("autopickup")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    setautoPick(Objects.requireNonNull(((Player) sender).getPlayer()), !autoPick(((Player) sender).getPlayer()));
                }
            }
        }
        if (label.equalsIgnoreCase("ASmelt") || label.equalsIgnoreCase("autosmelt")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (sender.hasPermission("storage.asmelt")) {
                        setautoSmelt(Objects.requireNonNull(((Player) sender).getPlayer()), !autoSmelt(((Player) sender).getPlayer()));
                    }
                }
            }
        }
        if (label.equalsIgnoreCase("Storage") || label.equalsIgnoreCase("kho") || label.equalsIgnoreCase("store")) {
            if (args.length == 0) {
                for (String user : getlanguagefile().getStringList("Help_User")) {
                    sender.sendMessage(colorize(user));
                }
                if (sender.hasPermission("Storage.admin")) {
                    for (String user : getlanguagefile().getStringList("Help_Admin")) {
                        sender.sendMessage(colorize(user));
                    }
                }
                return true;
            }
            if (args.length == 1) {
                if (sender.hasPermission("Storage.admin")) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        reloadfiles();
                        sender.sendMessage(Files.colorize("&aReloaded"));
                    }
                }
            }
            if (args.length == 3) {
                if (sender instanceof Player) {
                    if (args[0].equalsIgnoreCase("sell")) {
                        if (Integer.parseInt(args[2]) > 0) {
                            SellItems(((Player) sender).getPlayer(), args[1], Integer.parseInt(args[2]));
                        } else if (Integer.parseInt(args[2]) == 0) {
                            SellItems(((Player) sender).getPlayer(), args[1], getStorage(Objects.requireNonNull(((Player) sender).getPlayer()), args[1]));
                        }

                        return true;
                    }
                    if (args[0].equalsIgnoreCase("take")) {
                        if (Integer.parseInt(args[2]) > 0) {
                            RemoveItems(((Player) sender).getPlayer(), args[1], Integer.parseInt(args[2]));
                        } else if (Integer.parseInt(args[2]) == 0) {
                            RemoveItems(((Player) sender).getPlayer(), args[1], getStorage(Objects.requireNonNull(((Player) sender).getPlayer()), args[1]));
                        }
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("add")) {
                        if (Material.getMaterial(args[1]) != null) {
                            ItemStack checkitems = new ItemStack(Objects.requireNonNull(Material.getMaterial(args[1])));
                            if (Objects.requireNonNull(((Player) sender).getPlayer()).getInventory().containsAtLeast(checkitems, Integer.parseInt(args[2]))) {
                                ItemStack items = new ItemStack(Objects.requireNonNull(Material.getMaterial(args[1])), Integer.parseInt(args[2]));
                                ((Player) sender).getPlayer().getInventory().removeItem(items);
                                Data.addStorage(((Player) sender).getPlayer(), args[1], Integer.parseInt(args[2]));
                                sender.sendMessage(Files.colorize(Objects.requireNonNull(getlanguagefile().getString("Add_Block"))
                                        .replaceAll("%block%", Items.getName(args[1]))
                                        .replaceAll("%amount%", args[2])));
                            } else {
                                sender.sendMessage(Files.colorize(Files.getlanguagefile().getString("Not_Enough")));
                            }
                        } else {
                            sender.sendMessage(Files.colorize(Files.getlanguagefile().getString("Not_Correct_Item")));
                        }
                        return true;
                    }
                }
            }
            if (args.length == 5) {
                if (args[0].equalsIgnoreCase("storage")) {
                    if (Material.getMaterial(args[3]) != null) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            if (sender.hasPermission("Storage.admin")) {
                                if (args[1].equalsIgnoreCase("set")) {
                                    if (getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) == 0) {
                                        setMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Files.getconfigfile().getInt("Default_Max_Storage"));
                                    }
                                    if (Integer.parseInt(args[4]) <= getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3])) {
                                        setStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                                        
                                    }
                                }
                                if (args[1].equalsIgnoreCase("add")) {
                                    if (getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) == 0) {
                                        setMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Files.getconfigfile().getInt("Default_Max_Storage"));
                                    }
                                    if (getStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) + Integer.parseInt(args[4]) <= getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3])) {
                                        addStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                                        
                                    }
                                }
                                if (args[1].equalsIgnoreCase("remove")) {
                                    if (getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) == 0) {
                                        setMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Files.getconfigfile().getInt("Default_Max_Storage"));
                                    }
                                    if (getStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) - Integer.parseInt(args[4]) <= getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3])) {
                                        removeStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                                        
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("max_storage")) {
                    if (Material.getMaterial(args[3]) != null) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            if (sender.hasPermission("Storage.admin")) {
                                if (args[1].equalsIgnoreCase("set")) {
                                    setMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                                    
                                }
                                if (args[1].equalsIgnoreCase("add")) {
                                    addMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                                    
                                }
                                if (args[1].equalsIgnoreCase("remove")) {
                                    removeMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                                    
                                }
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
