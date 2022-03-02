package net.danh.storage.Manager;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

import static net.danh.storage.Manager.Data.getStorage;
import static net.danh.storage.Manager.Data.removeStorage;
import static net.danh.storage.Manager.Files.*;
import static net.danh.storage.Storage.economy;

public class Sell {

    private static int price;

    public static void SellItems(Player p, String name, Integer amount) {
        if (getStorage(p, name) >= amount) {
            for (String getBlockType : getconfigfile().getConfigurationSection("Blocks.").getKeys(false)) {
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
        } else {
            p.sendMessage(colorize(getlanguagefile().getString("Not_Enough")));
        }
    }
}
