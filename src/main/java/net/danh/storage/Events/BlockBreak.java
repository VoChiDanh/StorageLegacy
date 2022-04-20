package net.danh.storage.Events;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import preponderous.ponder.minecraft.bukkit.nms.NMSAssistant;

import java.util.List;
import java.util.Objects;

import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.getconfigfile;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBreaking(@NotNull BlockBreakEvent e) {
        Player p = e.getPlayer();
        String blocks = e.getBlock().getType().toString();
        String items = null;
        NMSAssistant nmsAssistant = new NMSAssistant();
        if (nmsAssistant.isVersionLessThan(13)) {
            String material = e.getBlock().getType().toString();
            short damaged = e.getBlock().getData();
            if (damaged != 0) {
                String data = String.valueOf(damaged);
                blocks = material + ";" + data;
            } else {
                blocks = e.getBlock().getType().toString();
            }
        }
        if (e.isCancelled()) return;
        List<String> w = getconfigfile().getStringList("Blacklist-World");
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
                if (Objects.requireNonNull(p.getInventory().getItemInMainHand().getItemMeta()).hasEnchant(Enchantment.LOOT_BONUS_BLOCKS) && getRandomInt(getconfigfile().getInt("Fortune.Chance.System.Min"), getconfigfile().getInt("Fortune.Chance.System.Max")) <= (p.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) * getconfigfile().getInt("Fortune.Chance.Player"))) {
                    int fortune = getRandomInt(getconfigfile().getInt("Fortune.Drop.Min"), getconfigfile().getInt("Fortune.Drop.Max"));
                    addStorage(p, blocks, fortune);
                } else {
                    addStorage(p, blocks, 1);
                }
                e.getBlock().getDrops().clear();
                e.setDropItems(false);
            }
        }
    }
}