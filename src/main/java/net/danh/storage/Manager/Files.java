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

import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Storage.*;

public class Files {

    public final static char COLOR_CHAR = ChatColor.COLOR_CHAR;
    private static File configFile, languageFile, dataFile, guiFile;
    private static FileConfiguration config, language, data, gui;

    public static void createfiles() {
        configFile = new File(get().getDataFolder(), "config.yml");
        languageFile = new File(get().getDataFolder(), "language.yml");
        dataFile = new File(get().getDataFolder(), "data.yml");
        guiFile = new File(get().getDataFolder(), "gui.yml");

        if (!configFile.exists()) get().saveResource("config.yml", false);
        if (!languageFile.exists()) get().saveResource("language.yml", false);
        if (!dataFile.exists()) get().saveResource("data.yml", false);
        if (!guiFile.exists()) get().saveResource("gui.yml", false);
        config = new YamlConfiguration();
        language = new YamlConfiguration();
        data = new YamlConfiguration();
        gui = new YamlConfiguration();

        try {
            config.load(configFile);
            language.load(languageFile);
            data.load(dataFile);
            gui.load(guiFile);
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
        config = YamlConfiguration.loadConfiguration(configFile);
        language = YamlConfiguration.loadConfiguration(languageFile);
        gui = YamlConfiguration.loadConfiguration(guiFile);
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
            output.add(ChatColor.translateAlternateColorCodes('&', string));
        }
        return output;
    }

    public static @NotNull String translateHexColorCodes(String startTag, String endTag, String message) {

        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);

        while (matcher.find()) {

            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x" + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1) + COLOR_CHAR
                    + group.charAt(2) + COLOR_CHAR + group.charAt(3) + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5));

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
        String output = input.replaceAll("#total_storage#", getTotalStorage(p))
                .replaceAll("#total_max_storage#", getTotalMaxStorage(p))
                .replaceAll("#total_used#", getTotalUsed(p))
                .replaceAll("#total_empty#", getTotalEmpty(p))
                .replaceAll("#total_count#", getTotalCount(p))
                .replaceAll("#player#", p.getName());
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
            String output = input.replaceAll("#total_storage#", getTotalStorage(p))
                    .replaceAll("#total_max_storage#", getTotalMaxStorage(p))
                    .replaceAll("#total_used#", getTotalUsed(p))
                    .replaceAll("#total_empty#", getTotalEmpty(p))
                    .replaceAll("#total_count#", getTotalCount(p))
                    .replaceAll("#player#", p.getName());
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
