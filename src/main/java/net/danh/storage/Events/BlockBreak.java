package net.danh.storage.Events;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

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
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
        Location loc = new com.sk89q.worldedit.util.Location(localPlayer.getWorld(), e.getBlock().getLocation().getBlockX(), e.getBlock().getLocation().getBlockY(), e.getBlock().getLocation().getBlockZ());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        if (!query.testState(loc, localPlayer, Flags.BLOCK_BREAK) && !p.hasPermission("Storage.admin")) {
            e.setCancelled(true);
            return;
        }
        e.getBlock().getDrops().clear();
        e.setDropItems(false);
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
                if (getMaxStorage(p, blocks) == 0) {
                    setMaxStorage(p, blocks, getconfigfile().getInt("Default_Max_Storage"));
                }
                if (Objects.requireNonNull(p.getItemInHand().getItemMeta()).hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
                    if (getRandomInt(getconfigfile().getInt("Fortune.Chance.System.Min"), getconfigfile().getInt("Fortune.Chance.System.Max")) <= (p.getItemInHand().getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) * getconfigfile().getInt("Fortune.Chance.Player"))) {
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