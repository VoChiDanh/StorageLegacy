package net.danh.storage.Hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.danh.storage.Manager.Data;
import net.danh.storage.Manager.Files;
import net.danh.storage.Manager.Items;
import net.danh.storage.Storage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class PlaceholderAPI extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "Storage";
    }

    @Override
    public @NotNull String getAuthor() {
        return Storage.get().getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return Storage.get().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, @NotNull String identifier) {
        if (p == null) {
            return "Player not online";
        }
        if (identifier.startsWith("storage_")) {
            String item = identifier.substring(8);
            return String.valueOf(Data.getStorage(p, item));
        }
        if (identifier.startsWith("max_storage_")) {
            String item = identifier.substring(12);
            return String.valueOf(Data.getMaxStorage(p, item));
        }
        if (identifier.startsWith("price_")) {
            String material = identifier.substring(6);
            return String.valueOf(Items.getPrice(material));
        }
        if (identifier.startsWith("count_")) {
            String name = identifier.substring(6);
            return String.valueOf(Data.getMaxStorage(p, name) - Data.getStorage(p, name));
        }
        if (identifier.startsWith("auto_")) {
            String name = identifier.substring(5);
            if (name.contains("smelt")) {
                if (Data.autoSmelt(p)) {
                    return Files.getconfigfile().getString("Boolean.True");
                }
                if (!Data.autoSmelt(p)) {
                    return Files.getconfigfile().getString("Boolean.False");
                }
                return "";
            }
            if (name.contains("pickup")) {
                if (Data.autoPick(p)) {
                    return Files.getconfigfile().getString("Boolean.True");
                }
                if (!Data.autoPick(p)) {
                    return Files.getconfigfile().getString("Boolean.False");
                }
                return "";
            }
            return "";
        }
        if (identifier.startsWith("used_")) {
            DecimalFormat df = new DecimalFormat(Files.getconfigfile().getString("Number_Format"));
            df.setRoundingMode(RoundingMode.CEILING);
            String name = identifier.substring(5);
            return df.format((Data.getStorage(p, name) / Data.getMaxStorage(p, name)) * 100L) + "%";
        }
        if (identifier.startsWith("empty_")) {
            DecimalFormat df = new DecimalFormat(Files.getconfigfile().getString("Number_Format"));
            df.setRoundingMode(RoundingMode.CEILING);
            String name = identifier.substring(6);
            return df.format(((Data.getMaxStorage(p, name) - Data.getStorage(p, name)) / Data.getMaxStorage(p, name)) * 100L) + "%";
        }
        return null;
    }
}
