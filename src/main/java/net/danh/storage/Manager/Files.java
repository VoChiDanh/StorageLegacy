package net.danh.storage.Manager;

import me.clip.placeholderapi.PlaceholderAPI;
import net.danh.dcore.NMS.NMSAssistant;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static net.danh.storage.Gui.Manager.getpickupcooldown;
import static net.danh.storage.Gui.Manager.getsmeltcooldown;
import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Items.getPrice;
import static net.danh.storage.Storage.*;

public class Files {

    private static File configFile, languageFile, dataFile, guiFile;
    private static FileConfiguration config, language, data, gui;

    public static void createfiles() {
        NMSAssistant nmsAssistant = new NMSAssistant();
        if (nmsAssistant.isVersionGreaterThan(13)) {
            guiFile = new File(get().getDataFolder(), "gui.yml");
            configFile = new File(get().getDataFolder(), "config.yml");
        } else {
            guiFile = new File(get().getDataFolder(), "gui-legacy.yml");
            configFile = new File(get().getDataFolder(), "config-legacy.yml");
        }
        languageFile = new File(get().getDataFolder(), "language.yml");
        dataFile = new File(get().getDataFolder(), "data.yml");

        if (!configFile.exists()) {
            if (nmsAssistant.isVersionGreaterThan(13)) {
                get().saveResource("config.yml", false);
            } else {
                get().saveResource("config-legacy.yml", false);
            }
        }
        if (!languageFile.exists()) get().saveResource("language.yml", false);
        if (!dataFile.exists()) get().saveResource("data.yml", false);
        if (!guiFile.exists()) {
            if (nmsAssistant.isVersionGreaterThan(13)) {
                get().saveResource("gui.yml", false);
            } else {
                get().saveResource("gui-legacy.yml", false);
            }
        }
        language = new YamlConfiguration();
        data = new YamlConfiguration();
        gui = new YamlConfiguration();
        config = new YamlConfiguration();

        try {
            language.load(languageFile);
            data.load(dataFile);
            gui.load(guiFile);
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getconfigfile() {
        return config;
    }

    public static FileConfiguration getlanguagefile() {
        return language;
    }

    public static FileConfiguration getdatafile() {
        return data;
    }

    public static FileConfiguration getguifile() {
        return gui;
    }

    public static void reloadfiles() {
        language = YamlConfiguration.loadConfiguration(languageFile);
        gui = YamlConfiguration.loadConfiguration(guiFile);
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void saveconfig() {
        try {
            config.save(configFile);
        } catch (IOException ignored) {
        }
    }

    public static void savelanguage() {
        try {
            language.save(languageFile);
        } catch (IOException ignored) {
        }
    }

    public static void savedata() {
        try {
            data.save(dataFile);
        } catch (IOException ignored) {
        }
    }

    public static void savegui() {
        try {
            gui.save(guiFile);
        } catch (IOException ignored) {
        }
    }

    /**
     * @param input Something
     * @return if it is int, return true
     */
    public static boolean isInt(String input) {
        String regex = "-?\\d+";
        if (input == null) {
            return false;
        }
        return input.matches(regex);
    }

    /**
     * @param input String with #placeholder_...#
     * @param p     Player
     * @return String with replace #placeholder_...# to data
     */
    public static String papi(String input, Player p) {
        String output = input.replaceAll("#total_storage#", getTotalStorage(p)).replaceAll("#total_max_storage#", getTotalMaxStorage(p)).replaceAll("#total_used#", getTotalUsed(p)).replaceAll("#total_empty#", getTotalEmpty(p)).replaceAll("#total_count#", getTotalCount(p)).replaceAll("#player#", p.getName()).replaceAll("#cooldown_pickup#", String.valueOf(getpickupcooldown(p))).replaceAll("#cooldown_smelt#", String.valueOf(getsmeltcooldown(p)));
        if (ecostatus) {
            output = output.replaceAll("#money#", String.valueOf(economy.getBalance(p)));
            output = output.replaceAll("#money_commas#", String.format("%,d", (long) economy.getBalance(p)));
            output = output.replaceAll("#money_fixed#", String.valueOf((long) economy.getBalance(p)));
        } else {
            output = output.replaceAll("#money#", "null");
            output = output.replaceAll("#money_commas#", "null");
            output = output.replaceAll("#money_fixed#", "null");
        }
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            output = output.replaceAll("#storage_" + item + "#", String.valueOf(getStorage(p, item)));
        }
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            output = output.replaceAll("#max_storage_" + item + "#", String.valueOf(getMaxStorage(p, item)));
        }
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            output = output.replaceAll("#used_" + item + "#", getUsed(p, item));
        }
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            output = output.replaceAll("#empty_" + item + "#", getEmpty(p, item));
        }
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            output = output.replaceAll("#count_" + item + "#", getCount(p, item));
        }
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            output = output.replaceAll("#price_" + item + "#", String.valueOf(getPrice(item)));
        }
        if (autoPick(p)) {
            output = output.replaceAll("#auto_pickup#", getconfigfile().getString("Boolean.true"));
        } else {
            output = output.replaceAll("#auto_pickup#", getconfigfile().getString("Boolean.false"));
        }
        if (autoSmelt(p)) {
            output = output.replaceAll("#auto_smelt#", getconfigfile().getString("Boolean.true"));
        } else {
            output = output.replaceAll("#auto_smelt#", getconfigfile().getString("Boolean.false"));
        }
        output = PlaceholderAPI.setPlaceholders(p, output);
        return output;
    }

    /**
     * @param lores List String with #placeholder_...#
     * @param p     Player
     * @return List String with replace #placeholder_...# to data
     */
    public static List<String> lorepapi(List<String> lores, Player p) {
        List<String> final_lores = new ArrayList<>();
        for (String input : lores) {
            String output = input.replaceAll("#total_storage#", getTotalStorage(p)).replaceAll("#total_max_storage#", getTotalMaxStorage(p)).replaceAll("#total_used#", getTotalUsed(p)).replaceAll("#total_empty#", getTotalEmpty(p)).replaceAll("#total_count#", getTotalCount(p)).replaceAll("#player#", p.getName()).replaceAll("#cooldown_pickup#", String.valueOf(getpickupcooldown(p))).replaceAll("#cooldown_smelt#", String.valueOf(getsmeltcooldown(p)));
            if (ecostatus) {
                output = output.replaceAll("#money#", String.valueOf(economy.getBalance(p)));
                output = output.replaceAll("#money_commas#", String.format("%,d", (long) economy.getBalance(p)));
                output = output.replaceAll("#money_fixed#", String.valueOf((long) economy.getBalance(p)));
            } else {
                output = output.replaceAll("#money#", "null");
                output = output.replaceAll("#money_commas#", "null");
                output = output.replaceAll("#money_fixed#", "null");
            }
            for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                output = output.replaceAll("#storage_" + item + "#", String.valueOf(getStorage(p, item)));
            }
            for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                output = output.replaceAll("#max_storage_" + item + "#", String.valueOf(getMaxStorage(p, item)));
            }
            for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                output = output.replaceAll("#used_" + item + "#", getUsed(p, item));
            }
            for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                output = output.replaceAll("#empty_" + item + "#", getEmpty(p, item));
            }
            for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                output = output.replaceAll("#count_" + item + "#", getCount(p, item));
            }
            for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                output = output.replaceAll("#price_" + item + "#", String.valueOf(getPrice(item)));
            }
            if (autoPick(p)) {
                output = output.replaceAll("#auto_pickup#", getconfigfile().getString("Boolean.true"));
            } else {
                output = output.replaceAll("#auto_pickup#", getconfigfile().getString("Boolean.false"));
            }
            if (autoSmelt(p)) {
                output = output.replaceAll("#auto_smelt#", getconfigfile().getString("Boolean.true"));
            } else {
                output = output.replaceAll("#auto_smelt#", getconfigfile().getString("Boolean.false"));
            }
            output = PlaceholderAPI.setPlaceholders(p, output);
            final_lores.add(output);
        }
        return final_lores;
    }
}