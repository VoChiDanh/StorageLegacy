package net.danh.storage.Gui.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.danh.storage.Gui.CatchInput.InputCatch;
import static net.danh.storage.Gui.Loader.LoadMenu.*;
import static net.danh.storage.Gui.OpenGui.OpenGui;
import static net.danh.storage.Gui.OpenGui.gui;
import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Storage.get;

public class GuiClickListener implements Listener {
    public static Player gplayer;
    private String block;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClickInv(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        if (!event.getInventory().equals(gui) || !event.getInventory().equals(player_gui.get(p))) return;
        else {
            if (event.getClickedInventory().equals(player_gui.get(p))) {
                if (event.getSlot() == pickup_buttons_slot) {
                    setautoPick(p, !autoPick(p));
                    OpenGui(p);
                    SaveMenu(p);
                    ReloadMenu();
                }
                if (event.getSlot() == smelt_buttons_slot) {
                    setautoSmelt(p, !autoSmelt(p));
                    OpenGui(p);
                    SaveMenu(p);
                    ReloadMenu();
                }
            }
            int i = 0;
            for (List<String> a : player_actions.get(p)) {
                if (event.getClickedInventory().equals(player_gui.get(p)) && event.getSlot() == player_actions_slot.get(p).get(i)) {
                    for (String s : a) {
                        String[] action = s.split("`");
                        String click = event.getClick().toString();
                        if (click.equalsIgnoreCase(action[0])) {
                            gplayer = p;
                            event.setCancelled(true);
                            List<String> ab = player_actions_block.get(p);
                            block = ab.get(i);
                            if (action[1].equalsIgnoreCase("TAKE_ALL")) {
                                new BukkitRunnable() {
                                    public void run() {
                                        p.closeInventory();
                                    }
                                }.runTaskLater(get(), 1);
                                new BukkitRunnable() {
                                    public void run() {
                                        p.performCommand("storage take " + block + " all");
                                    }
                                }.runTaskLater(get(), 2);
                            }
                            if (action[1].equalsIgnoreCase("ADD_ALL")) {
                                new BukkitRunnable() {
                                    public void run() {
                                        p.closeInventory();
                                    }
                                }.runTaskLater(get(), 1);
                                new BukkitRunnable() {
                                    public void run() {
                                        p.performCommand("storage add " + block + " all");
                                    }
                                }.runTaskLater(get(), 2);
                            }
                            if (action[1].equalsIgnoreCase("SELL_ALL")) {
                                new BukkitRunnable() {
                                    public void run() {
                                        p.closeInventory();
                                    }
                                }.runTaskLater(get(), 1);
                                new BukkitRunnable() {
                                    public void run() {
                                        p.performCommand("storage sell " + block + " all");
                                    }
                                }.runTaskLater(get(), 2);
                            }
                            if (action[1].equalsIgnoreCase("INPUT_TAKE")) {
                                new BukkitRunnable() {
                                    public void run() {
                                        p.closeInventory();
                                    }
                                }.runTaskLater(get(), 1);
                                new BukkitRunnable() {
                                    public void run() {
                                        InputCatch(p, "take", block);
                                    }
                                }.runTaskLater(get(), 2);
                            }
                            if (action[1].equalsIgnoreCase("INPUT_ADD")) {
                                new BukkitRunnable() {
                                    public void run() {
                                        p.closeInventory();
                                    }
                                }.runTaskLater(get(), 1);
                                new BukkitRunnable() {
                                    public void run() {
                                        InputCatch(p, "add", block);
                                    }
                                }.runTaskLater(get(), 2);
                            }
                            if (action[1].equalsIgnoreCase("INPUT_SELL")) {
                                new BukkitRunnable() {
                                    public void run() {
                                        p.closeInventory();
                                    }
                                }.runTaskLater(get(), 1);
                                new BukkitRunnable() {
                                    public void run() {
                                        InputCatch(p, "sell", block);
                                    }
                                }.runTaskLater(get(), 2);
                            }
                        }
                    }
                }
                i++;
            }
        }
        event.setCancelled(true);
    }
}
