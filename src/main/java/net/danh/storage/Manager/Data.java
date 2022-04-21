package net.danh.storage.Manager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import static net.danh.storage.Manager.Files.*;

public class Data {
    private static final HashMap<String, Integer> data = new HashMap<>();
    private static final HashMap<String, Boolean> status = new HashMap<>();

    public static int getStorageData(@NotNull Player p, String item) {
        return getdatafile().getInt("players." + p.getName() + ".items." + item + ".amount");
    }

    public static int getStorage(@NotNull Player p, String item) {
        return data.get(p.getName() + "_storage_" + item);
    }

    public static void setStorage(@NotNull Player p, String item, Integer amount) {
        data.put(p.getName() + "_storage_" + item, Math.max(amount, 0));
    }

    public static void addStorage(@NotNull Player p, String item, Integer amount) {
        if (getMaxStorage(p, item) >= (getStorage(p, item) + amount)) {
            data.replace(p.getName() + "_storage_" + item, getStorage(p, item) + amount);
            if (Objects.requireNonNull(getconfigfile().getString("Message.RECEIVE.TYPE")).equalsIgnoreCase("ACTION_BAR")
                    || Objects.requireNonNull(getconfigfile().getString("Message.RECEIVE.TYPE")).equalsIgnoreCase("CHAT")) {
                if (getconfigfile().getBoolean("Message.RECEIVE.STATUS")) {
                    p.spigot().sendMessage(ChatMessageType.valueOf(getconfigfile().getString("Message.RECEIVE.TYPE")),
                            new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("User.Receive_Item"))
                                    .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                            .replaceAll("-", " "))
                                    .replaceAll("%amount%", String.valueOf(amount))
                                    .replaceAll("%storage%", String.format("%,d", getStorage(p, item)))
                                    .replaceAll("%max%", String.format("%,d", getMaxStorage(p, item))))));
                }
            } else {
                if (Objects.requireNonNull(getconfigfile().getString("Message.RECEIVE.TYPE")).equalsIgnoreCase("TITLE")) {
                    p.sendTitle(colorize(Objects.requireNonNull(getconfigfile().getString("Message.RECEIVE.TITLE.TITLE"))
                            .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                    .replaceAll("-", " "))
                            .replaceAll("%amount%", String.valueOf(amount))
                            .replaceAll("%storage%", String.format("%,d", getStorage(p, item)))
                            .replaceAll("%max%", String.format("%,d", getMaxStorage(p, item)))), colorize(Objects.requireNonNull(getconfigfile().getString("Message.RECEIVE.TITLE.SUBTITLE"))
                            .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                    .replaceAll("-", " "))
                            .replaceAll("%amount%", String.valueOf(amount))
                            .replaceAll("%storage%", String.format("%,d", getStorage(p, item)))
                            .replaceAll("%max%", String.format("%,d", getMaxStorage(p, item)))), getconfigfile().getInt("Message.RECEIVE.TITLE.FADEIN"), getconfigfile().getInt("Message.RECEIVE.TITLE.STAY"), getconfigfile().getInt("Message.RECEIVE.TITLE.FADEOUT"));
                }
            }
        } else {
            data.put(p.getName() + "_storage_" + item, getMaxStorage(p, item));
            if (Objects.requireNonNull(getconfigfile().getString("Message.FULL.TYPE")).equalsIgnoreCase("ACTION_BAR")
                    || Objects.requireNonNull(getconfigfile().getString("Message.FULL.TYPE")).equalsIgnoreCase("CHAT")) {
                p.spigot().sendMessage(ChatMessageType.valueOf(getconfigfile().getString("Message.FULL.TYPE")),
                        new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("User.Full_Storage"))
                                .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                        .replaceAll("-", " "))
                                .replaceAll("%amount%", String.valueOf(amount))
                                .replaceAll("%storage%", String.format("%,d", getStorage(p, item)))
                                .replaceAll("%max%", String.format("%,d", getMaxStorage(p, item))))));
            } else {
                if (Objects.requireNonNull(getconfigfile().getString("Message.FULL.TYPE")).equalsIgnoreCase("TITLE")) {
                    p.sendTitle(colorize(Objects.requireNonNull(getconfigfile().getString("Message.FULL.TITLE.TITLE"))
                            .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                    .replaceAll("-", " "))
                            .replaceAll("%amount%", String.valueOf(amount))
                            .replaceAll("%storage%", String.format("%,d", getStorage(p, item)))
                            .replaceAll("%max%", String.format("%,d", getMaxStorage(p, item)))), colorize(Objects.requireNonNull(getconfigfile().getString("Message.FULL.TITLE.SUBTITLE"))
                            .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                    .replaceAll("-", " "))
                            .replaceAll("%amount%", String.valueOf(amount))
                            .replaceAll("%storage%", String.format("%,d", getStorage(p, item)))
                            .replaceAll("%max%", String.format("%,d", getMaxStorage(p, item)))), getconfigfile().getInt("Message.FULL.TITLE.FADEIN"), getconfigfile().getInt("Message.FULL.TITLE.STAY"), getconfigfile().getInt("Message.FULL.TITLE.FADEOUT"));
                }
            }
        }
    }

    public static void removeStorage(@NotNull Player p, String item, Integer amount) {
        if (getStorage(p, item) > amount) {
            data.replace(p.getName() + "_storage_" + item, getStorage(p, item) - amount);
        } else {
            data.replace(p.getName() + "_storage_" + item, 0);
        }
        if (Objects.requireNonNull(getconfigfile().getString("Message.REMOVE.TYPE")).equalsIgnoreCase("ACTION_BAR")
                || Objects.requireNonNull(getconfigfile().getString("Message.REMOVE.TYPE")).equalsIgnoreCase("CHAT")) {
            if (getconfigfile().getBoolean("Message.REMOVE.STATUS")) {
                p.spigot().sendMessage(ChatMessageType.valueOf(getconfigfile().getString("Message.REMOVE.TYPE")),
                        new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("User.Remove_Item"))
                                .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                        .replaceAll("-", " "))
                                .replaceAll("%amount%", String.valueOf(amount))
                                .replaceAll("%storage%", String.format("%,d", getStorage(p, item)))
                                .replaceAll("%max%", String.format("%,d", getMaxStorage(p, item))))));
            }
        } else {
            if (Objects.requireNonNull(getconfigfile().getString("Message.REMOVE.TYPE")).equalsIgnoreCase("TITLE")) {
                p.sendTitle(colorize(Objects.requireNonNull(getconfigfile().getString("Message.REMOVE.TITLE.TITLE"))
                        .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                .replaceAll("-", " "))
                        .replaceAll("%amount%", String.valueOf(amount))
                        .replaceAll("%storage%", String.format("%,d", getStorage(p, item)))
                        .replaceAll("%max%", String.format("%,d", getMaxStorage(p, item)))), colorize(Objects.requireNonNull(getconfigfile().getString("Message.REMOVE.TITLE.SUBTITLE"))
                        .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                .replaceAll("-", " "))
                        .replaceAll("%amount%", String.valueOf(amount))
                        .replaceAll("%storage%", String.format("%,d", getStorage(p, item)))
                        .replaceAll("%max%", String.format("%,d", getMaxStorage(p, item)))), getconfigfile().getInt("Message.REMOVE.TITLE.FADEIN"), getconfigfile().getInt("Message.REMOVE.TITLE.STAY"), getconfigfile().getInt("Message.REMOVE.TITLE.FADEOUT"));
            }
        }
    }

    public static int getMaxStorageData(@NotNull Player p, String item) {
        return getdatafile().getInt("players." + p.getName() + ".items." + item + ".max");
    }

    public static int getMaxStorage(@NotNull Player p, String item) {
        return data.get(p.getName() + "_max_" + item);
    }

    public static void setMaxStorage(@NotNull Player p, String item, Integer amount) {
        data.put(p.getName() + "_max_" + item, Math.max(amount, getconfigfile().getInt("Default_Max_Storage")));
    }

    public static void addMaxStorage(@NotNull Player p, String item, Integer amount) {
        data.replace(p.getName() + "_max_" + item, getMaxStorage(p, item) + amount);
    }

    public static void removeMaxStorage(@NotNull Player p, String item, Integer amount) {
        if (getMaxStorage(p, item) > amount) {
            data.replace(p.getName() + "_max_" + item, getMaxStorage(p, item) - amount);
        } else {
            data.replace(p.getName() + "_max_" + item, 0);
        }
    }

    public static int getRandomInt(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }

    public static boolean autoSmeltData(@NotNull Player p) {
        return getdatafile().getBoolean("players." + p.getName() + ".auto.Smelt");
    }

    public static boolean autoPickData(@NotNull Player p) {
        return getdatafile().getBoolean("players." + p.getName() + ".auto.Pick");
    }

    public static boolean autoSmelt(@NotNull Player p) {
        return status.get(p.getName() + "_auto_smelt_");
    }

    public static void setautoSmelt(@NotNull Player p, boolean Boolean) {
        status.put(p.getName() + "_auto_smelt_", Boolean);
    }

    public static boolean autoPick(@NotNull Player p) {
        return status.get(p.getName() + "_auto_pick_up_");
    }

    public static void setautoPick(@NotNull Player p, boolean Boolean) {
        status.put(p.getName() + "_auto_pick_up_", Boolean);
    }

    public static void savePlayerData(@NotNull Player p, String item) {
        getdatafile().set("players." + p.getName() + ".auto.Smelt", autoSmelt(p));
        getdatafile().set("players." + p.getName() + ".auto.Pick", autoPick(p));
        getdatafile().set("players." + p.getName() + ".items." + item + ".max", getMaxStorage(p, item));
        getdatafile().set("players." + p.getName() + ".items." + item + ".amount", getStorage(p, item));
        savedata();
    }
}
