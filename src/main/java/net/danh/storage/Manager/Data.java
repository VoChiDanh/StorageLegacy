package net.danh.storage.Manager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static net.danh.dcore.Utils.Chat.colorize;
import static net.danh.storage.Manager.Files.*;

public class Data {
    private static final HashMap<String, Integer> data = new HashMap<>();
    private static final HashMap<String, Boolean> status = new HashMap<>();

    /**
     * @param p    Player
     * @param item Material
     * @return Amount of item in player's storage saved in data.yml
     */
    public static int getStorageData(@NotNull Player p, String item) {
        item = item.toUpperCase();
        return getdatafile().getInt("players." + p.getName() + ".items." + item + ".amount");
    }

    /**
     * @param p    Player
     * @param item Material
     * @return Amount of item in player's storage (this data can change when player do something)
     */
    public static int getStorage(@NotNull Player p, String item) {
        item = item.toUpperCase();
        return data.get(p.getName() + "_storage_" + item);
    }

    /**
     * @param p      Player
     * @param item   Material
     * @param amount Amount
     */
    public static void setStorage(@NotNull Player p, String item, Integer amount) {
        item = item.toUpperCase();
        data.put(p.getName() + "_storage_" + item, Math.max(amount, 0));
    }

    /**
     * @param p      Player
     * @param item   Material
     * @param amount Amount
     */
    public static void addStorage(@NotNull Player p, String item, Integer amount) {
        item = item.toUpperCase();
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

    /**
     * @param p      Player
     * @param item   Material
     * @param amount Amount
     */
    public static void removeStorage(@NotNull Player p, String item, Integer amount) {
        item = item.toUpperCase();
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

    /**
     * @param p    Player
     * @param item Material
     * @return Amount of max item in player's storage saved in data.yml
     */
    public static int getMaxStorageData(@NotNull Player p, String item) {
        item = item.toUpperCase();
        return getdatafile().getInt("players." + p.getName() + ".items." + item + ".max");
    }

    /**
     * @param p    Player
     * @param item Material
     * @return Amount of item in player's storage (this data can change when player do something)
     */
    public static int getMaxStorage(@NotNull Player p, String item) {
        item = item.toUpperCase();
        return data.get(p.getName() + "_max_" + item);
    }

    /**
     * @param p      Player
     * @param item   Material
     * @param amount Amount
     */
    public static void setMaxStorage(@NotNull Player p, String item, Integer amount) {
        item = item.toUpperCase();
        data.put(p.getName() + "_max_" + item, Math.max(amount, getconfigfile().getInt("Default_Max_Storage")));
    }


    /**
     * @param p      Player
     * @param item   Material
     * @param amount Amount
     */
    public static void addMaxStorage(@NotNull Player p, String item, Integer amount) {
        item = item.toUpperCase();
        data.replace(p.getName() + "_max_" + item, getMaxStorage(p, item) + amount);
    }


    /**
     * @param p      Player
     * @param item   Material
     * @param amount Amount
     */
    public static void removeMaxStorage(@NotNull Player p, String item, Integer amount) {
        item = item.toUpperCase();
        if (getMaxStorage(p, item) > amount) {
            data.replace(p.getName() + "_max_" + item, getMaxStorage(p, item) - amount);
        } else {
            data.replace(p.getName() + "_max_" + item, 0);
        }
    }

    public static List<String> getPlayers() {
        return getdatafile().getStringList("All_players");
    }

    public static void addPlayers(@NotNull Player p) {
        List<String> players = getdatafile().getStringList("All_players");
        players.add(p.getName());
        Collections.sort(players);
        getdatafile().set("All_players", players);
        savedata();
    }

    /**
     * @param p Player
     * @return Autosmelt data (true/false) in data.yml
     */
    public static boolean autoSmeltData(@NotNull Player p) {
        return getdatafile().getBoolean("players." + p.getName() + ".auto.Smelt");
    }


    /**
     * @param p Player
     * @return Autopickup data (true/false) in data.yml
     */
    public static boolean autoPickData(@NotNull Player p) {
        return getdatafile().getBoolean("players." + p.getName() + ".auto.Pick");
    }

    /**
     * @param p Player
     * @return Autosmelt data in game when player online (This data can change when player do something)
     */
    public static boolean autoSmelt(@NotNull Player p) {
        return status.get(p.getName() + "_auto_smelt_");
    }

    /**
     * @param p       Player
     * @param Boolean true/false
     */
    public static void setautoSmelt(@NotNull Player p, boolean Boolean) {
        status.put(p.getName() + "_auto_smelt_", Boolean);
    }

    /**
     * @param p Player
     * @return Autopickup data in game when player online (This data can change when player do something)
     */
    public static boolean autoPick(@NotNull Player p) {
        return status.get(p.getName() + "_auto_pick_up_");
    }

    /**
     * @param p       Player
     * @param Boolean true/false
     */
    public static void setautoPick(@NotNull Player p, boolean Boolean) {
        status.put(p.getName() + "_auto_pick_up_", Boolean);
    }

    /**
     * @param p    Player
     * @param name Material
     * @return Percent used
     */
    public static String getUsed(Player p, String name) {
        float min = getStorage(p, name);
        float max = getMaxStorage(p, name);
        double n = (min / max) * 100;
        double f = max / 100;
        n = Math.round(n * f) / f;
        DecimalFormat df = new DecimalFormat(Objects.requireNonNull(Files.getconfigfile().getString("Number_Format")));
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(n) + "%";
    }

    /**
     * @param p    Player
     * @param name Material
     * @return Percent empty
     */
    public static String getEmpty(Player p, String name) {
        float min = Data.getMaxStorage(p, name) - getStorage(p, name);
        float max = Data.getMaxStorage(p, name);
        double n = (min / max) * 100;
        double f = max / 100;
        n = Math.round(n * f) / f;
        DecimalFormat df = new DecimalFormat(Objects.requireNonNull(Files.getconfigfile().getString("Number_Format")));
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(n) + "%";
    }

    /**
     * @param p    Player
     * @param name Material
     * @return Count item in storage
     */
    public static String getCount(Player p, String name) {
        return String.valueOf(Data.getMaxStorage(p, name) - getStorage(p, name));
    }

    /**
     * @param p Player
     * @return Total amount of all item
     */
    public static String getTotalStorage(Player p) {
        int total = 0;
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            int amount = getStorage(p, item);
            total += amount;
        }
        return String.valueOf(total);
    }

    /**
     * @param p Player
     * @return Total max of all item
     */
    public static String getTotalMaxStorage(Player p) {
        int total_max = 0;
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            int max = getMaxStorage(p, item);
            total_max += max;
        }
        return String.valueOf(total_max);
    }

    /**
     * @param p Player
     * @return Total Count of all item
     */
    public static String getTotalCount(Player p) {
        int total_count = 0;
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            int count = Integer.parseInt(getCount(p, item));
            total_count += count;
        }
        return String.valueOf(total_count);
    }

    /**
     * @param p Player
     * @return Total of all item
     */
    public static String getTotalUsed(Player p) {
        double max = 100.000;
        double min = Float.parseFloat(getTotalEmpty(p).replaceAll("%", "").replaceAll(",", "."));
        double u = max - min;
        DecimalFormat df = new DecimalFormat(Objects.requireNonNull(Files.getconfigfile().getString("Number_Format")));
        df.setRoundingMode(RoundingMode.HALF_UP);
        String d = getconfigfile().getString("Number_Format") + "1";
        d = d.replaceAll("#", "0");
        return df.format(u - Double.parseDouble(d)).replaceAll("-", "") + "%";
    }

    /**
     * @param p Player
     * @return Total empty of all item
     */
    public static String getTotalEmpty(Player p) {
        float total_max = 0;
        float total = 0;
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            float amount = getStorage(p, item);
            total += amount;
        }
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            float max = getMaxStorage(p, item);
            total_max += max;
        }
        float total_empty = total_max - total;
        double e = (total_empty / total_max) * 100;
        double f = total_max / 100;
        e = Math.round(e * f) / f;
        DecimalFormat df = new DecimalFormat(Objects.requireNonNull(Files.getconfigfile().getString("Number_Format")));
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(e) + "%";
    }

    /**
     * @param p    Player
     * @param item Material
     */
    public static void savePlayerData(@NotNull Player p, String item) {
        getdatafile().set("players." + p.getName() + ".auto.Smelt", autoSmelt(p));
        getdatafile().set("players." + p.getName() + ".auto.Pick", autoPick(p));
        getdatafile().set("players." + p.getName() + ".items." + item + ".max", getMaxStorage(p, item));
        getdatafile().set("players." + p.getName() + ".items." + item + ".amount", getStorage(p, item));
        savedata();
    }
}
