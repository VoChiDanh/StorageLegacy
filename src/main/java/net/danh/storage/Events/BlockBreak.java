package net.danh.storage.Events;

import net.danh.storage.Manager.Data;
import net.danh.storage.Manager.Files;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import preponderous.ponder.minecraft.bukkit.nms.NMSAssistant;

import java.util.List;
import java.util.Objects;

import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.getconfigfile;

public class BlockBreak implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreaking(@NotNull BlockBreakEvent e) {
        Player p = e.getPlayer();
        String blocks = e.getBlock().getType().toString();
        String items = null;
        NMSAssistant nms = new NMSAssistant();
        List<String> w = Files.getconfigfile().getStringList("Blacklist-World");
        if (!w.contains(p.getWorld().getName())) {
            if (autoPick(p)) {
                for (String getBlockType : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                    if (blocks.equalsIgnoreCase(getBlockType)) {
                        items = getconfigfile().getString("Blocks." + blocks + ".Name");
                        break;
                    }
                }
                if (items == null) {
                    return;
                }
                e.getBlock().getDrops().clear();
                if (nms.isVersionGreaterThan(11)) {
                    e.setDropItems(false);
                }
                if (getMaxStorage(p, items) == 0) {
                    setMaxStorage(p, items, getconfigfile().getInt("Default_Max_Storage"));
                }
                if (p.getItemInHand() != null && p.getItemInHand().getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
                    if (Data.getRandomInt(Files.getconfigfile().getInt("Fortune.Chance.System.Min"), Files.getconfigfile().getInt("Fortune.Chance.System.Max")) <= (p.getItemInHand().getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) * Files.getconfigfile().getInt("Fortune.Chance.Player"))) {
                        int fortune = Data.getRandomInt(Files.getconfigfile().getInt("Fortune.Drop.Min"), Files.getconfigfile().getInt("Fortune.Drop.Max"));
                        addStorage(p, items, 1 + fortune);
                    } else {
                        addStorage(p, items, 1);
                    }
                } else {
                    addStorage(p, items, 1);
                }
            }
        }
    }
}