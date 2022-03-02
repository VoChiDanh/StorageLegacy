package net.danh.storage.Manager;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

import java.util.Objects;

import static net.danh.storage.Manager.Data.removeStorage;
import static net.danh.storage.Manager.Files.*;
import static net.danh.storage.Storage.economy;

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
        int money = price * amount;
        EconomyResponse r = economy.depositPlayer(p, money);
        if (r.transactionSuccess()) {
            p.sendMessage(colorize(getlanguagefile().getString("Sell")
                    .replaceAll("%money%", String.valueOf(money))
                    .replaceAll("%item%", name.replaceAll("_", " "))));
        } else {
            p.sendMessage(colorize("&cError!"));
        }
    }
}
