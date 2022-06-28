package net.danh.storage.Events;

import net.danh.dcore.NMS.NMSAssistant;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static net.danh.dcore.Random.Number.getRandomInt;
import static net.danh.storage.Manager.Data.addStorage;
import static net.danh.storage.Manager.Data.autoPick;
import static net.danh.storage.Manager.Files.getconfigfile;

public class BlockBreak implements Listener {
    public static void drop(BlockBreakEvent e, Player p, String blocks) {
        if (getconfigfile().getBoolean("Vanilla_Drops")) {
            if (getconfigfile().getBoolean("Tool_Sensitive")) {
                int amount = e.getBlock().getDrops(p.getInventory().getItemInMainHand()).size();
                if (amount > 0) {
                    addStorage(p, blocks, amount);
                }
            } else {
                int amount = e.getBlock().getDrops().size();
                addStorage(p, blocks, amount);
            }
        } else {
            if (getconfigfile().getBoolean("Tool_Sensitive")) {
                int amount = e.getBlock().getDrops(p.getInventory().getItemInMainHand()).size();
                if (amount > 0) {
                    addStorage(p, blocks, 1);
                }
            } else {
                addStorage(p, blocks, 1);
            }
        }
    }

    public static void fortune(BlockBreakEvent e, Player p, String blocks, int level) {
        int amount = e.getBlock().getDrops(p.getInventory().getItemInMainHand()).size();
        if (amount > 0) {
            int chance = getconfigfile().getInt("Fortune.Chance") * level;
            if (getRandomInt(1, 100) <= chance) {
                int fortune = getRandomInt(getconfigfile().getInt("Fortune.Drop.Min"), getconfigfile().getInt("Fortune.Drop.Max"));
                addStorage(p, blocks, fortune);
            } else {
                addStorage(p, blocks, 1);
            }
        }
    }

    public boolean isPlacedBlock(Block b) {
        List<MetadataValue> metaDataValues = b.getMetadata("PlacedBlock");
        for (MetadataValue value : metaDataValues) {
            return value.asBoolean();
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreaking(@NotNull BlockBreakEvent e) {
        Player p = e.getPlayer();
        String blocks = e.getBlock().getType().toString();
        String items = null;
        NMSAssistant nmsAssistant = new NMSAssistant();
        if (nmsAssistant.isVersionLessThanOrEqualTo(12)) {
            String material = e.getBlock().getType().toString();
            short damaged = e.getBlock().getData();
            if (damaged != 0) {
                String data = String.valueOf(damaged);
                blocks = material + ";" + data;
            } else {
                if (!e.getBlock().getType().equals(Material.GLOWING_REDSTONE_ORE)) {
                    blocks = e.getBlock().getType().toString();
                } else {
                    blocks = Material.REDSTONE_ORE.toString();
                }
            }
        }
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
                if (!isPlacedBlock(e.getBlock()) && p.getInventory().getItemInMainHand().getItemMeta() != null && Objects.requireNonNull(p.getInventory().getItemInMainHand().getItemMeta()).hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
                    if (nmsAssistant.isVersionGreaterThanOrEqualTo(12)) {
                        if (getconfigfile().getBoolean("Fortune.Vanilla")) {
                            int amount = e.getBlock().getDrops(p.getInventory().getItemInMainHand()).size();
                            if (amount > 0) {
                                addStorage(p, blocks, amount);
                            } else {
                                return;
                            }
                        } else {
                            fortune(e, p, blocks, p.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS));
                        }
                    } else {
                        fortune(e, p, blocks, p.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS));
                    }
                } else {
                    drop(e, p, blocks);
                }
                e.getBlock().getDrops().clear();
                if (nmsAssistant.isVersionGreaterThanOrEqualTo(12)) {
                    e.setDropItems(false);
                }
            }
        }
    }
}