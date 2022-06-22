package net.danh.storage.Events;

import net.danh.storage.Storage;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class BlockPlace implements Listener {
    public void setMetaDataPlacedBlock(Block b, boolean placedBlock) {
        b.setMetadata("PlacedBlock", new FixedMetadataValue(Storage.get(), placedBlock));
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.getPlayer() != null) {
            setMetaDataPlacedBlock(e.getBlock(), true);
        }
    }
}
