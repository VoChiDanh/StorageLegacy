package net.danh.storage.Manager;

import org.bukkit.entity.Player;

import java.util.Objects;

import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.*;
import static net.danh.storage.Storage.*;

public class Sell {

    private static int price;

    public static void SellItems(Player p, String name, Integer amount) {
        for (String getBlockType : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            if (name.equalsIgnoreCase(getBlockType)) {
                price = getconfigfile().getInt("Blocks." + name + ".Price");
                name = getconfigfile().getString("Blocks." + name + ".Name");
                break;
            }
        }
        removeStorage(p, name, amount);
        economy.depositPlayer(p, price * amount);
    }
}
