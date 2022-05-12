package net.danh.storage.Manager;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import preponderous.ponder.minecraft.bukkit.nms.NMSAssistant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.danh.storage.Gui.Manager.getpickupcooldown;
import static net.danh.storage.Gui.Manager.getsmeltcooldown;
import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Items.getPrice;
import static net.danh.storage.Storage.*;

public class Files {

    public final static char COLOR_CHAR = ChatColor.COLOR_CHAR;
    private static File configFile, configLegacyFile, languageFile, dataFile, guiFile, guilegacyFile;
    private static FileConfiguration config, configlegacy, language, data, gui, guilegacy;

    public static void createfiles() {
        languageFile = new File(get().getDataFolder(), "language.yml");
        dataFile = new File(get().getDataFolder(), "data.yml");
        NMSAssistant nms = new NMSAssistant();
        if (nms.isVersionGreaterThan(13)) {
            guiFile = new File(get().getDataFolder(), "gui.yml");
            configFile = new File(get().getDataFolder(), "config.yml");
        } else {
            guilegacyFile = new File(get().getDataFolder(), "gui-legacy.yml");
            configLegacyFile = new File(get().getDataFolder(), "config-legacy.yml");
        }
        if (!configFile.exists()) get().saveResource("config.yml", false);
        if (!languageFile.exists()) get().saveResource("language.yml", false);
        if (!dataFile.exists()) get().saveResource("data.yml", false);
        if (nms.isVersionGreaterThan(13)) {
            if (!guiFile.exists()) get().saveResource("gui.yml", false);
            if (!configFile.exists()) get().saveResource("config.yml", false);
        } else {
            if (!guilegacyFile.exists()) get().saveResource("gui-legacy.yml", false);
            if (!configLegacyFile.exists()) get().saveResource("config-legacy.yml", false);
        }
        language = new YamlConfiguration();
        data = new YamlConfiguration();
        if (nms.isVersionGreaterThan(13)) {
            gui = new YamlConfiguration();
            config = new YamlConfiguration();
        } else {
            guilegacy = new YamlConfiguration();
            configlegacy = new YamlConfiguration();
        }

        try {
            language.load(languageFile);
            data.load(dataFile);
            if (nms.isVersionGreaterThan(13)) {
                gui.load(guiFile);
                config.load(configFile);
            } else {
                guilegacy.load(guilegacyFile);
                configlegacy.load(configLegacyFile);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getconfigfile() {
        NMSAssistant nms = new NMSAssistant();
        if (nms.isVersionGreaterThan(13)) {
            return config;
        } else {
            return configlegacy;
        }
    }

    public static FileConfiguration getlanguagefile() {
        return language;
    }

    public static FileConfiguration getdatafile() {
        return data;
    }

    public static FileConfiguration getguifile() {
        NMSAssistant nms = new NMSAssistant();
        if (nms.isVersionGreaterThan(13)) {
            return gui;
        } else {
            return guilegacy;
        }
    }

    public static void reloadfiles() {
        NMSAssistant nms = new NMSAssistant();
        language = YamlConfiguration.loadConfiguration(languageFile);
        if (nms.isVersionGreaterThan(13)) {
            gui = YamlConfiguration.loadConfiguration(guiFile);
            config = YamlConfiguration.loadConfiguration(configFile);
        } else {
            guilegacy = YamlConfiguration.loadConfiguration(guilegacyFile);
            configlegacy = YamlConfiguration.loadConfiguration(configLegacyFile);
        }
    }

    public static void saveconfig() {
        NMSAssistant nms = new NMSAssistant();
        if (nms.isVersionGreaterThan(13)) {
            try {
                config.save(configFile);
            } catch (IOException ignored) {
            }
        } else {
            try {
                configlegacy.save(configLegacyFile);
            } catch (IOException ignored) {
            }
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
        NMSAssistant nms = new NMSAssistant();
        if (nms.isVersionGreaterThan(13)) {
            try {
                gui.save(guiFile);
            } catch (IOException ignored) {
            }
        } else {
            try {
                guilegacy.save(guilegacyFile);
            } catch (IOException ignored) {
            }
        }
    }

    // Colorize messages with preset color codes (&) and if using 1.16+, applies hex values via "&#hexvalue"
    public static String colorize(String input) {
        input = ChatColor.translateAlternateColorCodes('&', input);
        NMSAssistant nms = new NMSAssistant();
        if (nms.isVersionGreaterThan(15)) {
            input = translateHexColorCodes("\\&#", "", input);
        }
        return input;
    }

    public static List<String> lorecolor(List<String> input) {
        List<String> output = new ArrayList<>();
        for (String string : input) {
            String colorstring = ChatColor.translateAlternateColorCodes('&', string);
            NMSAssistant nms = new NMSAssistant();
            if (nms.isVersionGreaterThan(15)) {
                colorstring = translateHexColorCodes("\\&#", "", colorstring);
            }
            output.add(colorstring);
        }
        return output;
    }

    public static @NotNull String translateHexColorCodes(String startTag, String endTag, String message) {

        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);

        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x" + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1) + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3) + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5));

        }

        return matcher.appendTail(buffer).toString();
    }

    public static boolean isInt(String input) {
        String regex = "-?\\d+";
        if (input == null) {
            return false;
        }
        return input.matches(regex);
    }

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