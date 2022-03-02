package net.danh.storage.Manager;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Data {

    public static int getStorage(@NotNull Player p, String item) {
        return Files.getdatafile().getInt("players." + p.getName() + ".items." + item + ".amount");
    }

    public static void setStorage(@NotNull Player p, String item, Integer amount) {
        Files.getdatafile().set("players." + p.getName() + ".items." + item + ".amount", amount);
        Files.savedata();
    }

    public static void addStorage(@NotNull Player p, String item, Integer amount) {
        if (getMaxStorage(p, item) <= (getStorage(p, item) + amount)) {
            Files.getdatafile().set("players." + p.getName() + ".items." + item + ".amount", getStorage(p, item) + amount);
            p.sendMessage(Files.colorize("&a+ " + amount + " " + item.replaceAll("_", " ")));
        } else {
            Files.getdatafile().set("players." + p.getName() + ".items." + item + ".amount", getMaxStorage(p, item));
            p.sendMessage(Files.colorize(Objects.requireNonNull(Files.getlanguagefile().getString("Full_Storage"))
                    .replaceAll("%item%", item.replaceAll("_", " ")
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
        Files.savedata();
    }

    public static int getMaxStorage(@NotNull Player p, String item) {
        return Files.getdatafile().getInt("players." + p.getName() + ".items." + item + ".max");
    }

    public static void setMaxStorage(@NotNull Player p, String item, Integer amount) {
        Files.getdatafile().set("players." + p.getName() + ".items." + item + ".max", amount);
        Files.savedata();
    }

    public static void addMaxStorage(@NotNull Player p, String item, Integer amount) {
        Files.getdatafile().set("players." + p.getName() + ".items." + item + ".max", getMaxStorage(p, item) + amount);
        Files.savedata();
    }

    public static void removeMaxStorage(@NotNull Player p, String item, Integer amount) {
        if (getMaxStorage(p, item) > amount) {
            Files.getdatafile().set("players." + p.getName() + ".items." + item + ".max", getMaxStorage(p, item) - amount);
        } else {
            Files.getdatafile().set("players." + p.getName() + ".items." + item + ".max", 0);
        }
        Files.savedata();
    }
}
