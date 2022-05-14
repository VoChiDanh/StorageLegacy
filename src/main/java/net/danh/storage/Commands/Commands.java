package net.danh.storage.Commands;

import net.danh.storage.Manager.Files;
import net.danh.storage.Manager.SpigotUpdater;
import net.danh.storage.Storage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

import static net.danh.storage.Gui.LoadMenu.ReloadMenu;
import static net.danh.storage.Gui.OpenGui.OpenGuiMenu;
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
                        }
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
                            }
                        }
                    }
                }
            }
        }
        if (label.equalsIgnoreCase("Storage") || label.equalsIgnoreCase("kho") || label.equalsIgnoreCase("store")) {
            if (args.length == 0) {
                Player p = ((Player) sender).getPlayer();
                OpenGuiMenu(p);
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    for (String user : getlanguagefile().getStringList("User.Help_User")) {
                        sender.sendMessage(colorize(user));
                    }
                    if (sender.hasPermission("Storage.admin")) {
                        for (String user : getlanguagefile().getStringList("Admin.Help_Admin")) {
                            sender.sendMessage(colorize(user));
                        }
                    }
                    return true;
                }
                if (sender.hasPermission("Storage.admin")) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        ReloadMenu();
                        reloadfiles();
                        sender.sendMessage(Files.colorize(getlanguagefile().getString("Admin.Reload")));
                        try {
                            SpigotUpdater updater = new SpigotUpdater(Storage.get(), 100516);
                            if (!updater.getLatestVersion().equals(Storage.get().getDescription().getVersion())) {
                                if (updater.checkForUpdates())
                                    sender.sendMessage(Files.colorize("&6An update was found!"));
                                sender.sendMessage(Files.colorize("&aNew version: " + updater.getLatestVersion()));
                                sender.sendMessage(Files.colorize("&aYour version: " + Storage.get().getDescription().getVersion()));
                                sender.sendMessage(Files.colorize("&cDownload: " + updater.getResourceURL()));
                            }
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
                        if (isInt(args[2])) {
                            try {
                                if (Integer.parseInt(args[2]) > 0) {
                                    SellItems(((Player) sender).getPlayer(), args[1], Integer.parseInt(args[2]));
                                } else {
                                    sender.sendMessage(colorize(getlanguagefile().getString("Invaild_Number")));
                                }
                            } catch (Exception e) {
                                sender.sendMessage(colorize(getlanguagefile().getString("Number_To_Big")));
                            }
                        } else {
                            if (getStorage(Objects.requireNonNull(((Player) sender).getPlayer()), args[1]) > 0) {
                                if (args[2].equalsIgnoreCase("all")) {
                                    SellItems(((Player) sender).getPlayer(), args[1], getStorage(Objects.requireNonNull(((Player) sender).getPlayer()), args[1]));
                                } else {
                                    sender.sendMessage(colorize(getlanguagefile().getString("Invaild_Character")));
                                }
                            } else {
                                sender.sendMessage(colorize(getlanguagefile().getString("User.Not_Have_Any_Item")));
                            }
                        }
                    }
                    if (args[0].equalsIgnoreCase("take")) {
                        if (isInt(args[2])) {
                            try {
                                if (Integer.parseInt(args[2]) > 0) {
                                    if (Integer.parseInt(args[2]) <= getAmountEmpty(Objects.requireNonNull(((Player) sender).getPlayer()), args[1])) {
                                        RemoveItems(((Player) sender).getPlayer(), args[1], Integer.parseInt(args[2]));
                                    } else {
                                        sender.sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("User.Not_Enough_Inventory"))
                                                .replaceAll("%space%", String.valueOf(getAmountEmpty(Objects.requireNonNull(((Player) sender).getPlayer()), args[1])))));
                                    }
                                } else {
                                    sender.sendMessage(colorize(getlanguagefile().getString("Invaild_Number")));
                                }
                            } catch (Exception e) {
                                sender.sendMessage(colorize(getlanguagefile().getString("Number_To_Big")));
                            }

                        } else {
                            if (getStorage(Objects.requireNonNull(((Player) sender).getPlayer()), args[1]) > 0) {
                                if (args[2].equalsIgnoreCase("all")) {
                                    RemoveItems(((Player) sender).getPlayer(), args[1], Math.min(getStorage(Objects.requireNonNull(((Player) sender).getPlayer()), args[1]), getAmountEmpty(Objects.requireNonNull(((Player) sender).getPlayer()), args[1])));
                                } else {
                                    sender.sendMessage(colorize(getlanguagefile().getString("Invaild_Character")));
                                }
                            } else {
                                sender.sendMessage(colorize(getlanguagefile().getString("User.Not_Have_Any_Item")));
                            }
                        }
                    }
                    if (args[0].equalsIgnoreCase("add")) {
                        if (isInt(args[2])) {
                            try {
                                if (Integer.parseInt(args[2]) > 0) {
                                    AddItems((((Player) sender).getPlayer()), args[1], Integer.parseInt(args[2]));
                                } else {
                                    sender.sendMessage(colorize(getlanguagefile().getString("Invaild_Number")));
                                }
                            } catch (Exception e) {
                                sender.sendMessage(colorize(getlanguagefile().getString("Number_To_Big")));
                            }
                        } else {
                            if (getAmountItem((Objects.requireNonNull(((Player) sender).getPlayer())), args[1]) > 0) {
                                if (args[2].equalsIgnoreCase("all")) {
                                    if (getMaxStorage(Objects.requireNonNull(((Player) sender).getPlayer()), args[1]) >= getStorage(Objects.requireNonNull(((Player) sender).getPlayer()), args[1]) + getAmountItem((Objects.requireNonNull(((Player) sender).getPlayer())), args[1])) {
                                        AddItems((((Player) sender).getPlayer()), args[1], getAmountItem((Objects.requireNonNull(((Player) sender).getPlayer())), args[1]));
                                    } else {
                                        if (getMaxStorage(Objects.requireNonNull(((Player) sender).getPlayer()), args[1]) - getStorage(Objects.requireNonNull(((Player) sender).getPlayer()), args[1]) > 0) {
                                            AddItems((((Player) sender).getPlayer()), args[1], getMaxStorage(Objects.requireNonNull(((Player) sender).getPlayer()), args[1]) - getStorage(Objects.requireNonNull(((Player) sender).getPlayer()), args[1]));
                                        } else {
                                            sender.sendMessage(colorize(getlanguagefile().getString("User.Add_Full_Storage")));
                                        }
                                    }
                                } else {
                                    sender.sendMessage(colorize(getlanguagefile().getString("Invaild_Character")));
                                }
                            } else {
                                sender.sendMessage(colorize(getlanguagefile().getString("User.Not_Have_Any_Item")));
                            }
                        }
                    }
                }
            }
            if (args.length == 5) {
                if (args[0].equalsIgnoreCase("storage")) {
                    Set<String> items = getconfigfile().getConfigurationSection("Blocks.").getKeys(false);
                    if (items.contains(args[3].toUpperCase())) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            if (Integer.parseInt(args[4]) > 0) {
                                if (sender.hasPermission("Storage.admin")) {
                                    Player s = ((Player) sender).getPlayer();
                                    if (args[1].equalsIgnoreCase("set")) {
                                        if (getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) == 0) {
                                            setMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Files.getconfigfile().getInt("Default_Max_Storage"));
                                        }
                                        if (Integer.parseInt(args[4]) <= getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3])) {
                                            setStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                                            Objects.requireNonNull(s).sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("Admin.Storage_Set"))
                                                    .replaceAll("%player%", args[2])
                                                    .replaceAll("%item%", getName(args[3]))
                                                    .replaceAll("%amount%", args[4])));
                                        } else {
                                            Objects.requireNonNull(s).sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("Admin.Storage_Set_Errol"))
                                                    .replaceAll("%player%", args[2])
                                                    .replaceAll("%max%", String.valueOf(getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3])))));
                                        }
                                    }
                                    if (args[1].equalsIgnoreCase("add")) {
                                        if (getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) == 0) {
                                            setMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Files.getconfigfile().getInt("Default_Max_Storage"));
                                        }
                                        if (getStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) + Integer.parseInt(args[4]) <= getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3])) {
                                            addStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                                            Objects.requireNonNull(s).sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("Admin.Storage_Add"))
                                                    .replaceAll("%player%", args[2])
                                                    .replaceAll("%item%", getName(args[3]))
                                                    .replaceAll("%amount%", args[4])));
                                        } else {
                                            Objects.requireNonNull(s).sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("Admin.Storage_Add_Errol"))
                                                    .replaceAll("%amount%", String.valueOf(getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) - getStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3])))
                                                    .replaceAll("%player%", args[2])));
                                        }
                                    }
                                    if (args[1].equalsIgnoreCase("remove")) {
                                        if (getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) == 0) {
                                            setMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Files.getconfigfile().getInt("Default_Max_Storage"));
                                        }
                                        if (getStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) - Integer.parseInt(args[4]) >= 0) {
                                            removeStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                                            Objects.requireNonNull(s).sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("Admin.Storage_Remove"))
                                                    .replaceAll("%player%", args[2])
                                                    .replaceAll("%item%", getName(args[3]))
                                                    .replaceAll("%amount%", args[4])));
                                        } else {
                                            Objects.requireNonNull(s).sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("Admin.Storage_Remove_Errol"))
                                                    .replaceAll("%amount%", String.valueOf(getStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]))))
                                                    .replaceAll("%player%", args[2]));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("max_storage")) {
                    Set<String> items = getconfigfile().getConfigurationSection("Blocks.").getKeys(false);
                    if (items.contains(args[3].toUpperCase())) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            if (Integer.parseInt(args[4]) > 0) {
                                if (sender.hasPermission("Storage.admin")) {
                                    Player s = ((Player) sender).getPlayer();
                                    if (args[1].equalsIgnoreCase("set")) {
                                        if (Integer.parseInt(args[4]) >= getStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) && Integer.parseInt(args[4]) >= getconfigfile().getInt("Default_Max_Storage")) {
                                            setMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                                            Objects.requireNonNull(s).sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("Admin.MaxStorage_Set"))
                                                    .replaceAll("%player%", args[2])
                                                    .replaceAll("%item%", getName(args[3]))
                                                    .replaceAll("%number%", args[4])));
                                        } else if (Integer.parseInt(args[4]) >= getconfigfile().getInt("Default_Max_Storage")) {
                                            Objects.requireNonNull(s).sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("Admin.MaxStorage_Set_Errol_1"))
                                                    .replaceAll("%player%", args[2])
                                                    .replaceAll("%storage%", String.valueOf(getStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3])))));
                                        }
                                        if (Integer.parseInt(args[4]) < getconfigfile().getInt("Default_Max_Storage")) {
                                            Objects.requireNonNull(s).sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("Admin.MaxStorage_Set_Errol_2"))
                                                    .replaceAll("%max_default%", String.valueOf(getconfigfile().getInt("Default_Max_Storage")))));
                                        }
                                    }
                                    if (args[1].equalsIgnoreCase("add")) {
                                        addMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                                        Objects.requireNonNull(s).sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("Admin.MaxStorage_Add"))
                                                .replaceAll("%player%", args[2])
                                                .replaceAll("%item%", getName(args[3]))
                                                .replaceAll("%number%", args[4])));
                                    }
                                    if (args[1].equalsIgnoreCase("remove")) {
                                        int remove = getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) - Integer.parseInt(args[4]);
                                        int number = getMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]) - getStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3]);
                                        if (remove >= getStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3])) {
                                            removeMaxStorage(Objects.requireNonNull(Bukkit.getPlayer(args[2])), args[3], Integer.parseInt(args[4]));
                                            Objects.requireNonNull(s).sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("Admin.MaxStorage_Remove"))
                                                    .replaceAll("%player%", args[2])
                                                    .replaceAll("%item%", getName(args[3]))
                                                    .replaceAll("%number%", args[4])));
                                        } else if (number > 0) {
                                            Objects.requireNonNull(s).sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("Admin.MaxStorage_Remove_Errol_1"))
                                                    .replaceAll("%player%", args[2])
                                                    .replaceAll("%number%", String.valueOf(number))));
                                        } else {
                                            Objects.requireNonNull(s).sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("Admin.MaxStorage_Remove_Errol_2"))
                                                    .replaceAll("%player%", args[2])));
                                        }
                                    }
                                }
                            } else {
                                Player s = ((Player) sender).getPlayer();
                                Objects.requireNonNull(s).sendMessage(colorize(getlanguagefile().getString("Invaild_Number")));
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
