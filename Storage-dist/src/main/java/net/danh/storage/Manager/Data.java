package net.danh.storage.Manager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class Data {

    public static int getStorage(@NotNull Player p, String item) {
        return Files.getdatafile().getInt("players." + p.getName() + ".items." + item + ".amount");
    }

    public static void setStorage(@NotNull Player p, String item, Integer amount) {
        Files.getdatafile().set("players." + p.getName() + ".items." + item + ".amount", amount);
        Files.savedata();
    }

    public static void addStorage(@NotNull Player p, String item, Integer amount) {
        if (getMaxStorage(p) >= (getStorage(p, item) + amount)) {
            Files.getdatafile().set("players." + p.getName() + ".items." + item + ".amount", getStorage(p, item) + amount);

            if (!Files.getconfigfile().getBoolean("Message.STATUS")) {
                p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.RECEIVE")),
                        new TranslatableComponent(Files.colorize(Objects.requireNonNull(Files.getlanguagefile().getString("Receive_Item"))
                                .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                        .replaceAll("-", " "))
                                .replaceAll("%amount%", String.valueOf(amount)))));
            }
        } else {
            Files.getdatafile().set("players." + p.getName() + ".items." + item + ".amount", getMaxStorage(p));
            p.sendMessage(Files.colorize(Objects.requireNonNull(Files.getlanguagefile().getString("Full_Storage"))
                    .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                            .replaceAll("-", " "))));
        }
        Files.savedata();
    }

    public static void removeStorage(@NotNull Player p, String item, Integer amount) {
        if (getStorage(p, item) > amount) {
            Files.getdatafile().set("players." + p.getName() + ".items." + item + ".amount", getStorage(p, item) - amount);
        } else {
            Files.getdatafile().set("players." + p.getName() + ".items." + item + ".amount", 0);
        }
        if (!Files.getconfigfile().getBoolean("Message.STATUS")) {
            p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.RECEIVE")),
                    new TranslatableComponent(Files.colorize(Objects.requireNonNull(Files.getlanguagefile().getString("Remove_Item"))
                            .replaceAll("%item%", Items.getName(item).replaceAll("_", " ")
                                    .replaceAll("-", " "))
                            .replaceAll("%amount%", String.valueOf(amount)))));
        }
        Files.savedata();
    }

    public static int getMaxStorage(@NotNull Player p) {
        return Files.getdatafile().getInt("players." + p.getName() + ".max");
    }

    public static void setMaxStorage(@NotNull Player p, Integer amount) {
        Files.getdatafile().set("players." + p.getName() + ".max", amount);
        Files.savedata();
    }

    public static void addMaxStorage(@NotNull Player p, Integer amount) {
        Files.getdatafile().set("players." + p.getName() + ".max", getMaxStorage(p) + amount);
        Files.savedata();
    }

    public static void removeMaxStorage(@NotNull Player p, Integer amount) {
        if (getMaxStorage(p) > amount) {
            Files.getdatafile().set("players." + p.getName() + ".max", getMaxStorage(p) - amount);
        } else {
            Files.getdatafile().set("players." + p.getName() + ".max", 0);
        }
        Files.savedata();
    }

    public static int getRandomInt(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }

    public static boolean autoSmelt(@NotNull Player p) {
        return Files.getdatafile().getBoolean("players." + p.getName() + ".auto.Smelt");
    }

    public static void setautoSmelt(@NotNull Player p, boolean Boolean) {
        Files.getdatafile().set("players." + p.getName() + ".auto.Smelt", Boolean);
        Files.savedata();
    }

    public static boolean autoPick(@NotNull Player p) {
        return Files.getdatafile().getBoolean("players." + p.getName() + ".auto.Pick");
    }

    public static void setautoPick(@NotNull Player p, boolean Boolean) {
        Files.getdatafile().set("players." + p.getName() + ".auto.Pick", Boolean);
        Files.savedata();
    }
}
