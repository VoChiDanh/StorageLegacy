package net.danh.storage.Hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.danh.storage.Manager.Data;
import net.danh.storage.Manager.Files;
import net.danh.storage.Manager.Items;
import net.danh.storage.Storage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.danh.storage.Manager.Data.*;

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
            return String.valueOf(getStorage(p, item));
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
            return getCount(p, name);
        }
        if (identifier.startsWith("auto_smelt")) {
            if (Data.autoSmelt(p)) {
                return Files.getconfigfile().getString("Boolean.true");
            } else {
                return Files.getconfigfile().getString("Boolean.false");
            }
        }
        if (identifier.startsWith("auto_pickup")) {
            if (Data.autoPick(p)) {
                return Files.getconfigfile().getString("Boolean.true");
            } else {
                return Files.getconfigfile().getString("Boolean.false");
            }
        }
        if (identifier.startsWith("used_")) {
            String name = identifier.substring(5);
            return getUsed(p, name);
        }
        if (identifier.startsWith("empty_")) {
            String name = identifier.substring(6);
            return getEmpty(p, name);
        }
        if (identifier.startsWith("total_storage")) {
            return getTotalStorage(p);
        }
        if (identifier.startsWith("total_max_storage")) {
            return getTotalMaxStorage(p);
        }
        if (identifier.startsWith("total_used_storage")) {
            return getTotalUsed(p);
        }
        if (identifier.startsWith("total_empty_storage")) {
            return getTotalEmpty(p);
        }
        return null;
    }
}
