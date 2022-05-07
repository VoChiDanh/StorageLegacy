package net.danh.storage.Gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.danh.storage.Gui.CatchInput.*;
import static net.danh.storage.Gui.LoadMenu.*;
import static net.danh.storage.Gui.Manager.update_task;
import static net.danh.storage.Gui.OpenGui.OpenGui;
import static net.danh.storage.Gui.OpenGui.gui;
import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.*;
import static net.danh.storage.Storage.get;

public class GuiEventListener implements Listener {
    public static Player gplayer;
    public static List<Player> input = new ArrayList<>();
    public static HashMap<Player, Object> input_result = new HashMap<>();
    private String block;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        if (input.contains(p)) {
            event.setCancelled(true);
            String message = event.getMessage();
            if (cancel.contains(message)) {
                input.remove(p);
                p.sendMessage(colorize(getlanguagefile().getString("Input.Cancel")));
            } else {
                if (getconfigfile().getString("Input.Type").equalsIgnoreCase("ONE")) {
                    if (message.contains(" ")) {
                        p.sendMessage(colorize(getlanguagefile().getString("Input.Space_Error")));
                        input.remove(p);
                    } else {
                        if (isInt(message)) {
                            try {
                                if (Integer.parseInt(message) > 0) {
                                    input_result.put(p, message);
                                    InputAction(p, block_input.get(p));
                                    input.remove(p);
                                } else {
                                    p.sendMessage(colorize(getlanguagefile().getString("Input.Invaild_Number")));
                                    input.remove(p);
                                }
                            } catch (Exception e) {
                                p.sendMessage(colorize(getlanguagefile().getString("Number_To_Big")));
                                input.remove(p);
                            }
                        } else {
                            p.sendMessage(colorize(getlanguagefile().getString("Input.Not_A_Number")));
                            input.remove(p);
                        }
                    }
                }
                if (getconfigfile().getString("Input.Type").equalsIgnoreCase("AGAIN")) {
                    if (message.contains(" ")) {
                        p.sendMessage(colorize(getlanguagefile().getString("Input.Space_Error") + " " + getlanguagefile().getString("Input.Again")));
                    } else {
                        if (isInt(message)) {
                            try {
                                if (Integer.parseInt(message) > 0) {
                                    input_result.put(p, message);
                                    InputAction(p, block_input.get(p));
                                    input.remove(p);
                                } else {
                                    p.sendMessage(colorize(getlanguagefile().getString("Input.Invaild_Number") + " " + getlanguagefile().getString("Input.Again")));
                                }
                            } catch (Exception e) {
                                p.sendMessage(colorize(getlanguagefile().getString("Number_To_Big")));
                            }
                        } else {
                            p.sendMessage(colorize(getlanguagefile().getString("Input.Not_A_Number") + " " + getlanguagefile().getString("Input.Again")));
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClickInv(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        if (!event.getInventory().equals(gui) || !event.getInventory().equals(player_gui.get(p))) return;
        if (event.getClickedInventory() == null) return;
        else {
            if (event.getClickedInventory().equals(player_gui.get(p))) {
                if (event.getSlot() == pickup_buttons_slot) {
                    setautoPick(p, !autoPick(p));
                    update_task.get(p).cancel();
                    OpenGui(p);
                }
                if (event.getSlot() == smelt_buttons_slot) {
                    setautoSmelt(p, !autoSmelt(p));
                    update_task.get(p).cancel();
                    OpenGui(p);
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
