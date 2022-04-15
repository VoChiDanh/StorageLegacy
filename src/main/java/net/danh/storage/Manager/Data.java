package net.danh.storage.Manager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Data {
    protected static final HashMap<String, Integer> data = new HashMap<>();
    protected static final HashMap<String, Boolean> status = new HashMap<>();

    public static int getStorageData(@NotNull Player p, String item) {
        return Files.getdatafile().getInt("players." + p.getName() + ".items." + item + ".amount");
    }

    public static int getStorage(@NotNull Player p, String item) {
        if (!data.containsKey(p.getName() + "_storage_" + item)) {
            data.put(p.getName() + "_storage_" + item, 0);
        }
        return data.get(p.getName() + "_storage_" + item);
    }

    public static void setStorage(@NotNull Player p, String item, Integer amount) {
        if (!data.containsKey(p.getName() + "_storage_" + item)) {
            data.put(p.getName() + "_storage_" + item, 0);
        } else {
            data.put(p.getName() + "_storage_" + item, amount);
        }
        if (Files.getconfigfile().getBoolean("Message.STATUS")) {
            p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.ADD")),
                    new TranslatableComponent(Files.colorize(Objects.requireNonNull(Files.getlanguagefile().getString("Receive_Item"))
                            .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                    .replaceAll("-", " "))
                            .replaceAll("%amount%", String.valueOf(amount))
                            .replaceAll("%storage%", String.format("%,d", getStorage(p, item)))
                            .replaceAll("%max%", String.format("%,d", getMaxStorage(p, item))))));
        }
    }

    public static void addStorage(@NotNull Player p, String item, Integer amount) {
        if (getMaxStorage(p, item) >= (getStorage(p, item) + amount)) {
            data.replace(p.getName() + "_storage_" + item, getStorage(p, item) + amount);
            if (Files.getconfigfile().getBoolean("Message.STATUS")) {
                p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.ADD")),
                        new TranslatableComponent(Files.colorize(Objects.requireNonNull(Files.getlanguagefile().getString("Receive_Item"))
                                .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                        .replaceAll("-", " "))
                                .replaceAll("%amount%", String.valueOf(amount))
                                .replaceAll("%storage%", String.format("%,d", getStorage(p, item)))
                                .replaceAll("%max%", String.format("%,d", getMaxStorage(p, item))))));
            }
        } else {
            data.put(p.getName() + "_storage_" + item, getMaxStorage(p, item));
            p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.FULL")),
                    new TranslatableComponent(Files.colorize(Objects.requireNonNull(Files.getlanguagefile().getString("Full_Storage"))
                            .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                    .replaceAll("-", " "))
                            .replaceAll("%amount%", String.valueOf(amount))
                            .replaceAll("%storage%", String.format("%,d", getStorage(p, item)))
                            .replaceAll("%max%", String.format("%,d", getMaxStorage(p, item))))));
        }
    }

    public static void removeStorage(@NotNull Player p, String item, Integer amount) {
        if (getStorage(p, item) > amount) {
            data.replace(p.getName() + "_storage_" + item, getStorage(p, item) - amount);
        } else {
            data.replace(p.getName() + "_storage_" + item, 0);
        }
        if (Files.getconfigfile().getBoolean("Message.STATUS")) {
            p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.REMOVE")),
                    new TranslatableComponent(Files.colorize(Objects.requireNonNull(Files.getlanguagefile().getString("Remove_Item"))
                            .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                    .replaceAll("-", " "))
                            .replaceAll("%amount%", String.valueOf(amount))
                            .replaceAll("%storage%", String.format("%,d", getStorage(p, item)))
                            .replaceAll("%max%", String.format("%,d", getMaxStorage(p, item))))));
        }
    }

    public static int getMaxStorageData(@NotNull Player p, String item) {
        return Files.getdatafile().getInt("players." + p.getName() + ".items." + item + ".max");
    }

    public static int getMaxStorage(@NotNull Player p, String item) {
        if (!data.containsKey(p.getName() + "_max_" + item)) {
            data.put(p.getName() + "_max_" + item, 0);
        }
        return data.get(p.getName() + "_max_" + item);
    }

    public static void setMaxStorage(@NotNull Player p, String item, Integer amount) {
        data.put(p.getName() + "_max_" + item, amount);
    }

    public static void addMaxStorage(@NotNull Player p, String item, Integer amount) {
        data.replace(p.getName() + "_max_" + item, getStorage(p, item) + amount);
    }

    public static void removeMaxStorage(@NotNull Player p, String item, Integer amount) {
        if (getMaxStorage(p, item) > amount) {
            data.replace(p.getName() + "_max_" + item, getStorage(p, item) - amount);
        } else {
            data.replace(p.getName() + "_max_" + item, 0);
        }
    }

    public static int getRandomInt(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }

    public static boolean autoSmeltData(@NotNull Player p) {
        return Files.getdatafile().getBoolean("players." + p.getName() + ".auto.Smelt");
    }

    public static boolean autoPickData(@NotNull Player p) {
        return Files.getdatafile().getBoolean("players." + p.getName() + ".auto.Pick");
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
        Files.getdatafile().set("players." + p.getName() + ".auto.Smelt", autoSmelt(p));
        Files.getdatafile().set("players." + p.getName() + ".auto.Pick", autoPick(p));
        Files.getdatafile().set("players." + p.getName() + ".items." + item + ".max", getMaxStorage(p, item));
        Files.getdatafile().set("players." + p.getName() + ".items." + item + ".amount", getStorage(p, item));
        Files.savedata();
    }
}
