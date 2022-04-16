package net.danh.storage.Events;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.getconfigfile;

public class IA_BlockBreak implements Listener {

    @EventHandler
    public void onIABB(@NotNull CustomBlockBreakEvent e) {
        Player p = e.getPlayer();
        String blocks = e.getNamespacedID();
        String items = null;
        if (e.isCancelled()) return;
        List<String> w = getconfigfile().getStringList("Blacklist-World");
        if (!w.contains(p.getWorld().getName())) {
            if (autoPick(p)) {
                CustomBlock customBlock = CustomBlock.getInstance(blocks);
                if (customBlock != null) {
                    e.getBlock().getDrops().clear();
                    for (String getBlockType : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                        if (blocks.equalsIgnoreCase(getBlockType)) {
                            items = getconfigfile().getString("Blocks." + blocks + ".Name");
                            break;
                        }
                    }
                    if (items == null) {
                        return;
                    }
                    if (Objects.requireNonNull(p.getInventory().getItemInMainHand().getItemMeta()).hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
                        if (getRandomInt(getconfigfile().getInt("Fortune.Chance.System.Min"), getconfigfile().getInt("Fortune.Chance.System.Max")) <= (p.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) * getconfigfile().getInt("Fortune.Chance.Player"))) {
                            int fortune = getRandomInt(getconfigfile().getInt("Fortune.Drop.Min"), getconfigfile().getInt("Fortune.Drop.Max"));
                            addStorage(p, blocks, 1 + fortune);
                        } else {
                            addStorage(p, blocks, 1);
                        }
                    } else {
                        addStorage(p, blocks, 1);
                    }
                }
            }
        }
    }
}
