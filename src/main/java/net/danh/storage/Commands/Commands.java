package net.danh.storage.Commands;

import net.danh.storage.Manager.Data;
import net.danh.storage.Manager.Files;
import net.danh.storage.Manager.Items;
import net.danh.storage.Manager.SpigotUpdater;
import net.danh.storage.Storage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TranslatableComponent;
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
import static net.danh.storage.Manager.Items.*;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("APick") || label.equalsIgnoreCase("autopickup")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    setautoPick(Objects.requireNonNull(((Player) sender).getPlayer()), !autoPick(((Player) sender).getPlayer()));
                    Player p = ((Player) sender).getPlayer();
                    if (Files.getconfigfile().getBoolean("Message.TOGGLE.STATUS")) {
                        if (autoPick(((Player) sender).getPlayer())) {
                            p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.TOGGLE.TYPE")),
                                    new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("Toggle_Status"))
                                            .replaceAll("%type%", Objects.requireNonNull(getlanguagefile().getString("Type.autopickup")))
                                            .replaceAll("%status%", Objects.requireNonNull(getconfigfile().getString("Boolean.true"))))));
                        }
                        if (!autoPick(((Player) sender).getPlayer())) {
                            p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.TOGGLE.TYPE")),
                                    new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("Toggle_Status"))
                                            .replaceAll("%type%", Objects.requireNonNull(getlanguagefile().getString("Type.autopickup")))
                                            .replaceAll("%status%", Objects.requireNonNull(getconfigfile().getString("Boolean.false"))))));
                        } else {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        if (label.equalsIgnoreCase("ASmelt") || label.equalsIgnoreCase("autosmelt")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (sender.hasPermission("storage.asmelt")) {
                        setautoSmelt(Objects.requireNonNull(((Player) sender).getPlayer()), !autoSmelt(((Player) sender).getPlayer()));
                        Player p = ((Player) sender).getPlayer();
                        if (Files.getconfigfile().getBoolean("Message.TOGGLE.STATUS")) {
                            if (autoSmelt(((Player) sender).getPlayer())) {
                                p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.TOGGLE.TYPE")),
                                        new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("Toggle_Status"))
                                                .replaceAll("%type%", Objects.requireNonNull(getlanguagefile().getString("Type.autosmelt")))
                                                .replaceAll("%status%", Objects.requireNonNull(getconfigfile().getString("Boolean.true"))))));
                            }
                            if (!autoSmelt(((Player) sender).getPlayer())) {
                                p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.TOGGLE.TYPE")),
                                        new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("Toggle_Status"))
                                                .replaceAll("%type%", Objects.requireNonNull(getlanguagefile().getString("Type.autosmelt")))
                                                .replaceAll("%status%", Objects.requireNonNull(getconfigfile().getString("Boolean.false"))))));
                            } else {
                                return true;
                            }
                        } else {
                            return true;
                        }
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
                        try {
                            SpigotUpdater updater = new SpigotUpdater(Storage.get(), 100516);
                            if (updater.checkForUpdates())
                                sender.sendMessage(Files.colorize("&6An update was found!"));
                            sender.sendMessage(Files.colorize("&aNew version: " + updater.getLatestVersion()));
                            sender.sendMessage(Files.colorize("&aYour version: " + Storage.get().getDescription().getVersion()));
                            sender.sendMessage(Files.colorize("&cDownload: " + updater.getResourceURL()));
                        } catch (Exception e) {
                            Storage.get().getLogger().warning("Could not check for updates! Stacktrace:");
                            e.printStackTrace();
                        }
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
                            RemoveItems(((Player) sender).getPlayer(), args[1].toUpperCase(), Integer.parseInt(args[2]));
                        } else if (Integer.parseInt(args[2]) == 0) {
                            RemoveItems(((Player) sender).getPlayer(), args[1].toUpperCase(), getStorage(Objects.requireNonNull(((Player) sender).getPlayer()), args[1]));
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
                                Player p = ((Player) sender).getPlayer();
                                if (Objects.requireNonNull(getconfigfile().getString("Message.ADD.TYPE")).equalsIgnoreCase("ACTION_BAR")
                                        || Objects.requireNonNull(getconfigfile().getString("Message.ADD.TYPE")).equalsIgnoreCase("CHAT")) {
                                    if (Files.getconfigfile().getBoolean("Message.ADD.STATUS")) {
                                        p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.ADD.TYPE")),
                                                new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("Add_Item"))
                                                        .replaceAll("%item%", getName(args[1]).replaceAll("_", " "))
                                                        .replaceAll("%amount%", args[2])
                                                        .replaceAll("%storage%", String.format("%,d", getStorage(p, args[1])))
                                                        .replaceAll("%max%", String.format("%,d", getMaxStorage(p, args[1]))))));
                                    }
                                } else {
                                    if (Objects.requireNonNull(getconfigfile().getString("Message.ADD.TYPE")).equalsIgnoreCase("TITLE")) {
                                        p.sendTitle(colorize(Objects.requireNonNull(getconfigfile().getString("Message.ADD.TITLE.TITLE"))
                                                .replaceAll("%item%", Items.getName(getName(args[1])).replaceAll("_", " ")
                                                        .replaceAll("-", " "))
                                                .replaceAll("%amount%", args[2])
                                                .replaceAll("%storage%", String.format("%,d", getStorage(p, args[1]))))
                                                .replaceAll("%max%", String.format("%,d", getMaxStorage(p, args[1]))), colorize(Objects.requireNonNull(getconfigfile().getString("Message.ADD.TITLE.SUBTITLE"))
                                                .replaceAll("%item%", Items.getName(getName(args[1])).replaceAll("_", " ")
                                                        .replaceAll("-", " "))
                                                .replaceAll("%amount%", args[2])
                                                .replaceAll("%storage%", String.format("%,d", getStorage(p, args[1])))
                                                .replaceAll("%max%", String.format("%,d", getMaxStorage(p, args[1])))), getconfigfile().getInt("Message.ADD.TITLE.FADEIN"), getconfigfile().getInt("Message.ADD.TITLE.STAY"), getconfigfile().getInt("Message.ADD.TITLE.FADEOUT"));
                                    }
                                }
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
